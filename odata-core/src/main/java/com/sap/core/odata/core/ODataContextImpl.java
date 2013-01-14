package com.sap.core.odata.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.uri.PathInfo;

/**
 * @author SAP AG
 */
public class ODataContextImpl implements ODataContext {

  private static final String DEBUG_MODE = "~debugMode";
  private static final String SERVICE = "~service";
  private static final String PATH_INFO = "~pathInfo";
  private static final String RUNTIME_MEASUREMENTS = "~runtimeMeasurements";

  private Map<String, Object> parameterTable = new HashMap<String, Object>();
  private Map<String, String> requestHeader = new HashMap<String, String>();

  @Override
  public void setParameter(String name, Object value) {
    parameterTable.put(name, value);
  }

  @Override
  public void removeParameter(String name) {
    parameterTable.remove(name);
  }

  @Override
  public Object getParameter(String name) {
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
  public void setDebugMode(boolean debugMode) {
    setParameter(DEBUG_MODE, debugMode);
  }

  public void setService(ODataService service) {
    setParameter(SERVICE, service);
  }

  @Override
  public ODataService getService() throws ODataException {
    return (ODataService) getParameter(SERVICE);
  }

  public void setUriInfo(PathInfo uriInfo) {
    setParameter(PATH_INFO, uriInfo);
  }

  @Override
  public PathInfo getPathInfo() throws ODataException {
    return (PathInfo) getParameter(PATH_INFO);
  }

  @Override
  public int startRuntimeMeasurement(String className, String methodName) {
    if (isInDebugMode()) {
      RuntimeMeasurement measurement = new RuntimeMeasurementImpl();
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
  public void stopRuntimeMeasurement(int handle) {
    if (isInDebugMode()) {
      @SuppressWarnings("unchecked")
      List<RuntimeMeasurement> runtimeMeasurements = (List<RuntimeMeasurement>) getParameter(RUNTIME_MEASUREMENTS);

      if (runtimeMeasurements != null && handle >= 0 && handle < runtimeMeasurements.size())
        runtimeMeasurements.get(handle).setTimeStopped(System.nanoTime());
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
    public void setTimeStarted(long time_start) {
      this.timeStarted = time_start;
    }

    @Override
    public long getTimeStopped() {
      return timeStopped;
    }

    @Override
    public void setTimeStopped(long time_stop) {
      this.timeStopped = time_stop;
    }

    @Override
    public String getClassName() {
      return className;
    }

    @Override
    public void setClassName(String className) {
      this.className = className;
    }

    @Override
    public String getMethodName() {
      return methodName;
    }

    @Override
    public void setMethodName(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public String toString() {
      return className + "." + methodName + ": " + (timeStopped - timeStarted);
    }
  }

  public void setHttpRequestHeader(String name, String value) {
    requestHeader.put(name, value);
  }

  @Override
  public String getHttpRequestHeader(String name) {
    return requestHeader.get(name);
  }

  @Override
  public Map<String, String> getHttpRequestHeaders() {
    return Collections.unmodifiableMap(requestHeader);
  }

}
