/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.api.processor;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.PathInfo;

/**
 * Compilation of generic context objects. 
  *
 * @author SAP AG
 * @com.sap.core.odata.DoNotImplement
 */
public interface ODataContext {

  /**
   * @return ODataService related for this context
   * @throws ODataException
   */
  ODataService getService() throws ODataException;

  /**
   * @return a OData path info object
   * @throws ODataException
   */
  PathInfo getPathInfo() throws ODataException;

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
   * Returns header value of HTTP request
   * @param name name of a request header element (e.g. "Content-Type")
   * @return null or a request header value if found
   */
  String getHttpRequestHeader(String name);

  /**
   * Returns all header values of HTTP request
   * @return immutable map of request header values
   */
  Map<String, String> getHttpRequestHeaders();

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

  /**
   * Get a list of languages that are acceptable for the response.
   * If no acceptable languages are specified, a read-only list containing 
   * a single wildcard java.util.Locale instance (with language field set to "*") is returned.
   * 
   * @return a read-only list of acceptable languages sorted according to their q-value, with highest preference first.
   */
  List<Locale> getAcceptableLanguages();

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
