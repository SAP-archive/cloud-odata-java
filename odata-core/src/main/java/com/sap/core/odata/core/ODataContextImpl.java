package com.sap.core.odata.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataUriInfo;
import com.sap.core.odata.api.service.ODataService;

public class ODataContextImpl implements ODataContext {

  private static String DEBUG_MODE = "~debugMode";
  private static String SERVICE = "~service";
  private static String URI_INFO = "~uriInfo";
  private static String RUNTIME_MEASUREMENTS = "~runtimeMeasurements";

  private Map<String, Object> parameterTable = new HashMap<String, Object>();

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

  public void setUriInfo(ODataUriInfo uriInfo) {
    setParameter(URI_INFO, uriInfo);
  }

  @Override
  public ODataUriInfo getUriInfo() throws ODataException {
    return (ODataUriInfo) getParameter(URI_INFO);
  }

  @SuppressWarnings("unchecked")
  @Override
  public int startRuntimeMeasurement(String className, String methodName) {
    List<RuntimeMeasurement> runtimeMeasurements;
    
    if (getParameter(RUNTIME_MEASUREMENTS) == null) {
      runtimeMeasurements = new ArrayList<RuntimeMeasurement>();
    } else {
      runtimeMeasurements = (List<RuntimeMeasurement>) getParameter(RUNTIME_MEASUREMENTS);
    }

    if (isInDebugMode()) {
      RuntimeMeasurement measurement = new RuntimeMeasurementImpl();
      measurement.setTimeStarted(System.nanoTime());
      measurement.setClassName(className);
      measurement.setMethodName(methodName);

      runtimeMeasurements.add(measurement);
    }

    return runtimeMeasurements.size() - 1;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void stopRuntimeMeasurement(int handle) {
    if (isInDebugMode()) {
      try {
        RuntimeMeasurement runtimeMeasurement = ((List<RuntimeMeasurement>)getParameter(RUNTIME_MEASUREMENTS)).get(handle);
        runtimeMeasurement.setTimeStopped(System.nanoTime());
      } catch (NullPointerException e) {
        //nothing to handle
      } catch (IndexOutOfBoundsException e) {
        // nothing to handle
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
  }

}
