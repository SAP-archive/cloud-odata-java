package com.sap.core.odata.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.uri.PathInfo;

/**
 * @author SAP AG
 */
public class ODataContextImpl implements ODataContext {

  private static final String ODATA_REQUEST = "~odataRequest";
  private static final String DEBUG_MODE = "~debugMode";
  private static final String SERVICE = "~service";
  private static final String PATH_INFO = "~pathInfo";
  private static final String RUNTIME_MEASUREMENTS = "~runtimeMeasurements";
  private static final String HTTP_METHOD = "~httpMethod";

  private final Map<String, Object> parameterTable = new HashMap<String, Object>();

  private List<Locale> acceptableLanguages;

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
    if (getParameter(DEBUG_MODE) != null) {
      return (Boolean) getParameter(DEBUG_MODE);
    }
    return false;
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

  @Override
  public int startRuntimeMeasurement(final String className, final String methodName) {
    if (isInDebugMode()) {
      final RuntimeMeasurement measurement = new RuntimeMeasurementImpl();
      measurement.setTimeStarted(System.nanoTime());
      measurement.setClassName(className);
      measurement.setMethodName(methodName);

      @SuppressWarnings("unchecked")
      List<RuntimeMeasurement> runtimeMeasurements = (List<RuntimeMeasurement>) getParameter(RUNTIME_MEASUREMENTS);
      if (runtimeMeasurements == null) {
        runtimeMeasurements = new ArrayList<RuntimeMeasurement>();
        setParameter(RUNTIME_MEASUREMENTS, runtimeMeasurements);
      }
      runtimeMeasurements.add(measurement);

      return runtimeMeasurements.size() - 1;
    }

    return 0;
  }

  @Override
  public void stopRuntimeMeasurement(final int handle) {
    if (isInDebugMode()) {
      @SuppressWarnings("unchecked")
      final List<RuntimeMeasurement> runtimeMeasurements = (List<RuntimeMeasurement>) getParameter(RUNTIME_MEASUREMENTS);

      if ((runtimeMeasurements != null) && (handle >= 0) && (handle < runtimeMeasurements.size())) {
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
    private long timeStarted;
    private long timeStopped;
    private String className;
    private String methodName;

    @Override
    public long getTimeStarted() {
      return timeStarted;
    }

    @Override
    public void setTimeStarted(final long time_start) {
      timeStarted = time_start;
    }

    @Override
    public long getTimeStopped() {
      return timeStopped;
    }

    @Override
    public void setTimeStopped(final long time_stop) {
      timeStopped = time_stop;
    }

    @Override
    public String getClassName() {
      return className;
    }

    @Override
    public void setClassName(final String className) {
      this.className = className;
    }

    @Override
    public String getMethodName() {
      return methodName;
    }

    @Override
    public void setMethodName(final String methodName) {
      this.methodName = methodName;
    }

    @Override
    public String toString() {
      return className + "." + methodName + ": " + (timeStopped - timeStarted);
    }
  }

  @Override
  public String getHttpRequestHeader(final String name) {
    ODataRequest request = (ODataRequest) parameterTable.get(ODATA_REQUEST);
    for (final String headerName : request.getHeaders().keySet()) {
      if (headerName.equalsIgnoreCase(name)) {
        return request.getHeaders().get(headerName);
      }
    }
    return null;
  }

  @Override
  public Map<String, String> getHttpRequestHeaders() {
    ODataRequest request = (ODataRequest) parameterTable.get(ODATA_REQUEST);
    return request.getHeaders();
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
}
