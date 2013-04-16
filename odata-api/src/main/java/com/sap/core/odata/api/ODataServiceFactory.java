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

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * Creates instance of custom OData service.
 * 
 * @author SAP AG
 */
public abstract class ODataServiceFactory {

  /**
   * Label used in web.xml to assign servlet init parameter to factory class instance.
   */
  public static final String FACTORY_LABEL = "com.sap.core.odata.service.factory";

  /**
   * Label used in core to access application class loader 
   */
  public static final String FACTORY_CLASSLOADER_LABEL = "com.sap.core.odata.service.factory.classloader";

  /**
   * Label used in web.xml to assign servlet init parameter for a path split (service resolution).
   */
  public static final String PATH_SPLIT_LABEL = "com.sap.core.odata.path.split";

  /**
   * Create instance of custom {@link ODataService}.
   * @param ctx OData context object
   * @return A new service instance.
   * @throws ODataException in case of error
   */
  public abstract ODataService createService(ODataContext ctx) throws ODataException;

  /**
   * Create a default service instance based on </code>ODataSingleProcessor<code>.
   * @param provider A custom <code>EdmProvider</code> implementation.
   * @param processor A custom processor implementation derived from <code>ODataSingleProcessor</code> .
   * @return A new default <code>ODataSingleProcessorService</code> instance.
   */
  public ODataService createODataSingleProcessorService(final EdmProvider provider, final ODataSingleProcessor processor) {
    return RuntimeDelegate.createODataSingleProcessorService(provider, processor);
  }

  /**
   * A service can return implementation classes for various callback interfaces.
   * @param callbackInterface a interface type to query for implementation
   * @return a callback implementation for this interface or null
   */
  public <T extends ODataCallback> T getCallback(final Class<? extends ODataCallback> callbackInterface) {
    return null;
  }

}
