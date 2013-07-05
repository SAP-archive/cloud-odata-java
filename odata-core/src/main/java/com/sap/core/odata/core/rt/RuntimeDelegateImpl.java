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
package com.sap.core.odata.core.rt;

import java.io.InputStream;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.batch.BatchResponsePart.BatchResponsePartBuilder;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.ep.EntityProvider.EntityProviderInterface;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.rt.RuntimeDelegate.RuntimeDelegateInstance;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.core.ODataResponseImpl;
import com.sap.core.odata.core.batch.BatchResponsePartImpl;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;
import com.sap.core.odata.core.edm.parser.EdmxProvider;
import com.sap.core.odata.core.edm.provider.EdmImplProv;
import com.sap.core.odata.core.ep.ProviderFacadeImpl;
import com.sap.core.odata.core.processor.ODataSingleProcessorService;
import com.sap.core.odata.core.uri.UriParserImpl;

/**
 * @author SAP AG
 */
public class RuntimeDelegateImpl extends RuntimeDelegateInstance {

  @Override
  protected ODataResponseBuilder createODataResponseBuilder() {
    ODataResponseImpl r = new ODataResponseImpl();
    return r.new ODataResponseBuilderImpl();
  }

  @Override
  protected EdmSimpleType getEdmSimpleType(final EdmSimpleTypeKind edmSimpleType) {
    return EdmSimpleTypeFacadeImpl.getEdmSimpleType(edmSimpleType);
  }

  @Override
  protected UriParser getUriParser(final Edm edm) {
    return new UriParserImpl(edm);
  }

  @Override
  protected EdmSimpleTypeFacade getSimpleTypeFacade() {
    return new EdmSimpleTypeFacadeImpl();
  }

  @Override
  protected Edm createEdm(final EdmProvider provider) {
    return new EdmImplProv(provider);
  }

  @Override
  protected EntityProviderInterface createEntityProvider() {
    return new ProviderFacadeImpl();
  }

  @Override
  protected ODataService createODataSingleProcessorService(final EdmProvider provider, final ODataSingleProcessor processor) {
    return new ODataSingleProcessorService(provider, processor);
  }

  @Override
  protected EdmProvider createEdmProvider(final InputStream metadataXml, final boolean validate) throws EntityProviderException {
    return new EdmxProvider().parse(metadataXml, validate);
  }

  @Override
  protected BatchResponsePartBuilder createBatchResponsePartBuilder() {
    BatchResponsePartImpl part = new BatchResponsePartImpl();
    return part.new BatchResponsePartBuilderImpl();
  }

}
