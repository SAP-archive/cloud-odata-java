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
package com.sap.core.odata.api.processor;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
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
public abstract class ODataSingleProcessor implements MetadataProcessor, ServiceDocumentProcessor, EntityProcessor, EntitySetProcessor, EntityComplexPropertyProcessor, EntityLinkProcessor, EntityLinksProcessor, EntityMediaProcessor, EntitySimplePropertyProcessor, EntitySimplePropertyValueProcessor, FunctionImportProcessor, FunctionImportValueProcessor, BatchProcessor, CustomContentType {

  /**
   * A request context object usually injected by the OData library.
   */
  private ODataContext context;

  /**
   * @see ODataProcessor
   */
  @Override
  public void setContext(final ODataContext context) {
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
  public ODataResponse executeBatch(final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see FunctionImportProcessor
   */
  @Override
  public ODataResponse executeFunctionImport(final GetFunctionImportUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see FunctionImportValueProcessor
   */
  @Override
  public ODataResponse executeFunctionImportValue(final GetFunctionImportUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValueProcessor
   */
  @Override
  public ODataResponse readEntitySimplePropertyValue(final GetSimplePropertyUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValueProcessor
   */
  @Override
  public ODataResponse updateEntitySimplePropertyValue(final PutMergePatchUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyValueProcessor
   */
  @Override
  public ODataResponse deleteEntitySimplePropertyValue(final DeleteUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyProcessor
   */
  @Override
  public ODataResponse readEntitySimpleProperty(final GetSimplePropertyUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySimplePropertyProcessor
   */
  @Override
  public ODataResponse updateEntitySimpleProperty(final PutMergePatchUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMediaProcessor
   */
  @Override
  public ODataResponse readEntityMedia(final GetMediaResourceUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMediaProcessor
   */
  @Override
  public ODataResponse updateEntityMedia(final PutMergePatchUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityMediaProcessor
   */
  @Override
  public ODataResponse deleteEntityMedia(final DeleteUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinksProcessor
   */
  @Override
  public ODataResponse readEntityLinks(final GetEntitySetLinksUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinksProcessor
   */
  @Override
  public ODataResponse countEntityLinks(final GetEntitySetLinksCountUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinkProcessor
   */
  @Override
  public ODataResponse createEntityLink(final PostUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinkProcessor
   */
  @Override
  public ODataResponse readEntityLink(final GetEntityLinkUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinkProcessor
   */
  @Override
  public ODataResponse existsEntityLink(final GetEntityLinkCountUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinkProcessor
   */
  @Override
  public ODataResponse updateEntityLink(final PutMergePatchUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityLinkProcessor
   */
  @Override
  public ODataResponse deleteEntityLink(final DeleteUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityComplexPropertyProcessor
   */
  @Override
  public ODataResponse readEntityComplexProperty(final GetComplexPropertyUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityComplexPropertyProcessor
   */
  @Override
  public ODataResponse updateEntityComplexProperty(final PutMergePatchUriInfo uriInfo, final InputStream content, final String requestContentType, final boolean merge, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySetProcessor
   */
  @Override
  public ODataResponse readEntitySet(final GetEntitySetUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySetProcessor
   */
  @Override
  public ODataResponse countEntitySet(final GetEntitySetCountUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntitySetProcessor
   */
  @Override
  public ODataResponse createEntity(final PostUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityProcessor
   */
  @Override
  public ODataResponse readEntity(final GetEntityUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityProcessor
   */
  @Override
  public ODataResponse existsEntity(final GetEntityCountUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityProcessor
   */
  @Override
  public ODataResponse updateEntity(final PutMergePatchUriInfo uriInfo, final InputStream content, final String requestContentType, final boolean merge, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see EntityProcessor
   */
  @Override
  public ODataResponse deleteEntity(final DeleteUriInfo uriInfo, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * @see ServiceDocumentProcessor
   */
  @Override
  public ODataResponse readServiceDocument(final GetServiceDocumentUriInfo uriInfo, final String contentType) throws ODataException {
    final Edm entityDataModel = getContext().getService().getEntityDataModel();
    final String serviceRoot = getContext().getPathInfo().getServiceRoot().toASCIIString();

    final ODataResponse response = EntityProvider.writeServiceDocument(contentType, entityDataModel, serviceRoot);
    final ODataResponseBuilder odataResponseBuilder = ODataResponse.fromResponse(response).header(ODataHttpHeaders.DATASERVICEVERSION, ODataServiceVersion.V10);
    if (isContentTypeUpdateNecessary(contentType, response)) {
      odataResponseBuilder.contentHeader(contentType);
    }
    return odataResponseBuilder.build();
  }

  /**
   * Simple check whether the content type for the {@link ODataResponse} needs adapted or not (based on requested content type).
   * 
   * @param contentType
   * @param response
   * @return true if an update is necessary
   */
  private boolean isContentTypeUpdateNecessary(final String contentType, final ODataResponse response) {
    boolean contentTypeAlreadySet = contentType.equals(response.getContentHeader());
    boolean requestedAtomAndRespondAtomSvc = contentType.contains("atom") && response.getContentHeader().contains("atomsvc");

    return !(contentTypeAlreadySet || requestedAtomAndRespondAtomSvc);
  }

  /**
   * @see MetadataProcessor
   */
  @Override
  public ODataResponse readMetadata(final GetMetadataUriInfo uriInfo, final String contentType) throws ODataException {
    final EdmServiceMetadata edmServiceMetadata = getContext().getService().getEntityDataModel().getServiceMetadata();

    return ODataResponse.status(HttpStatusCodes.OK).header(HttpHeaders.CONTENT_TYPE, contentType).header(ODataHttpHeaders.DATASERVICEVERSION, edmServiceMetadata.getDataServiceVersion()).entity(edmServiceMetadata.getMetadata()).build();
  }

  /**
   * @see CustomContentType
   */
  @Override
  public List<String> getCustomContentTypes(final Class<? extends ODataProcessor> processorFeature) throws ODataException {
    return Collections.emptyList();
  }
}
