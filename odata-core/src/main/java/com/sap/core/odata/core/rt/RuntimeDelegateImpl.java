package com.sap.core.odata.core.rt;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.ep.EntityProvider.EntityProviderInterface;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.processor.feature.Batch;
import com.sap.core.odata.api.processor.feature.Entity;
import com.sap.core.odata.api.processor.feature.EntityComplexProperty;
import com.sap.core.odata.api.processor.feature.EntityLink;
import com.sap.core.odata.api.processor.feature.EntityLinks;
import com.sap.core.odata.api.processor.feature.EntityMedia;
import com.sap.core.odata.api.processor.feature.EntitySet;
import com.sap.core.odata.api.processor.feature.EntitySimpleProperty;
import com.sap.core.odata.api.processor.feature.EntitySimplePropertyValue;
import com.sap.core.odata.api.processor.feature.FunctionImport;
import com.sap.core.odata.api.processor.feature.FunctionImportValue;
import com.sap.core.odata.api.processor.feature.Metadata;
import com.sap.core.odata.api.processor.feature.ProcessorFeature;
import com.sap.core.odata.api.processor.feature.ServiceDocument;
import com.sap.core.odata.api.rt.RuntimeDelegate.RuntimeDelegateInstance;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.api.uri.expression.FilterParser;
import com.sap.core.odata.api.uri.expression.OrderByParser;
import com.sap.core.odata.core.ODataResponseImpl;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;
import com.sap.core.odata.core.edm.provider.EdmImplProv;
import com.sap.core.odata.core.ep.ProviderFacadeImpl;
import com.sap.core.odata.core.uri.UriParserImpl;
import com.sap.core.odata.core.uri.expression.FilterParserImpl;
import com.sap.core.odata.core.uri.expression.OrderByParserImpl;

/**
 * @author SAP AG
 */
public class RuntimeDelegateImpl extends RuntimeDelegateInstance {

  @Override
  protected ODataResponseBuilder createODataResponseBuilder() {
    ODataResponseImpl r = new ODataResponseImpl();
    return r.new ODataResponseBuilderImpl();
  }

  protected EdmSimpleType getEdmSimpleType(EdmSimpleTypeKind edmSimpleType) {
    return EdmSimpleTypeFacadeImpl.getEdmSimpleType(edmSimpleType);
  }

  @Override
  protected UriParser getUriParser(Edm edm) {
    return new UriParserImpl(edm);
  }

  @Override
  protected EdmSimpleType getInternalEdmSimpleTypeByString(String edmSimpleType) {
    return EdmSimpleTypeFacadeImpl.getInternalEdmSimpleTypeByString(edmSimpleType);
  }

  @Override
  protected EdmSimpleTypeFacade getSimpleTypeFacade() {
    return new EdmSimpleTypeFacadeImpl();
  }

  @Override
  protected Edm createEdm(EdmProvider provider) {
    return new EdmImplProv(provider);
  }


  @Override
  protected EntityProviderInterface createEntityProvider() {
    return new ProviderFacadeImpl();
  }

  @Override
  protected FilterParser getFilterParser(Edm edm, EdmEntityType edmType) {
    return new FilterParserImpl(edm, edmType);
  }

  @Override
  protected OrderByParser getOrderByParser(Edm edm, EdmEntityType edmType) {
    return new OrderByParserImpl(edm, edmType);
  }
  
  @Override
  protected List<String> getSupportedContentTypes(List<String> customContentTypes, Class<? extends ProcessorFeature> processorFeature) throws ODataException {
    List<String> result = new ArrayList<String>();

    result.addAll(customContentTypes);

    if (processorFeature == Batch.class) {
      result.add(HttpContentType.MULTIPART_MIXED);
    } else if (processorFeature == Entity.class) {
      result.add(HttpContentType.APPLICATION_ATOM_XML_ENTRY);
      result.add(HttpContentType.APPLICATION_ATOM_XML);
      result.add(HttpContentType.APPLICATION_JSON);
      result.add(HttpContentType.APPLICATION_XML);
    } else if (processorFeature == FunctionImport.class
        || processorFeature == EntityLink.class
        || processorFeature == EntityLinks.class
        || processorFeature == EntitySimpleProperty.class
        || processorFeature == EntityComplexProperty.class) {
      result.add(HttpContentType.APPLICATION_XML);
      result.add(HttpContentType.APPLICATION_JSON);
    } else if (processorFeature == EntityMedia.class
        || processorFeature == EntitySimplePropertyValue.class
        || processorFeature == FunctionImportValue.class) {
      result.add(HttpContentType.WILDCARD);
    } else if (processorFeature == EntitySet.class) {
      result.add(HttpContentType.APPLICATION_ATOM_XML_FEED);
      result.add(HttpContentType.APPLICATION_ATOM_XML);
      result.add(HttpContentType.APPLICATION_JSON);
      result.add(HttpContentType.APPLICATION_XML);
    } else if (processorFeature == Metadata.class) {
      result.add(HttpContentType.APPLICATION_XML);
    } else if (processorFeature == ServiceDocument.class) {
      result.add(HttpContentType.APPLICATION_ATOM_SVC);
      result.add(HttpContentType.APPLICATION_JSON);
      result.add(HttpContentType.APPLICATION_XML);
    } else {
      throw new ODataNotImplementedException();
    }

    return result;
  }
}
