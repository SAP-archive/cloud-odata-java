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
package com.sap.core.odata.processor.api.jpa.factory;

import java.util.Locale;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAMessageService;

/**
 * Factory interface for creating following instances
 * 
 * <p>
 * <ul>
 * <li>OData JPA Processor of type
 * {@link com.sap.core.odata.api.processor.ODataSingleProcessor}</li>
 * <li>JPA EDM Provider of type
 * {@link com.sap.core.odata.api.edm.provider.EdmProvider}</li>
 * <li>OData JPA Context
 * {@link com.sap.core.odata.processor.api.jpa.ODataJPAContext}</li>
 * </ul>
 * </p>
 * 
 * @author SAP AG
 * @see com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory
 */
public interface ODataJPAAccessFactory {
  /**
   * The method creates an OData JPA Processor. The processor handles runtime
   * behavior of an OData service.
   * 
   * @param oDataJPAContext
   *            an instance of type
   *            {@link com.sap.core.odata.processor.api.jpa.ODataJPAContext}.
   *            The context should be initialized properly and cannot be null.
   * @return An implementation of OData JPA Processor.
   */
  public ODataSingleProcessor createODataProcessor(
      ODataJPAContext oDataJPAContext);

  /**
   * 
   * @param oDataJPAContext
   *            an instance of type
   *            {@link com.sap.core.odata.processor.api.jpa.ODataJPAContext}.
   *            The context should be initialized properly and cannot be null.
   * @return An implementation of JPA EdmProvider. EdmProvider handles
   *         meta-data.
   */
  public EdmProvider createJPAEdmProvider(ODataJPAContext oDataJPAContext);

  /**
   * The method creates an instance of OData JPA Context. An empty instance is
   * returned.
   * 
   * @return an instance of type
   *         {@link com.sap.core.odata.processor.api.jpa.ODataJPAContext}
   */
  public ODataJPAContext createODataJPAContext();

  /**
   * The method creates an instance of message service for loading language
   * dependent message text.
   * 
   * @param locale
   *            is the language in which the message service should load
   *            message texts.
   * @return an instance of type
   *         {@link com.sap.core.odata.processor.api.jpa.exception.ODataJPAMessageService}
   */
  public ODataJPAMessageService getODataJPAMessageService(Locale locale);
}
