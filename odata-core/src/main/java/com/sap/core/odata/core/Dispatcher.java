package com.sap.core.odata.core;

import java.io.InputStream;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMethodNotAllowedException;
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
import com.sap.core.odata.core.commons.ODataHttpMethod;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.core.uri.UriInfoImpl;

/**
 * Request dispatching according to URI type and HTTP method
 * @author SAP AG
 */
public class Dispatcher {

  private final ODataService service;

  public Dispatcher(ODataService service) {
    this.service = service;
  }

  public ODataResponse dispatch(final ODataHttpMethod method, final UriInfoImpl uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
    if (uriInfo.getFunctionImport() != null
        && uriInfo.getFunctionImport().getHttpMethod() != null
        && !uriInfo.getFunctionImport().getHttpMethod().equals(method.toString()))
      throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    switch (uriInfo.getUriType()) {
    case URI0:
      if (method == ODataHttpMethod.GET)
        return service.getServiceDocumentProcessor().readServiceDocument(uriInfo, contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI1:
      switch (method) {
      case GET:
        return service.getEntitySetProcessor().readEntitySet(uriInfo, contentType);
      case POST:
        if (uriInfo.getFormat() == null
            && uriInfo.getFilter() == null
            && uriInfo.getInlineCount() == null
            && uriInfo.getOrderBy() == null
            && uriInfo.getSkipToken() == null
            && uriInfo.getSkip() == null
            && uriInfo.getTop() == null
            && uriInfo.getExpand().isEmpty()
            && uriInfo.getSelect().isEmpty())
          return service.getEntitySetProcessor().createEntity(uriInfo, content, requestContentType, contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI2:
      switch (method) {
      case GET:
        return service.getEntityProcessor().readEntity(uriInfo, contentType);
      case PUT:
        if (uriInfo.getFormat() == null
            && uriInfo.getExpand().isEmpty()
            && uriInfo.getSelect().isEmpty())
          return service.getEntityProcessor().updateEntity(uriInfo, content, requestContentType, false, contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      case PATCH:
      case MERGE:
        if (uriInfo.getFormat() == null
            && uriInfo.getExpand().isEmpty()
            && uriInfo.getSelect().isEmpty())
          return service.getEntityProcessor().updateEntity(uriInfo, content, requestContentType, true, contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      case DELETE:
        if (uriInfo.getFormat() == null
            && uriInfo.getFilter() == null
            && uriInfo.getExpand().isEmpty()
            && uriInfo.getSelect().isEmpty())
          return service.getEntityProcessor().deleteEntity(uriInfo, contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI3:
      switch (method) {
      case GET:
        return service.getEntityComplexPropertyProcessor().readEntityComplexProperty(uriInfo, contentType);
      case PUT:
        if (uriInfo.getFormat() == null
            && uriInfo.getNavigationSegments().isEmpty())
          return service.getEntityComplexPropertyProcessor().updateEntityComplexProperty(uriInfo, content, requestContentType, false, contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      case PATCH:
      case MERGE:
        if (uriInfo.getFormat() == null
            && uriInfo.getNavigationSegments().isEmpty())
          return service.getEntityComplexPropertyProcessor().updateEntityComplexProperty(uriInfo, content, requestContentType, true, contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI4:
    case URI5:
      switch (method) {
      case GET:
        if (uriInfo.isValue())
          return service.getEntitySimplePropertyValueProcessor().readEntitySimplePropertyValue(uriInfo, contentType);
        else
          return service.getEntitySimplePropertyProcessor().readEntitySimpleProperty(uriInfo, contentType);
      case PUT:
      case PATCH:
      case MERGE:
        if (uriInfo.getNavigationSegments().isEmpty()
            && isPropertyNotKey(getEntityType(uriInfo), getProperty(uriInfo)))
          if (uriInfo.isValue())
            return service.getEntitySimplePropertyValueProcessor().updateEntitySimplePropertyValue(uriInfo, content, requestContentType, contentType);
          else
            if (uriInfo.getFormat() == null)
              return service.getEntitySimplePropertyProcessor().updateEntitySimpleProperty(uriInfo, content, requestContentType, contentType);
            else
              throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      case DELETE:
        final EdmProperty property = getProperty(uriInfo);
        if (uriInfo.isValue()
            && uriInfo.getNavigationSegments().isEmpty()
            && isPropertyNotKey(getEntityType(uriInfo), property) && isPropertyNullable(property))
          return service.getEntitySimplePropertyValueProcessor().deleteEntitySimplePropertyValue(uriInfo, contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI6A:
      if (method == ODataHttpMethod.GET)
        return service.getEntityProcessor().readEntity(uriInfo, contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI6B:
      if (method == ODataHttpMethod.GET)
        return service.getEntitySetProcessor().readEntitySet(uriInfo, contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI7A:
      switch (method) {
      case GET:
        return service.getEntityLinkProcessor().readEntityLink(uriInfo, contentType);
      case PUT:
      case PATCH:
      case MERGE:
        if (uriInfo.getFormat() == null
            && uriInfo.getFilter() == null
            && uriInfo.getNavigationSegments().size() == 1)
          return service.getEntityLinkProcessor().updateEntityLink(uriInfo, content, requestContentType, contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      case DELETE:
        if (uriInfo.getFormat() == null
            && uriInfo.getFilter() == null
            && uriInfo.getNavigationSegments().size() == 1)
          return service.getEntityLinkProcessor().deleteEntityLink(uriInfo, contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI7B:
      switch (method) {
      case GET:
        return service.getEntityLinksProcessor().readEntityLinks(uriInfo, contentType);
      case POST:
        if (uriInfo.getFormat() == null
            && uriInfo.getFilter() == null
            && uriInfo.getInlineCount() == null
            && uriInfo.getOrderBy() == null
            && uriInfo.getSkipToken() == null
            && uriInfo.getSkip() == null
            && uriInfo.getTop() == null
            && uriInfo.getNavigationSegments().size() == 1)
          return service.getEntityLinksProcessor().createEntityLink(uriInfo, content, requestContentType, contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI8:
      if (method == ODataHttpMethod.GET)
        return service.getMetadataProcessor().readMetadata(uriInfo, contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI9:
      if (method == ODataHttpMethod.POST)
        return service.getBatchProcessor().executeBatch(contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI10:
    case URI11:
    case URI12:
    case URI13:
      return service.getFunctionImportProcessor().executeFunctionImport(uriInfo, contentType);

    case URI14:
      if (uriInfo.isValue())
        return service.getFunctionImportValueProcessor().executeFunctionImportValue(uriInfo, contentType);
      else
        return service.getFunctionImportProcessor().executeFunctionImport(uriInfo, contentType);

    case URI15:
      if (method == ODataHttpMethod.GET)
        return service.getEntitySetProcessor().countEntitySet(uriInfo, contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI16:
      if (method == ODataHttpMethod.GET)
        return service.getEntityProcessor().existsEntity(uriInfo, contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI17:
      switch (method) {
      case GET:
        return service.getEntityMediaProcessor().readEntityMedia(uriInfo, contentType);
      case PUT:
        if (uriInfo.getFormat() == null
            && uriInfo.getFilter() == null
            && uriInfo.getNavigationSegments().isEmpty())
          return service.getEntityMediaProcessor().updateEntityMedia(uriInfo, content, requestContentType, contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      case DELETE:
        if (uriInfo.getFormat() == null
            && uriInfo.getFilter() == null
            && uriInfo.getNavigationSegments().isEmpty())
          return service.getEntityMediaProcessor().deleteEntityMedia(uriInfo, contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI50A:
      if (method == ODataHttpMethod.GET)
        return service.getEntityLinkProcessor().existsEntityLink(uriInfo, contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI50B:
      if (method == ODataHttpMethod.GET)
        return service.getEntityLinksProcessor().countEntityLinks(uriInfo, contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    default:
      throw new ODataRuntimeException("Unknown or not implemented URI type: " + uriInfo.getUriType());
    }
  }

  private EdmProperty getProperty(final UriInfo uriInfo) {
    return uriInfo.getPropertyPath().get(uriInfo.getPropertyPath().size() - 1);
  }

  private EdmEntityType getEntityType(final UriInfo uriInfo) throws EdmException {
    return uriInfo.getTargetEntitySet().getEntityType();
  }

  private boolean isPropertyNotKey(final EdmEntityType entityType, final EdmProperty property) throws EdmException {
    return !entityType.getKeyProperties().contains(property);
  }

  private boolean isPropertyNullable(final EdmProperty property) throws EdmException {
    return property.getFacets() == null || property.getFacets().isNullable();
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
