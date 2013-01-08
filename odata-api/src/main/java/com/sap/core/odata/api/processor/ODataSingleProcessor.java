package com.sap.core.odata.api.processor;

import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.feature.Batch;
import com.sap.core.odata.api.processor.feature.CustomContentType;
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
import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetComplexPropertyUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityLinkCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityLinkUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetLinksCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetLinksUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.GetFunctionImportUriInfo;
import com.sap.core.odata.api.uri.info.GetMediaResourceUriInfo;
import com.sap.core.odata.api.uri.info.GetMetadataUriInfo;
import com.sap.core.odata.api.uri.info.GetServiceDocumentUriInfo;
import com.sap.core.odata.api.uri.info.GetSimplePropertyUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;

/**
 * <p>A default {@link ODataProcessor} that implements all processor features in a single class.</p>
 * <p>It is recommended to derive from this class and it is required by the
 * {@link com.sap.core.odata.api.ODataServiceFactory} to build an {@link com.sap.core.odata.api.ODataService}.</p>
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
    Batch,
    CustomContentType
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
  public ODataResponse executeFunctionImport(GetFunctionImportUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see FunctionImportValue
   */
  @Override
  public ODataResponse executeFunctionImportValue(GetFunctionImportUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValue
   */
  @Override
  public ODataResponse readEntitySimplePropertyValue(GetSimplePropertyUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValue
   */
  @Override
  public ODataResponse updateEntitySimplePropertyValue(PutMergePatchUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValue
   */
  @Override
  public ODataResponse deleteEntitySimplePropertyValue(DeleteUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimpleProperty
   */
  @Override
  public ODataResponse readEntitySimpleProperty(GetSimplePropertyUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimpleProperty
   */
  @Override
  public ODataResponse updateEntitySimpleProperty(PutMergePatchUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMedia
   */
  @Override
  public ODataResponse readEntityMedia(GetMediaResourceUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMedia
   */
  @Override
  public ODataResponse updateEntityMedia(PutMergePatchUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMedia
   */
  @Override
  public ODataResponse deleteEntityMedia(DeleteUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinks
   */
  @Override
  public ODataResponse readEntityLinks(GetEntitySetLinksUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinks
   */
  @Override
  public ODataResponse countEntityLinks(GetEntitySetLinksCountUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse createEntityLink(PostUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse readEntityLink(GetEntityLinkUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse existsEntityLink(GetEntityLinkCountUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse updateEntityLink(PutMergePatchUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLink
   */
  @Override
  public ODataResponse deleteEntityLink(DeleteUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityComplexProperty
   */
  @Override
  public ODataResponse readEntityComplexProperty(GetComplexPropertyUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityComplexProperty
   */
  @Override
  public ODataResponse updateEntityComplexProperty(PutMergePatchUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySet
   */
  @Override
  public ODataResponse readEntitySet(GetEntitySetUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySet
   */
  @Override
  public ODataResponse countEntitySet(GetEntitySetCountUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse createEntity(PostUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse readEntity(GetEntityUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse existsEntity(GetEntityCountUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse updateEntity(PutMergePatchUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see Entity
   */
  @Override
  public ODataResponse deleteEntity(DeleteUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see ServiceDocument
   */
  @Override
  public ODataResponse readServiceDocument(GetServiceDocumentUriInfo uriInfo, String contentType) throws ODataException {
    EntityProvider odataSerializer = EntityProvider.create(contentType);
    contentType += "; " + DEFAULT_SERVICE_CHARSET_PARAMETER;

    ODataResponse response = ODataResponse.fromResponse(odataSerializer.writeServiceDocument(getContext().getService().getEntityDataModel(), getContext().getUriInfo().getServiceRoot().toASCIIString()))
      .header("Content-Type", contentType)
      .header("DataServiceVersion", Edm.DATA_SERVICE_VERSION_10).build();
    return response;
  }

  /**
   * @see Metadata
   */
  @Override
  public ODataResponse readMetadata(GetMetadataUriInfo uriInfo, String contentType) throws ODataException {
    EdmServiceMetadata edmServiceMetadata = getContext().getService().getEntityDataModel().getServiceMetadata();

    return ODataResponse
        .status(HttpStatusCodes.OK)
        .header("Content-Type", contentType.toString())
        .header("DataServiceVersion", edmServiceMetadata.getDataServiceVersion())
        .entity(edmServiceMetadata.getMetadata())
        .build();
  }

  /**
   * @see CustomContentType
   */
  @Override
  public List<String> getCustomContentTypes(Class<? extends ProcessorFeature> processorFeature) throws ODataException {
    return Collections.emptyList();
  }
}

