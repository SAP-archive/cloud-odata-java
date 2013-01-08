package com.sap.core.odata.api.processor;

import java.util.List;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.PathInfo;

/**
 * Compilation of generic context objects. 
  *
 * @author SAP AG
 * @DoNotImplement
 */
public interface ODataContext {

  /**
   * @return ODataService related for this context
   * @throws ODataException
   */
  ODataService getService() throws ODataException;

  /**
   * @return a OData URI info object
   * @throws ODataException
   */
  PathInfo getUriInfo() throws ODataException;

  /**
   * Start runtime measurement
   * 
   * @param className where the runtime measurement starts
   * @param methodName where the runtime measurement starts
   * @return handle for the started runtime measurement which can be used for stopping
   */
  int startRuntimeMeasurement(String className, String methodName);

  /**
   * Stop runtime measurement
   * 
   * @param handle of runtime measurement to be stopped
   */
  void stopRuntimeMeasurement(int handle);

  /**
   * Get the list of all runtime measurements
   * 
   * @return list of all runtime measurements of type {@link RuntimeMeasurement}
   */
  List<RuntimeMeasurement> getRuntimeMeasurements();

  /**
   * Set parameter
   * 
   * @param name of parameter (name is used as key, existing values are overwritten)
   * @param value of parameter as object
   */
  void setParameter(String name, Object value);

  /**
   * Remove parameter
   * 
   * @param name of parameter to be removed
   */
  void removeParameter(String name);

  /**
   * Get the parameter
   * 
   * @param name of parameter
   * @return parameter value as {@link Object} for the given name
   */
  Object getParameter(String name);

  /**
   * Get information about enabled debug mode
   * 
   * @return debugMode as boolean
   */
  boolean isInDebugMode();

  /**
   * Enable debug mode
   * 
   * @param debugMode as boolean
   */
  void setDebugMode(boolean debugMode);

  public interface RuntimeMeasurement {
    void setMethodName(String methodName);
    String getMethodName();

    void setClassName(String className);
    String getClassName();

    void setTimeStopped(long timeStopped);
    long getTimeStopped();

    void setTimeStarted(long timeStarted);
    long getTimeStarted();
  }
}