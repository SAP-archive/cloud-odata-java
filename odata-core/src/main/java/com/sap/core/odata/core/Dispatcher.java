package com.sap.core.odata.core;

import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMethodNotAllowedException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.aspect.ProcessorAspect;
import com.sap.core.odata.api.service.ODataService;
import com.sap.core.odata.core.enums.ODataHttpMethod;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.core.uri.UriParserResultImpl;

/**
 * Request dispatching according to URI type and HTTP method
 * @author SAP AG
 */
public class Dispatcher {

  public Dispatcher(ODataService service) {
    this.service = service;
  }

  private ODataService service;

  public ODataResponse dispatch(final ODataHttpMethod method, final UriParserResultImpl uriParserResult, final ContentType contentType) throws ODataException {
    switch (uriParserResult.getUriType()) {
    case URI0:
      if (method == ODataHttpMethod.GET)
        return this.service.getServiceDocumentProcessor().readServiceDocument(uriParserResult, contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI1:
    case URI6B:
      switch (method) {
      case GET:
        return this.service.getEntitySetProcessor().readEntitySet(uriParserResult, contentType);
      case POST:
        return this.service.getEntitySetProcessor().createEntity(contentType);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI2:
      switch (method) {
      case GET:
        return this.service.getEntityProcessor().readEntity(uriParserResult, contentType);
      case PUT:
      case PATCH:
      case MERGE:
        if (contentType == null
            && uriParserResult.getExpand().isEmpty()
            && uriParserResult.getSelect().isEmpty())
          return this.service.getEntityProcessor().updateEntity(contentType);
        else
          throw new ODataBadRequestException(ODataBadRequestException.COMMON);
      case DELETE:
        if (contentType == null
            && uriParserResult.getFilter() == null
            && uriParserResult.getExpand().isEmpty()
            && uriParserResult.getSelect().isEmpty())
          return service.getEntityProcessor().deleteEntity(uriParserResult, contentType);
        else
          throw new ODataBadRequestException(ODataBadRequestException.COMMON);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI3:
      switch (method) {
      case GET:
        return this.service.getEntityComplexPropertyProcessor().readEntityComplexProperty(uriParserResult, contentType);
      case PUT:
      case PATCH:
      case MERGE:
        return this.service.getEntityComplexPropertyProcessor().updateEntityComplexProperty(contentType);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI4:
    case URI5:
      switch (method) {
      case GET:
        if (uriParserResult.isValue())
          return this.service.getEntitySimplePropertyValueProcessor().readEntitySimplePropertyValue(uriParserResult, contentType);
        else
          return this.service.getEntitySimplePropertyProcessor().readEntitySimpleProperty(uriParserResult, contentType);
      case PUT:
      case PATCH:
      case MERGE:
        if (uriParserResult.isValue())
          return this.service.getEntitySimplePropertyValueProcessor().updateEntitySimplePropertyValue(contentType);
        else
          return this.service.getEntitySimplePropertyProcessor().updateEntitySimpleProperty(contentType);
      case DELETE:
        if (uriParserResult.isValue())
          return this.service.getEntitySimplePropertyValueProcessor().deleteEntitySimplePropertyValue(contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI6A:
      if (method == ODataHttpMethod.GET)
        return this.service.getEntityProcessor().readEntity(uriParserResult, contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI7A:
      switch (method) {
      case GET:
        return this.service.getEntityLinkProcessor().readEntityLink(uriParserResult, contentType);
      case PUT:
      case PATCH:
      case MERGE:
        return this.service.getEntityLinkProcessor().updateEntityLink(contentType);
      case DELETE:
        if (contentType == null)
          return service.getEntityLinkProcessor().deleteEntityLink(uriParserResult, contentType);
        else
          throw new ODataBadRequestException(ODataBadRequestException.COMMON);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI7B:
      switch (method) {
      case GET:
        return this.service.getEntityLinksProcessor().readEntityLinks(uriParserResult, contentType);
      case POST:
        return this.service.getEntityLinksProcessor().createEntityLink(contentType);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI8:
      if (method == ODataHttpMethod.GET)
        return this.service.getMetadataProcessor().readMetadata(uriParserResult, contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI9:
      if (method == ODataHttpMethod.POST)
        return this.service.getBatchProcessor().executeBatch(contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI10:
    case URI11:
    case URI12:
    case URI13:
      return this.service.getFunctionImportProcessor().executeFunctionImport(uriParserResult,contentType);

    case URI14:
      if (uriParserResult.isValue())
        return this.service.getFunctionImportValueProcessor().executeFunctionImportValue(uriParserResult,contentType);
      else
        return this.service.getFunctionImportProcessor().executeFunctionImport(uriParserResult,contentType);

    case URI15:
      if (method == ODataHttpMethod.GET)
        return this.service.getEntitySetProcessor().countEntitySet(uriParserResult,contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI16:
      if (method == ODataHttpMethod.GET)
        return this.service.getEntityProcessor().existsEntity(uriParserResult,contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI17:
      switch (method) {
      case GET:
        return this.service.getEntityMediaProcessor().readEntityMedia(uriParserResult,contentType);
      case PUT:
        return this.service.getEntityMediaProcessor().updateEntityMedia(contentType);
      case DELETE:
        return this.service.getEntityMediaProcessor().deleteEntityMedia(contentType);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI50A:
      if (method == ODataHttpMethod.GET)
        return this.service.getEntityLinkProcessor().existsEntityLink(uriParserResult,contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI50B:
      if (method == ODataHttpMethod.GET)
        return this.service.getEntityLinksProcessor().countEntityLinks(uriParserResult,contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    default:
      throw new ODataRuntimeException("Unknown or not implemented URI type: " + uriParserResult.getUriType());
    }
  }

  public ProcessorAspect mapUriTypeToProcessorAspect(UriParserResultImpl uriParserResult) {
    ProcessorAspect aspect;

    switch (uriParserResult.getUriType()) {
    case URI0:
      aspect = ProcessorAspect.SERVICE_DOCUMENT;
      break;
    case URI1:
    case URI6B:
      aspect = ProcessorAspect.ENTITY_SET;
      break;
    case URI2:
    case URI6A:
      aspect = ProcessorAspect.ENTITY;
      break;
    case URI3:
      aspect = ProcessorAspect.ENTITY_COMPLEX_PROPERTY;
      break;
    case URI4:
    case URI5:
      if (uriParserResult.isValue()) {
        aspect = ProcessorAspect.ENTITY_SIMPLE_PROPERTY_VALUE;
      } else {
        aspect = ProcessorAspect.ENTITY_SIMPLE_PROPERTY;
      }
      break;
    case URI7A:
      aspect = ProcessorAspect.ENTITY_LINK;
      break;
    case URI7B:
      aspect = ProcessorAspect.ENTITY_LINKS;
      break;
    case URI8:
      aspect = ProcessorAspect.METDDATA;
      break;
    case URI9:
      aspect = ProcessorAspect.BATCH;
      break;
    case URI10:
    case URI11:
    case URI12:
    case URI13:
      aspect = ProcessorAspect.FUNCTION_IMPORT;
      break;
    case URI14:
      if (uriParserResult.isValue()) {
        aspect = ProcessorAspect.FUNCTION_IMPORT_VALUE;
      } else {
        aspect = ProcessorAspect.FUNCTION_IMPORT;
      }
      break;
    case URI15:
      aspect = ProcessorAspect.ENTITY_SET;
      break;
    case URI16:
      aspect = ProcessorAspect.ENTITY;
      break;
    case URI17:
      aspect = ProcessorAspect.ENTITY_MEDIA;
      break;
    case URI50A:
      aspect = ProcessorAspect.ENTITY_LINK;
      break;
    case URI50B:
      aspect = ProcessorAspect.ENTITY_LINKS;
      break;
    default:
      throw new ODataRuntimeException("Unknown or not implemented URI type: " + uriParserResult.getUriType());
    }

    return aspect;
  }
}
