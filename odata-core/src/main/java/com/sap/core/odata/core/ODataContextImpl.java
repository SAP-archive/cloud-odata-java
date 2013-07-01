package com.sap.core.odata.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.ODataDebugCallback;
import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.core.debug.ODataDebugResponseWrapper;

/**
 * @author SAP AG
 */
public class ODataContextImpl implements ODataContext {

  private static final String ODATA_REQUEST = "~odataRequest";
  private static final String DEBUG_MODE = "~debugMode";
  private static final String SERVICE = "~service";
  private static final String SERVICE_FACTORY = "~serviceFactory";
  private static final String PATH_INFO = "~pathInfo";
  private static final String RUNTIME_MEASUREMENTS = "~runtimeMeasurements";
  private static final String HTTP_METHOD = "~httpMethod";

  private Map<String, Object> parameterTable = new HashMap<String, Object>();

  private List<Locale> acceptableLanguages;

  public ODataContextImpl(final ODataRequest request, final ODataServiceFactory factory) {
    setServiceFactory(factory);
    setRequest(request);
    setPathInfo(request.getPathInfo());
    if (request.getMethod() != null) {
      setHttpMethod(request.getMethod().name());
    }
    setAcceptableLanguages(request.getAcceptableLanguages());
    setDebugMode(checkDebugMode(request.getQueryParameters()));
  }

  @Override
  public void setParameter(final String name, final Object value) {
    parameterTable.put(name, value);
  }

  @Override
  public void removeParameter(final String name) {
    parameterTable.remove(name);
  }

  @Override
  public Object getParameter(final String name) {
    return parameterTable.get(name);
  }

  @Override
  public boolean isInDebugMode() {
    return getParameter(DEBUG_MODE) != null && (Boolean) getParameter(DEBUG_MODE);
  }

  @Override
  public void setDebugMode(final boolean debugMode) {
    setParameter(DEBUG_MODE, debugMode);
  }

  public void setService(final ODataService service) {
    setParameter(SERVICE, service);
  }

  @Override
  public ODataService getService() throws ODataException {
    return (ODataService) getParameter(SERVICE);
  }

  public void setPathInfo(final PathInfo uriInfo) {
    setParameter(PATH_INFO, uriInfo);
  }

  @Override
  public PathInfo getPathInfo() throws ODataException {
    return (PathInfo) getParameter(PATH_INFO);
  }

  public void setServiceFactory(final ODataServiceFactory serviceFactory) {
    setParameter(SERVICE_FACTORY, serviceFactory);
  }

  @Override
  public ODataServiceFactory getServiceFactory() {
    return (ODataServiceFactory) getParameter(SERVICE_FACTORY);
  }

  @Override
  public int startRuntimeMeasurement(final String className, final String methodName) {
    if (isInDebugMode()) {
      final RuntimeMeasurement measurement = new RuntimeMeasurementImpl();
      measurement.setTimeStarted(System.nanoTime());
      measurement.setClassName(className);
      measurement.setMethodName(methodName);

      List<RuntimeMeasurement> runtimeMeasurements = getRuntimeMeasurements();
      if (runtimeMeasurements == null) {
        runtimeMeasurements = new ArrayList<RuntimeMeasurement>();
        setParameter(RUNTIME_MEASUREMENTS, runtimeMeasurements);
      }
      runtimeMeasurements.add(measurement);

      return runtimeMeasurements.size() - 1;
    } else {
      return 0;
    }
  }

  @Override
  public void stopRuntimeMeasurement(final int handle) {
    if (isInDebugMode()) {
      List<RuntimeMeasurement> runtimeMeasurements = getRuntimeMeasurements();
      if (runtimeMeasurements != null && handle >= 0 && handle < runtimeMeasurements.size()) {
        runtimeMeasurements.get(handle).setTimeStopped(System.nanoTime());
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<RuntimeMeasurement> getRuntimeMeasurements() {
    return (List<RuntimeMeasurement>) getParameter(RUNTIME_MEASUREMENTS);
  }

  protected class RuntimeMeasurementImpl implements RuntimeMeasurement {
    private String className;
    private String methodName;
    private long timeStarted;
    private long timeStopped;

    @Override
    public void setClassName(final String className) {
      this.className = className;
    }

    @Override
    public String getClassName() {
      return className;
    }

    @Override
    public void setMethodName(final String methodName) {
      this.methodName = methodName;
    }

    @Override
    public String getMethodName() {
      return methodName;
    }

    @Override
    public void setTimeStarted(final long start) {
      timeStarted = start;
    }

    @Override
    public long getTimeStarted() {
      return timeStarted;
    }

    @Override
    public void setTimeStopped(final long stop) {
      timeStopped = stop;
    }

    @Override
    public long getTimeStopped() {
      return timeStopped;
    }

    @Override
    public String toString() {
      return className + "." + methodName + ": " + (timeStopped - timeStarted);
    }
  }

  @Override
  @Deprecated
  public String getHttpRequestHeader(final String name) {
    ODataRequest request = (ODataRequest) parameterTable.get(ODATA_REQUEST);
    return request.getHeaderValue(name);
  }

  @Override
  @Deprecated
  public Map<String, String> getHttpRequestHeaders() {
    ODataRequest request = (ODataRequest) parameterTable.get(ODATA_REQUEST);
    return request.getHeaders();
  }

  @Override
  public String getRequestHeader(final String name) {
    ODataRequest request = (ODataRequest) parameterTable.get(ODATA_REQUEST);
    return request.getRequestHeaderValue(name);
  }

  @Override
  public Map<String, List<String>> getRequestHeaders() {
    ODataRequest request = (ODataRequest) parameterTable.get(ODATA_REQUEST);
    return request.getRequestHeaders();
  }

  @Override
  public List<Locale> getAcceptableLanguages() {
    return Collections.unmodifiableList(acceptableLanguages);
  }

  public void setAcceptableLanguages(final List<Locale> acceptableLanguages) {
    this.acceptableLanguages = acceptableLanguages;

    if (this.acceptableLanguages.isEmpty()) {
      final Locale wildcard = new Locale("*");
      this.acceptableLanguages.add(wildcard);
    }
  }

  public void setHttpMethod(final String httpMethod) {
    setParameter(HTTP_METHOD, httpMethod);
  }

  @Override
  public String getHttpMethod() {
    return (String) getParameter(HTTP_METHOD);
  }

  public void setRequest(final ODataRequest request) {
    setParameter(ODATA_REQUEST, request);
  }

  private boolean checkDebugMode(final Map<String, String> queryParameters) {
    if (getQueryDebugValue(queryParameters) == null) {
      return false;
    } else {
      final ODataDebugCallback callback = getServiceFactory().getCallback(ODataDebugCallback.class);
      return callback != null && callback.isDebugEnabled();
    }
  }

  private static String getQueryDebugValue(final Map<String, String> queryParameters) {
    final String debugValue = queryParameters.get(ODataDebugResponseWrapper.ODATA_DEBUG_QUERY_PARAMETER);
    return ODataDebugResponseWrapper.ODATA_DEBUG_JSON.equals(debugValue) ?
        debugValue : null;
  }

}
