package com.sap.core.odata.api.processor;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.processor.feature.CustomContentType;
import com.sap.core.odata.api.processor.part.BatchProcessor;
import com.sap.core.odata.api.processor.part.EntityComplexPropertyProcessor;
import com.sap.core.odata.api.processor.part.EntityLinkProcessor;
import com.sap.core.odata.api.processor.part.EntityLinksProcessor;
import com.sap.core.odata.api.processor.part.EntityMediaProcessor;
import com.sap.core.odata.api.processor.part.EntityProcessor;
import com.sap.core.odata.api.processor.part.EntitySetProcessor;
import com.sap.core.odata.api.processor.part.EntitySimplePropertyProcessor;
import com.sap.core.odata.api.processor.part.EntitySimplePropertyValueProcessor;
import com.sap.core.odata.api.processor.part.FunctionImportProcessor;
import com.sap.core.odata.api.processor.part.FunctionImportValueProcessor;
import com.sap.core.odata.api.processor.part.MetadataProcessor;
import com.sap.core.odata.api.processor.part.ServiceDocumentProcessor;
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
public abstract class ODataSingleProcessor implements 
    MetadataProcessor,
    ServiceDocumentProcessor,
    EntityProcessor,
    EntitySetProcessor,
    EntityComplexPropertyProcessor,
    EntityLinkProcessor,
    EntityLinksProcessor,
    EntityMediaProcessor,
    EntitySimplePropertyProcessor,
    EntitySimplePropertyValueProcessor,
    FunctionImportProcessor,
    FunctionImportValueProcessor,
    BatchProcessor,
    CustomContentType
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
    return context;
  }

  /**
   * @see BatchProcessor
   */
  @Override
  public ODataResponse executeBatch(String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see FunctionImportProcessor
   */
  @Override
  public ODataResponse executeFunctionImport(GetFunctionImportUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see FunctionImportValueProcessor
   */
  @Override
  public ODataResponse executeFunctionImportValue(GetFunctionImportUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValueProcessor
   */
  @Override
  public ODataResponse readEntitySimplePropertyValue(GetSimplePropertyUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValueProcessor
   */
  @Override
  public ODataResponse updateEntitySimplePropertyValue(PutMergePatchUriInfo uriInfo, InputStream content, String requestContentType, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValueProcessor
   */
  @Override
  public ODataResponse deleteEntitySimplePropertyValue(DeleteUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyProcessor
   */
  @Override
  public ODataResponse readEntitySimpleProperty(GetSimplePropertyUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyProcessor
   */
  @Override
  public ODataResponse updateEntitySimpleProperty(PutMergePatchUriInfo uriInfo, InputStream content, String requestContentType, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMediaProcessor
   */
  @Override
  public ODataResponse readEntityMedia(GetMediaResourceUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMediaProcessor
   */
  @Override
  public ODataResponse updateEntityMedia(PutMergePatchUriInfo uriInfo, InputStream content, String requestContentType, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMediaProcessor
   */
  @Override
  public ODataResponse deleteEntityMedia(DeleteUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinksProcessor
   */
  @Override
  public ODataResponse readEntityLinks(GetEntitySetLinksUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinksProcessor
   */
  @Override
  public ODataResponse countEntityLinks(GetEntitySetLinksCountUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinkProcessor
   */
  @Override
  public ODataResponse createEntityLink(PostUriInfo uriInfo, InputStream content, String requestContentType, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinkProcessor
   */
  @Override
  public ODataResponse readEntityLink(GetEntityLinkUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinkProcessor
   */
  @Override
  public ODataResponse existsEntityLink(GetEntityLinkCountUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinkProcessor
   */
  @Override
  public ODataResponse updateEntityLink(PutMergePatchUriInfo uriInfo, InputStream content, String requestContentType, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinkProcessor
   */
  @Override
  public ODataResponse deleteEntityLink(DeleteUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityComplexPropertyProcessor
   */
  @Override
  public ODataResponse readEntityComplexProperty(GetComplexPropertyUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityComplexPropertyProcessor
   */
  @Override
  public ODataResponse updateEntityComplexProperty(PutMergePatchUriInfo uriInfo, InputStream content, String requestContentType, boolean merge, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySetProcessor
   */
  @Override
  public ODataResponse readEntitySet(GetEntitySetUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySetProcessor
   */
  @Override
  public ODataResponse countEntitySet(GetEntitySetCountUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySetProcessor
   */
  @Override
  public ODataResponse createEntity(PostUriInfo uriInfo, InputStream content, String requestContentType, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityProcessor
   */
  @Override
  public ODataResponse readEntity(GetEntityUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityProcessor
   */
  @Override
  public ODataResponse existsEntity(GetEntityCountUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityProcessor
   */
  @Override
  public ODataResponse updateEntity(PutMergePatchUriInfo uriInfo, InputStream content, String requestContentType, boolean merge, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityProcessor
   */
  @Override
  public ODataResponse deleteEntity(DeleteUriInfo uriInfo, String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see ServiceDocumentProcessor
   */
  @Override
  public ODataResponse readServiceDocument(GetServiceDocumentUriInfo uriInfo, String contentType) throws ODataException {
    Edm entityDataModel = getContext().getService().getEntityDataModel();
    String serviceRoot = getContext().getPathInfo().getServiceRoot().toASCIIString();
    
    ODataResponse response = EntityProvider.writeServiceDocument(contentType, entityDataModel, serviceRoot);
    ODataResponseBuilder odataResponseBuilder = ODataResponse.fromResponse(response)
        .header("DataServiceVersion", Edm.DATA_SERVICE_VERSION_10);
    if(needResponseContentTypeReplacement(contentType, response.getContentHeader())) {
      odataResponseBuilder.contentHeader(contentType);
    }
    return odataResponseBuilder.build();
  }

  /**
   * Check if response content type needs to be replaced by request content type.
   * 
   * @param requestContentType
   * @param responseContentType
   * @return
   */
  private boolean needResponseContentTypeReplacement(String requestContentType, String responseContentType) {
    if(requestContentType.equals(responseContentType)) {
      return false;
    } else if(responseContentType.contains("atomsvc") && requestContentType.contains("atom")) {
      return false;
    }
    return true;
  }

  /**
   * @see MetadataProcessor
   */
  @Override
  public ODataResponse readMetadata(GetMetadataUriInfo uriInfo, String contentType) throws ODataException {
    final EdmServiceMetadata edmServiceMetadata = getContext().getService().getEntityDataModel().getServiceMetadata();

    return ODataResponse
        .status(HttpStatusCodes.OK)
        .header("Content-Type", contentType)
        .header("DataServiceVersion", edmServiceMetadata.getDataServiceVersion())
        .entity(edmServiceMetadata.getMetadata())
        .build();
  }

  /**
   * @see CustomContentType
   */
  @Override
  public List<String> getCustomContentTypes(Class<? extends ODataProcessor> processorFeature) throws ODataException {
    return Collections.emptyList();
  }
}
