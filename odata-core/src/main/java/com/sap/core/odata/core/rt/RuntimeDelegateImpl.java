package com.sap.core.odata.core.rt;

import java.io.InputStream;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.batch.BatchResponsePart.BatchResponsePartBuilder;
import com.sap.core.odata.api.client.batch.BatchChangeSet.BatchChangeSetBuilder;
import com.sap.core.odata.api.client.batch.BatchChangeSetPart.BatchChangeSetPartBuilder;
import com.sap.core.odata.api.client.batch.BatchQueryPart.BatchQueryPartBuilder;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.ep.EntityProvider.EntityProviderInterface;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.processor.ODataRequest.ODataRequestBuilder;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.rt.RuntimeDelegate.RuntimeDelegateInstance;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.core.ODataRequestImpl;
import com.sap.core.odata.core.ODataResponseImpl;
import com.sap.core.odata.core.batch.BatchChangeSetImpl;
import com.sap.core.odata.core.batch.BatchChangeSetPartImpl;
import com.sap.core.odata.core.batch.BatchQueryPartImpl;
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

  @Override
  protected ODataRequestBuilder createODataRequestBuilder() {
    ODataRequestImpl request = new ODataRequestImpl();
    return request.new ODataRequestBuilderImpl();
  }

  @Override
  protected BatchChangeSetBuilder createBatchChangeSetBuilder() {
    BatchChangeSetImpl changeSet = new BatchChangeSetImpl();
    return changeSet.new BatchChangeSetBuilderImpl();
  }

  @Override
  protected BatchQueryPartBuilder createBatchQueryRequestBuilder() {
    BatchQueryPartImpl batchQueryRequest = new BatchQueryPartImpl();
    return batchQueryRequest.new BatchQueryRequestBuilderImpl();
  }

  @Override
  protected BatchChangeSetPartBuilder createBatchChangeSetRequest() {
    BatchChangeSetPartImpl batchChangeSetRequest = new BatchChangeSetPartImpl();
    ;
    return batchChangeSetRequest.new BatchChangeSetRequestBuilderImpl();
  }

}
