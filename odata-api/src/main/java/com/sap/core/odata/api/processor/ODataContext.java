package com.sap.core.odata.api.processor;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.PathInfo;

/**
 * Compilation of generic context objects.
 * @author SAP AG
 * @com.sap.core.odata.DoNotImplement
 */
public interface ODataContext {

  /**
   * Gets the OData service.
   * @return ODataService related for this context
   * @throws ODataException
   */
  ODataService getService() throws ODataException;

  /**
   * @return the service factory instance
   */
  ODataServiceFactory getServiceFactory();

  /**
   * Gets information about the request path.
   * @return an OData path info object
   * @throws ODataException
   */
  PathInfo getPathInfo() throws ODataException;

  /**
   * Starts runtime measurement.
   * @param className class name where the runtime measurement starts
   * @param methodName method name where the runtime measurement starts
   * @return handle for the started runtime measurement which can be used for stopping
   */
  int startRuntimeMeasurement(String className, String methodName);

  /**
   * Stops runtime measurement.
   * @param handle of runtime measurement to be stopped
   */
  void stopRuntimeMeasurement(int handle);

  /**
   * Gets the list of all runtime measurements.
   * @return list of all runtime measurements of type {@link RuntimeMeasurement}
   */
  List<RuntimeMeasurement> getRuntimeMeasurements();

  /**
   * Gets the HTTP method of the request.
   * @return HTTP method as {@link String}
   */
  String getHttpMethod();

  /**
   * Sets a parameter.
   * @param name of parameter (name is used as key, existing values are overwritten)
   * @param value of parameter as object
   */
  void setParameter(String name, Object value);

  /**
   * Removes parameter.
   * @param name of parameter to be removed
   */
  void removeParameter(String name);

  /**
   * Gets a named parameter value.
   * @param name of parameter
   * @return parameter value as {@link Object} for the given name
   */
  Object getParameter(String name);

  /**
   * Returns a header value of the HTTP request.
   * @param name name of a request header element (e.g. "Content-Type")
   * @return null or a request header value if found
   */
  @Deprecated
  String getHttpRequestHeader(String name);

  /**
   * Returns the first found header value of the HTTP request.
   * @param name name of the first found request header element (e.g. "Content-Type")
   * @return null or a request header value if found
   */
  String getRequestHeader(String name);

  /**
   * Returns all header values of the HTTP request.
   * @return immutable map of request header values
   */
  @Deprecated
  Map<String, String> getHttpRequestHeaders();

  /**
   * Returns all header values of the HTTP request but never null.
   * @return immutable map of request header values
   */
  Map<String, List<String>> getRequestHeaders();

  /**
   * Gets information about enabled debug mode.
   * @return debugMode as boolean
   */
  boolean isInDebugMode();

  /**
   * Enables debug mode.
   * @param debugMode as boolean
   */
  void setDebugMode(boolean debugMode);

  /**
   * Gets a list of languages that are acceptable for the response.
   * If no acceptable languages are specified, a read-only list containing 
   * a single wildcard java.util.Locale instance (with language field set to "*") is returned.
   * @return a read-only list of acceptable languages sorted according to their q-value,
   *         with highest preference first.
   */
  List<Locale> getAcceptableLanguages();

  /**
   * <p>Runtime measurements.</p>
   * <p>All times are in nanoseconds since some fixed but arbitrary time
   * (perhaps in the future, so values may be negative).</p>
   * @see System#nanoTime()
   */
  public interface RuntimeMeasurement {
    /**
     * Sets the class name.
     * @param className the name of the class that is measured
     */
    void setClassName(String className);

    /**
     * Gets the class name.
     * @return the name of the class that is measured
     */
    String getClassName();

    /**
     * Sets the method name.
     * @param methodName the name of the method that is measured
     */
    void setMethodName(String methodName);

    /**
     * Gets the method name.
     * @return the name of the method that is measured
     */
    String getMethodName();

    /**
     * Sets the start time.
     * @param timeStarted the start time in nanoseconds
     * @see System#nanoTime()
     */
    void setTimeStarted(long timeStarted);

    /**
     * Gets the start time.
     * @return the start time in nanoseconds or 0 if not set yet
     * @see System#nanoTime()
     */
    long getTimeStarted();

    /**
     * Sets the stop time.
     * @param timeStopped the stop time in nanoseconds
     * @see System#nanoTime()
     */
    void setTimeStopped(long timeStopped);

    /**
     * Gets the stop time.
     * @return the stop time in nanoseconds or 0 if not set yet
     * @see System#nanoTime()
     */
    long getTimeStopped();
  }
}
