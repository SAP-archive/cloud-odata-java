package com.sap.core.odata.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMethodNotAllowedException;
import com.sap.core.odata.api.exception.ODataNotAcceptableException;
import com.sap.core.odata.api.exception.ODataUnsupportedMediaTypeException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataResponse;
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
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.ContentType.ODataFormat;
import com.sap.core.odata.core.commons.ODataHttpMethod;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.core.uri.UriType;

/**
 * Request dispatching according to URI type and HTTP method
 * @author SAP AG
 */
public class Dispatcher {
  private static final String URI_INFO_FOMRAT_JSON = "json";
  private static final String URI_INFO_FOMRAT_ATOM = "atom";
  private static final String URI_INFO_FOMRAT_XML = "xml";
  private static final String DEFAULT_CHARSET = "utf-8";

  private final ODataService service;

  public Dispatcher(final ODataService service) {
    this.service = service;
  }

  public ODataResponse dispatch(final ODataHttpMethod method, final UriInfoImpl uriInfo, final InputStream content, final String requestContentType, final List<String> acceptHeaderContentTypes) throws ODataException {
    preDispatchValidation(method, uriInfo, requestContentType);
    final String acceptContentType = doContentNegotiation(uriInfo, acceptHeaderContentTypes);
    return doDispatch(method, uriInfo, content, requestContentType, acceptContentType);
  }
  
  private void preDispatchValidation(final ODataHttpMethod method, final UriInfoImpl uriInfo, final String requestContentType) throws ODataException {
    validateUriMethod(method, uriInfo);
    checkFunctionImport(method, uriInfo);
    
    if (method != ODataHttpMethod.GET) {
      checkNotGetSystemQueryOptions(method, uriInfo);
      checkNumberOfNavigationSegments(uriInfo);
      checkProperty(method, uriInfo);

      if (method == ODataHttpMethod.POST || method == ODataHttpMethod.PUT
          || method == ODataHttpMethod.PATCH || method == ODataHttpMethod.MERGE) {
        checkRequestContentType(uriInfo, ContentType.parse(requestContentType));
      }
    }
  }
  
  private void checkFunctionImport(final ODataHttpMethod method, final UriInfoImpl uriInfo) throws ODataException {
    if (uriInfo.getFunctionImport() != null
        && uriInfo.getFunctionImport().getHttpMethod() != null
        && !uriInfo.getFunctionImport().getHttpMethod().equals(method.toString())) {
      throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
    }
  }

  private void checkNotGetSystemQueryOptions(final ODataHttpMethod method, final UriInfoImpl uriInfo) throws ODataException {
    switch (uriInfo.getUriType()) {
    case URI1:
    case URI6B:
      if (uriInfo.getFormat() != null
          || uriInfo.getFilter() != null
          || uriInfo.getInlineCount() != null
          || uriInfo.getOrderBy() != null
          || uriInfo.getSkipToken() != null
          || uriInfo.getSkip() != null
          || uriInfo.getTop() != null
          || !uriInfo.getExpand().isEmpty()
          || !uriInfo.getSelect().isEmpty()) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI2:
      if (uriInfo.getFormat() != null
          || !uriInfo.getExpand().isEmpty()
          || !uriInfo.getSelect().isEmpty()) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      if (method == ODataHttpMethod.DELETE) {
        if (uriInfo.getFilter() != null) {
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
        }
      }
      break;

    case URI3:
      if (uriInfo.getFormat() != null) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI4:
    case URI5:
      if (method == ODataHttpMethod.PUT || method == ODataHttpMethod.PATCH || method == ODataHttpMethod.MERGE) {
        if (!uriInfo.isValue() && uriInfo.getFormat() != null) {
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
        }
      }
      break;

    case URI7A:
      if (uriInfo.getFormat() != null || uriInfo.getFilter() != null) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI7B:
      if (uriInfo.getFormat() != null
          || uriInfo.getFilter() != null
          || uriInfo.getInlineCount() != null
          || uriInfo.getOrderBy() != null
          || uriInfo.getSkipToken() != null
          || uriInfo.getSkip() != null
          || uriInfo.getTop() != null) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI17:
      if (uriInfo.getFormat() != null || uriInfo.getFilter() != null) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    default:
      break;
    }
  }

  private void checkNumberOfNavigationSegments(final UriInfoImpl uriInfo) throws ODataException {
    switch (uriInfo.getUriType()) {
    case URI1:
    case URI6B:
    case URI7A:
    case URI7B:
      if (uriInfo.getNavigationSegments().size() > 1) {
        throw new ODataBadRequestException(ODataBadRequestException.NOTSUPPORTED);
      }
      break;

    case URI3:
    case URI4:
    case URI5:
    case URI17:
      if (!uriInfo.getNavigationSegments().isEmpty()) {
        throw new ODataBadRequestException(ODataBadRequestException.NOTSUPPORTED);
      }
      break;

    default:
      break;
    }
  }

  private void checkProperty(final ODataHttpMethod method, final UriInfoImpl uriInfo) throws ODataException {
    switch (uriInfo.getUriType()) {
    case URI4:
    case URI5:
      if (isPropertyKey(uriInfo)) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      if (method == ODataHttpMethod.DELETE) {
        if (!isPropertyNullable(getProperty(uriInfo))) {
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
        }
      }
      break;

    default:
      break;
    }
  }
  private EdmProperty getProperty(final UriInfo uriInfo) {
    if(uriInfo.getPropertyPath() == null || uriInfo.getPropertyPath().isEmpty()) {
      return null;
    }
    return uriInfo.getPropertyPath().get(uriInfo.getPropertyPath().size() - 1);
  }

  private boolean isPropertyKey(final UriInfo uriInfo) throws EdmException {
    final EdmEntityType entityType = uriInfo.getTargetEntitySet().getEntityType();
    return entityType.getKeyProperties().contains(getProperty(uriInfo));
  }

  private boolean isPropertyNullable(final EdmProperty property) throws EdmException {
    return property.getFacets() == null || property.getFacets().isNullable();
  }

  private void checkRequestContentType(final UriInfoImpl uriInfo, final ContentType contentType) throws ODataException {
    switch (uriInfo.getUriType()) {
    case URI1:
    case URI6B:
      final List<ContentType> supportedContentTypes =
          uriInfo.getTargetEntitySet().getEntityType().hasStream() ?
              Arrays.asList(ContentType.WILDCARD) : // A media resource can have any type.
              getSupportedContentTypes(EntityProcessor.class); // The request must contain a single entity!

      if (!isValidRequestContentType(contentType, supportedContentTypes)) {
        throw new ODataUnsupportedMediaTypeException(ODataUnsupportedMediaTypeException.NOT_SUPPORTED.addContent(contentType));
      }
      break;

    case URI2:
      if (!isValidRequestContentType(contentType, getSupportedContentTypes(EntityProcessor.class))) {
        throw new ODataUnsupportedMediaTypeException(ODataUnsupportedMediaTypeException.NOT_SUPPORTED.addContent(contentType));
      }
      break;

    case URI4:
    case URI5:
      if (uriInfo.isValue()
          && !isValidRequestContentTypeForProperty(getProperty(uriInfo), contentType)) {
        throw new ODataUnsupportedMediaTypeException(ODataUnsupportedMediaTypeException.NOT_SUPPORTED.addContent(contentType));
      }
      break;

    default:
      break;
    }
  }

  private boolean isValidRequestContentType(final ContentType contentType, final List<ContentType> allowedContentTypes) {
    if (contentType == null || contentType.hasWildcard()) {
      return false;
    }
    final ContentType requested = ensureCharsetParameterIsSet(contentType);
    return requested.hasMatch(allowedContentTypes);
  }

  private boolean isValidRequestContentTypeForProperty(final EdmProperty property, final ContentType contentType) throws EdmException {
    final ContentType requested = ensureCharsetParameterIsSet(contentType);
    final String mimeType = property.getMimeType();
    if (mimeType != null) {
      return requested.equals(ContentType.create(mimeType));
    } else {
      return requested.hasMatch(Arrays.asList(ContentType.TEXT_PLAIN, ContentType.TEXT_PLAIN_CS_UTF_8, ContentType.APPLICATION_OCTET_STREAM));
    }
  }

  private String doContentNegotiation(final UriInfoImpl uriInfo, List<String> acceptHeaderContentTypes) throws ODataException {
    ContentType contentType;
    if (uriInfo.getFormat() == null) {
      contentType = doContentNegotiationForAcceptHeader(uriInfo, acceptHeaderContentTypes);
    } else {
      contentType = doContentNegotiationForFormat(uriInfo);
    }

    if (contentType.getODataFormat() == ODataFormat.CUSTOM) {
      return contentType.getType();
    }
    return contentType.toContentTypeString();
  }

  private ContentType doContentNegotiationForFormat(final UriInfoImpl uriInfo) throws ODataException {
    validateFormatQuery(uriInfo);
    ContentType formatContentType = mapFormat(uriInfo);
    formatContentType = ensureCharsetParameterIsSet(formatContentType);

    final Class<? extends ODataProcessor> processorFeature = mapUriTypeToProcessorFeature(uriInfo);
    final List<ContentType> supportedContentTypes = getSupportedContentTypes(processorFeature);
    for (final ContentType contentType : supportedContentTypes) {
      if (contentType.equals(formatContentType)) {
        return formatContentType;
      }
    }

    throw new ODataNotAcceptableException(ODataNotAcceptableException.NOT_SUPPORTED_CONTENT_TYPE.addContent(uriInfo.getFormat()));
  }

  /**
   * Validates that <code>dollar format query/syntax</code> is correct for further processing.
   * If some validation error occurs an exception is thrown.
   * 
   * @param uriInfo
   * @throws ODataBadRequestException
   */
  private void validateFormatQuery(final UriInfoImpl uriInfo) throws ODataBadRequestException {
    if (uriInfo.isValue()) {
      throw new ODataBadRequestException(ODataBadRequestException.INVALID_SYNTAX);
    }
  }

  private ContentType ensureCharsetParameterIsSet(final ContentType contentType) {
    if (isContentTypeODataTextRelated(contentType)) {
      if (!contentType.getParameters().containsKey(ContentType.PARAMETER_CHARSET)) {
        return ContentType.create(contentType, ContentType.PARAMETER_CHARSET, DEFAULT_CHARSET);
      }
    }
    return contentType;
  }

  private boolean isContentTypeODataTextRelated(final ContentType contentType) {
    return (contentType != null) && (contentType.equals(ContentType.TEXT_PLAIN) || (contentType.getODataFormat() == ODataFormat.XML) || (contentType.getODataFormat() == ODataFormat.ATOM) || (contentType.getODataFormat() == ODataFormat.JSON));
  }

  private ContentType mapFormat(final UriInfoImpl uriInfo) {
    final String format = uriInfo.getFormat();
    if (URI_INFO_FOMRAT_XML.equals(format)) {
      return ContentType.APPLICATION_XML;
    } else if (URI_INFO_FOMRAT_ATOM.equals(format)) {
      if (uriInfo.getUriType() == UriType.URI0) {
        // special handling for serviceDocument uris (UriType.URI0)
        return ContentType.APPLICATION_ATOM_SVC;
      }
      return ContentType.APPLICATION_ATOM_XML;
    } else if (URI_INFO_FOMRAT_JSON.equals(format)) {
      return ContentType.APPLICATION_JSON;
    }

    return ContentType.create(format);
  }

  private ContentType doContentNegotiationForAcceptHeader(final UriInfoImpl uriInfo, List<String> acceptHeaderContentTypes) throws ODataException {
    final Class<? extends ODataProcessor> processorFeature = mapUriTypeToProcessorFeature(uriInfo);
    final List<ContentType> supportedContentTypes = getSupportedContentTypes(processorFeature);
    return contentNegotiation(extractAcceptHeaders(acceptHeaderContentTypes), supportedContentTypes);
  }
  
  private List<ContentType> extractAcceptHeaders(List<String> acceptHeaderValues) throws ODataBadRequestException {
    final List<ContentType> mediaTypes = new ArrayList<ContentType>();
    if(acceptHeaderValues != null) {
      for (final String mediaType : acceptHeaderValues) {
        try {
          mediaTypes.add(ContentType.create(mediaType.toString()));
        } catch (IllegalArgumentException e) {
          throw new ODataBadRequestException(ODataBadRequestException.INVALID_HEADER.addContent("Accept")
              .addContent(mediaType.toString()), e);
        }
      }
    }


    return mediaTypes;
  }


  private List<ContentType> getSupportedContentTypes(final Class<? extends ODataProcessor> processorFeature) throws ODataException {
    final List<ContentType> resultContentTypes = new ArrayList<ContentType>();
    for (final String contentType : service.getSupportedContentTypes(processorFeature)) {
      resultContentTypes.add(ContentType.create(contentType));
    }

    return resultContentTypes;
  }

  ContentType contentNegotiation(final List<ContentType> acceptedContentTypes, final List<ContentType> supportedContentTypes) throws ODataException {
    final Set<ContentType> setSupported = new HashSet<ContentType>(supportedContentTypes);

    if (acceptedContentTypes.isEmpty()) {
      if (!setSupported.isEmpty()) {
        return supportedContentTypes.get(0);
      }
    } else {
      for (ContentType contentType : acceptedContentTypes) {
        contentType = ensureCharsetParameterIsSet(contentType);
        final ContentType match = contentType.match(supportedContentTypes);
        if (match != null) {
          return match;
        }
      }
    }

    throw new ODataNotAcceptableException(ODataNotAcceptableException.NOT_SUPPORTED_ACCEPT_HEADER.addContent(acceptedContentTypes.toString()));
  }


  private void validateUriMethod(final ODataHttpMethod method, final UriInfoImpl uriInfo) throws ODataException {
    switch (uriInfo.getUriType()) {
    case URI0:
      if (method == ODataHttpMethod.GET) {
        return;
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI1:
    case URI6B:
      switch (method) {
      case GET:
      case POST:
        return;
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI2:
      switch (method) {
      case GET:
      case PUT:
      case PATCH:
      case MERGE:
      case DELETE:
        return;
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI3:
      switch (method) {
      case GET:
      case PUT:
      case PATCH:
      case MERGE:
        return;
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI4:
    case URI5:
      switch (method) {
      case GET:
      case PUT:
      case PATCH:
      case MERGE:
        return;
      case DELETE:
        if (uriInfo.isValue()) {
          return;
        } else {
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
        }
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI6A:
      switch (method) {
      case GET:
        return;
      case PUT:
      case PATCH:
      case MERGE:
      case DELETE:
        throw new ODataBadRequestException(ODataBadRequestException.NOTSUPPORTED);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI7A:
      switch (method) {
      case GET:
      case PUT:
      case PATCH:
      case MERGE:
      case DELETE:
        return;
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI7B:
      switch (method) {
      case GET:
      case POST:
        return;
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI8:
      if (method == ODataHttpMethod.GET) {
        return;
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI9:
      if (method == ODataHttpMethod.POST) {
        return;
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI10:
    case URI11:
    case URI12:
    case URI13:
    case URI14:
      return;

    case URI15:
      if (method == ODataHttpMethod.GET) {
        return;
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI16:
      if (method == ODataHttpMethod.GET) {
        return;
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI17:
      switch (method) {
      case GET:
      case PUT:
      case DELETE:
        return;
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI50A:
      if (method == ODataHttpMethod.GET) {
        return;
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI50B:
      if (method == ODataHttpMethod.GET) {
        return;
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    default:
      throw new ODataRuntimeException("Unknown or not implemented URI type: " + uriInfo.getUriType());
    }
  }
  
  public ODataResponse doDispatch(final ODataHttpMethod method, final UriInfoImpl uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
    switch (uriInfo.getUriType()) {
    case URI0:
      if (method == ODataHttpMethod.GET) {
        return service.getServiceDocumentProcessor().readServiceDocument(uriInfo, contentType);
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI1:
    case URI6B:
      switch (method) {
      case GET:
        return service.getEntitySetProcessor().readEntitySet(uriInfo, contentType);
      case POST:
        return service.getEntitySetProcessor().createEntity(uriInfo, content, requestContentType, contentType);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI2:
      switch (method) {
      case GET:
        return service.getEntityProcessor().readEntity(uriInfo, contentType);
      case PUT:
        return service.getEntityProcessor().updateEntity(uriInfo, content, requestContentType, false, contentType);
      case PATCH:
      case MERGE:
        return service.getEntityProcessor().updateEntity(uriInfo, content, requestContentType, true, contentType);
      case DELETE:
        return service.getEntityProcessor().deleteEntity(uriInfo, contentType);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI3:
      switch (method) {
      case GET:
        return service.getEntityComplexPropertyProcessor().readEntityComplexProperty(uriInfo, contentType);
      case PUT:
        return service.getEntityComplexPropertyProcessor().updateEntityComplexProperty(uriInfo, content, requestContentType, false, contentType);
      case PATCH:
      case MERGE:
        return service.getEntityComplexPropertyProcessor().updateEntityComplexProperty(uriInfo, content, requestContentType, true, contentType);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI4:
    case URI5:
      switch (method) {
      case GET:
        if (uriInfo.isValue()) {
          return service.getEntitySimplePropertyValueProcessor().readEntitySimplePropertyValue(uriInfo, contentType);
        } else {
          return service.getEntitySimplePropertyProcessor().readEntitySimpleProperty(uriInfo, contentType);
        }
      case PUT:
      case PATCH:
      case MERGE:
        if (uriInfo.isValue()) {
          return service.getEntitySimplePropertyValueProcessor().updateEntitySimplePropertyValue(uriInfo, content, requestContentType, contentType);
        } else {
          return service.getEntitySimplePropertyProcessor().updateEntitySimpleProperty(uriInfo, content, requestContentType, contentType);
        }
      case DELETE:
        if (uriInfo.isValue()) {
          return service.getEntitySimplePropertyValueProcessor().deleteEntitySimplePropertyValue(uriInfo, contentType);
        } else {
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
        }
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI6A:
      switch (method) {
      case GET:
        return service.getEntityProcessor().readEntity(uriInfo, contentType);
      case PUT:
      case PATCH:
      case MERGE:
      case DELETE:
        throw new ODataBadRequestException(ODataBadRequestException.NOTSUPPORTED);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI7A:
      switch (method) {
      case GET:
        return service.getEntityLinkProcessor().readEntityLink(uriInfo, contentType);
      case PUT:
      case PATCH:
      case MERGE:
        return service.getEntityLinkProcessor().updateEntityLink(uriInfo, content, requestContentType, contentType);
      case DELETE:
        return service.getEntityLinkProcessor().deleteEntityLink(uriInfo, contentType);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI7B:
      switch (method) {
      case GET:
        return service.getEntityLinksProcessor().readEntityLinks(uriInfo, contentType);
      case POST:
        return service.getEntityLinksProcessor().createEntityLink(uriInfo, content, requestContentType, contentType);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI8:
      if (method == ODataHttpMethod.GET) {
        return service.getMetadataProcessor().readMetadata(uriInfo, contentType);
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI9:
      if (method == ODataHttpMethod.POST) {
        return service.getBatchProcessor().executeBatch(contentType);
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI10:
    case URI11:
    case URI12:
    case URI13:
      return service.getFunctionImportProcessor().executeFunctionImport(uriInfo, contentType);

    case URI14:
      if (uriInfo.isValue()) {
        return service.getFunctionImportValueProcessor().executeFunctionImportValue(uriInfo, contentType);
      } else {
        return service.getFunctionImportProcessor().executeFunctionImport(uriInfo, contentType);
      }

    case URI15:
      if (method == ODataHttpMethod.GET) {
        return service.getEntitySetProcessor().countEntitySet(uriInfo, contentType);
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI16:
      if (method == ODataHttpMethod.GET) {
        return service.getEntityProcessor().existsEntity(uriInfo, contentType);
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI17:
      switch (method) {
      case GET:
        return service.getEntityMediaProcessor().readEntityMedia(uriInfo, contentType);
      case PUT:
        return service.getEntityMediaProcessor().updateEntityMedia(uriInfo, content, requestContentType, contentType);
      case DELETE:
        return service.getEntityMediaProcessor().deleteEntityMedia(uriInfo, contentType);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI50A:
      if (method == ODataHttpMethod.GET) {
        return service.getEntityLinkProcessor().existsEntityLink(uriInfo, contentType);
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI50B:
      if (method == ODataHttpMethod.GET) {
        return service.getEntityLinksProcessor().countEntityLinks(uriInfo, contentType);
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    default:
      throw new ODataRuntimeException("Unknown or not implemented URI type: " + uriInfo.getUriType());
    }
  }

  public Class<? extends ODataProcessor> mapUriTypeToProcessorFeature(final UriInfoImpl uriInfo) {
    Class<? extends ODataProcessor> feature;

    switch (uriInfo.getUriType()) {
    case URI0:
      feature = ServiceDocumentProcessor.class;
      break;
    case URI1:
    case URI6B:
    case URI15:
      feature = EntitySetProcessor.class;
      break;
    case URI2:
    case URI6A:
    case URI16:
      feature = EntityProcessor.class;
      break;
    case URI3:
      feature = EntityComplexPropertyProcessor.class;
      break;
    case URI4:
    case URI5:
      feature = uriInfo.isValue() ? EntitySimplePropertyValueProcessor.class : EntitySimplePropertyProcessor.class;
      break;
    case URI7A:
    case URI50A:
      feature = EntityLinkProcessor.class;
      break;
    case URI7B:
    case URI50B:
      feature = EntityLinksProcessor.class;
      break;
    case URI8:
      feature = MetadataProcessor.class;
      break;
    case URI9:
      feature = BatchProcessor.class;
      break;
    case URI10:
    case URI11:
    case URI12:
    case URI13:
      feature = FunctionImportProcessor.class;
      break;
    case URI14:
      feature = uriInfo.isValue() ? FunctionImportValueProcessor.class : FunctionImportProcessor.class;
      break;
    case URI17:
      feature = EntityMediaProcessor.class;
      break;
    default:
      throw new ODataRuntimeException("Unknown or not implemented URI type: " + uriInfo.getUriType());
    }

    return feature;
  }
}
