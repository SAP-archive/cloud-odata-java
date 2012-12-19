package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.ep.ODataEntityContent;
import com.sap.core.odata.api.ep.ODataEntityProvider;
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

/**
 * <p>A default {@link ODataProcessor} that implements all processor aspects in a single class.</p>
 * <p>It is recommended to derive from this class and it is required by the
 * {@link com.sap.core.odata.api.service.ODataServiceFactory} to build an {@link com.sap.core.odata.api.service.ODataService}.</p>
 * <p>This abstract class provides a default behavior, returning the correct response
 * for requests for the service or the metadata document, respectively, and throwing an
 * {@link ODataNotImplementedException} for all other requests.
 * Sub classes have to override only methods they want to support.</p> 
 * 
 * @author SAP AG
 */
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

  /**
   * A request context object usually injected by the OData library.
   */
  private ODataContext context;

  /**
   * @see ODataProcessor
   */
  @Override
  public void setContext(ODataContext context) {
    this.context = context;
  }

  /**
   * @see ODataProcessor
   */
  @Override
  public ODataContext getContext() {
    return this.context;
  }

  /**
   * @see Batch
   */
  @Override
  public ODataResponse executeBatch(ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see FunctionImport
   */
  @Override
  public ODataResponse executeFunctionImport(GetFunctionImportView uriParserResultView,ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see FunctionImportValue
   */
  @Override
  public ODataResponse executeFunctionImportValue(GetFunctionImportView uriParserResultView,ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValue
   */
  @Override
  public ODataResponse readEntitySimplePropertyValue(GetSimplePropertyView uriParserResultView,ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValue
   */
  @Override
  public ODataResponse updateEntitySimplePropertyValue(ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValue
   */
  @Override
  public ODataResponse deleteEntitySimplePropertyValue(ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimpleProperty
   */
  @Override
  public ODataResponse readEntitySimpleProperty(GetSimplePropertyView uriParserResultView, ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimpleProperty
   */
  @Override
  public ODataResponse updateEntitySimpleProperty(ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMedia
   */
  @Override
  public ODataResponse readEntityMedia(GetMediaResourceView uriParserResultView, ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMedia
   */
  @Override
  public ODataResponse updateEntityMedia(ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMedia
   */
  @Override
  public ODataResponse deleteEntityMedia(ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinks
   */
  @Override
  public ODataResponse readEntityLinks(GetEntitySetLinksView uriParserResultView, ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinks
   */
  @Override
  public ODataResponse countEntityLinks(GetEntitySetLinksCountView uriParserResultView, ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse createEntityLink(ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse readEntityLink(GetEntityLinkView uriParserResultView, ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse existsEntityLink(GetEntityLinkCountView uriParserResultView, ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse updateEntityLink(ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse deleteEntityLink(ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityComplexProperty
   */
  @Override
  public ODataResponse readEntityComplexProperty(GetComplexPropertyView uriParserResultView, ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityComplexProperty
   */
  @Override
  public ODataResponse updateEntityComplexProperty(ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySet
   */
  @Override
  public ODataResponse readEntitySet(GetEntitySetView uriParserResultView, ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySet
   */
  @Override
  public ODataResponse countEntitySet(GetEntitySetCountView uriParserResultView, ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse createEntity(ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse readEntity(GetEntityView uriParserResultView, ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse existsEntity(GetEntityCountView uriParserResultView, ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse updateEntity(ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse deleteEntity(ContentType contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see ServiceDocument
   */
  @Override
  public ODataResponse readServiceDocument(GetServiceDocumentView uriParserResultView, ContentType contentType) throws ODataException {
    ODataEntityProvider odataSerializer = ODataEntityProvider.create(contentType);
    ODataEntityContent serviceDocument = odataSerializer.writeServiceDocument(getContext().getService().getEntityDataModel(), getContext().getUriInfo().getBaseUri().toASCIIString());
    return ODataResponse
        .header("DataServiceVersion", Edm.DATA_SERVICE_VERSION_10)
        .entity(serviceDocument)
        .build();
  }

  /**
   * @see Metadata
   */
  @Override
  public ODataResponse readMetadata(GetMetadataView uriParserResultView, ContentType contentType) throws ODataException {
    EdmServiceMetadata edmServiceMetadata = getContext().getService().getEntityDataModel().getServiceMetadata();
    return ODataResponse
        .status(HttpStatusCodes.OK)
        .header("Content-Type", contentType.toString())
        .header("DataServiceVersion", edmServiceMetadata.getDataServiceVersion())
        .entity(edmServiceMetadata.getMetadata())
        .build();
  }

}
