package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.aspect.Batch;
import com.sap.core.odata.api.processor.aspect.Entity;
import com.sap.core.odata.api.processor.aspect.EntityComplexProperty;
import com.sap.core.odata.api.processor.aspect.EntityLink;
import com.sap.core.odata.api.processor.aspect.EntityLinks;
import com.sap.core.odata.api.processor.aspect.EntityMedia;
import com.sap.core.odata.api.processor.aspect.EntitySet;
import com.sap.core.odata.api.processor.aspect.EntitySimpleProperty;
import com.sap.core.odata.api.processor.aspect.EntitySimplePropertyValue;
import com.sap.core.odata.api.processor.aspect.FunctionImport;
import com.sap.core.odata.api.processor.aspect.FunctionImportValue;
import com.sap.core.odata.api.processor.aspect.Metadata;
import com.sap.core.odata.api.processor.aspect.ServiceDocument;
import com.sap.core.odata.api.uri.resultviews.GetComplexPropertyView;
import com.sap.core.odata.api.uri.resultviews.GetEntityCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntityLinkCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntityLinkView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetLinksCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetLinksView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.api.uri.resultviews.GetEntityView;
import com.sap.core.odata.api.uri.resultviews.GetFunctionImportView;
import com.sap.core.odata.api.uri.resultviews.GetMediaResourceView;
import com.sap.core.odata.api.uri.resultviews.GetMetadataView;
import com.sap.core.odata.api.uri.resultviews.GetServiceDocumentView;
import com.sap.core.odata.api.uri.resultviews.GetSimplePropertyView;


public abstract class ODataSingleProcessor
    implements ODataProcessor,
    Metadata,
    ServiceDocument,
    Entity,
    EntitySet,
    EntityComplexProperty,
    EntityLink,
    EntityLinks,
    EntityMedia,
    EntitySimpleProperty,
    EntitySimplePropertyValue,
    FunctionImport,
    FunctionImportValue,
    Batch
{

  private ODataContext context;
  
  @Override
  public void setContext(ODataContext context) {
    this.context = context;
  }

  @Override
  public ODataContext getContext() {
    return this.context;
  }

  @Override
  public ODataResponse executeBatch() throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse executeFunctionImport(GetFunctionImportView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse executeFunctionImportValue(GetFunctionImportView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntitySimplePropertyValue(GetSimplePropertyView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse updateEntitySimplePropertyValue() throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse deleteEntitySimplePropertyValue() throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntitySimpleProperty(GetSimplePropertyView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse updateEntitySimpleProperty() throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntityMedia(GetMediaResourceView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse updateEntityMedia() throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse deleteEntityMedia() throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntityLinks(GetEntitySetLinksView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse countEntityLinks(GetEntitySetLinksCountView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse createEntityLink() throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntityLink(GetEntityLinkView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse existsEntityLink(GetEntityLinkCountView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse updateEntityLink() throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse deleteEntityLink() throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntityComplexProperty(GetComplexPropertyView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse updateEntityComplexProperty() throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntitySet(GetEntitySetView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse countEntitySet(GetEntitySetCountView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse createEntity() throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntity(GetEntityView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse existsEntity(GetEntityCountView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse updateEntity() throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse deleteEntity() throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readServiceDocument(GetServiceDocumentView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readMetadata(GetMetadataView uriParserResultView) throws ODataException {
    throw new ODataNotImplementedException();
  }

}
