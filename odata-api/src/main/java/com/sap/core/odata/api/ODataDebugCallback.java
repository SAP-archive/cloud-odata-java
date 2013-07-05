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
package com.sap.core.odata.api;

/**
 * @author SAP AG
 *
 */
public interface ODataDebugCallback extends ODataCallback {

  /**
   * Determines whether additional debug information can be retrieved
   * from this OData service for the current request.
   * @return <code>true</code> if the system is in debug mode
   *         and the current user has the rights to debug the OData service
   */
  boolean isDebugEnabled();

}
