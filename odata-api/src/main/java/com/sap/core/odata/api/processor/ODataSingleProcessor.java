package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.enums.HttpStatusCodes;
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
import com.sap.core.odata.api.uri.resultviews.DeleteResultView;
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

  private static final String PARAMETER_CHARSET = "charset";
  private static final String DEFAULT_SERVICE_CHARSET = "utf-8";
  private static final String DEFAULT_SERVICE_CHARSET_PARAMETER = PARAMETER_CHARSET + "=" + DEFAULT_SERVICE_CHARSET;
  
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
  public ODataResponse executeBatch(String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see FunctionImport
   */
  @Override
  public ODataResponse executeFunctionImport(GetFunctionImportView uriParserResultView,String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see FunctionImportValue
   */
  @Override
  public ODataResponse executeFunctionImportValue(GetFunctionImportView uriParserResultView,String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValue
   */
  @Override
  public ODataResponse readEntitySimplePropertyValue(GetSimplePropertyView uriParserResultView,String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValue
   */
  @Override
  public ODataResponse updateEntitySimplePropertyValue(String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValue
   */
  @Override
  public ODataResponse deleteEntitySimplePropertyValue(String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimpleProperty
   */
  @Override
  public ODataResponse readEntitySimpleProperty(GetSimplePropertyView uriParserResultView, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimpleProperty
   */
  @Override
  public ODataResponse updateEntitySimpleProperty(String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMedia
   */
  @Override
  public ODataResponse readEntityMedia(GetMediaResourceView uriParserResultView, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMedia
   */
  @Override
  public ODataResponse updateEntityMedia(String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMedia
   */
  @Override
  public ODataResponse deleteEntityMedia(String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinks
   */
  @Override
  public ODataResponse readEntityLinks(GetEntitySetLinksView uriParserResultView, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinks
   */
  @Override
  public ODataResponse countEntityLinks(GetEntitySetLinksCountView uriParserResultView, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse createEntityLink(String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse readEntityLink(GetEntityLinkView uriParserResultView, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse existsEntityLink(GetEntityLinkCountView uriParserResultView, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse updateEntityLink(String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse deleteEntityLink(DeleteResultView uriParserResultView, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityComplexProperty
   */
  @Override
  public ODataResponse readEntityComplexProperty(GetComplexPropertyView uriParserResultView, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityComplexProperty
   */
  @Override
  public ODataResponse updateEntityComplexProperty(String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySet
   */
  @Override
  public ODataResponse readEntitySet(GetEntitySetView uriParserResultView, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySet
   */
  @Override
  public ODataResponse countEntitySet(GetEntitySetCountView uriParserResultView, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse createEntity(String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse readEntity(GetEntityView uriParserResultView, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse existsEntity(GetEntityCountView uriParserResultView, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse updateEntity(String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse deleteEntity(DeleteResultView uriParserResultView, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see ServiceDocument
   */
  @Override
  public ODataResponse readServiceDocument(GetServiceDocumentView uriParserResultView, String contentType) throws ODataException {
    ODataEntityProvider odataSerializer = ODataEntityProvider.create(contentType);
    contentType += "; " + DEFAULT_SERVICE_CHARSET_PARAMETER;

    ODataResponse response = ODataResponse.fromResponse(odataSerializer.writeServiceDocument(getContext().getService().getEntityDataModel(), getContext().getUriInfo().getBaseUri().toASCIIString()))
      .header("Content-Type", contentType)
      .header("DataServiceVersion", Edm.DATA_SERVICE_VERSION_10).build();
    return response;
  }

  /**
   * @see Metadata
   */
  @Override
  public ODataResponse readMetadata(GetMetadataView uriParserResultView, String contentType) throws ODataException {
    EdmServiceMetadata edmServiceMetadata = getContext().getService().getEntityDataModel().getServiceMetadata();

    return ODataResponse
        .status(HttpStatusCodes.OK)
        .header("Content-Type", contentType.toString())
        .header("DataServiceVersion", edmServiceMetadata.getDataServiceVersion())
        .entity(edmServiceMetadata.getMetadata())
        .build();
  }

}
