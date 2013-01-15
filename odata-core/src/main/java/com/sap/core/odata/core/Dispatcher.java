package com.sap.core.odata.core;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMethodNotAllowedException;
import com.sap.core.odata.api.processor.ODataResponse;
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

  public ODataResponse dispatch(final ODataHttpMethod method, final UriInfoImpl uriInfo, final Object content, final String contentType) throws ODataException {
    switch (uriInfo.getUriType()) {
    case URI0:
      if (method == ODataHttpMethod.GET)
        return service.getServiceDocumentProcessor().readServiceDocument(uriInfo, contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI1:
    case URI6B:
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
          return service.getEntitySetProcessor().createEntity(uriInfo, content, contentType);
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
      case PATCH:
      case MERGE:
        if (uriInfo.getFormat() == null
            && uriInfo.getExpand().isEmpty()
            && uriInfo.getSelect().isEmpty())
          return service.getEntityProcessor().updateEntity(uriInfo, content, contentType);
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
      case PATCH:
      case MERGE:
        if (uriInfo.getFormat() == null)
          return service.getEntityComplexPropertyProcessor().updateEntityComplexProperty(uriInfo, content, contentType);
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
        if (isPropertyNotKey(getEntityType(uriInfo), getProperty(uriInfo)))
          if (uriInfo.isValue())
            return service.getEntitySimplePropertyValueProcessor().updateEntitySimplePropertyValue(uriInfo, content, contentType);
          else
            if (uriInfo.getFormat() == null)
              return service.getEntitySimplePropertyProcessor().updateEntitySimpleProperty(uriInfo, content, contentType);
            else
              throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      case DELETE:
        final EdmProperty property = getProperty(uriInfo);
        if (uriInfo.isValue()
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

    case URI7A:
      switch (method) {
      case GET:
        return service.getEntityLinkProcessor().readEntityLink(uriInfo, contentType);
      case PUT:
      case PATCH:
      case MERGE:
        if (uriInfo.getFormat() == null
            && uriInfo.getFilter() == null)
          return service.getEntityLinkProcessor().updateEntityLink(uriInfo, content, contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      case DELETE:
        if (uriInfo.getFormat() == null
            && uriInfo.getFilter() == null)
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
            && uriInfo.getTop() == null)
          return service.getEntityLinksProcessor().createEntityLink(uriInfo, content, contentType);
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
      if (uriInfo.getFunctionImport().getHttpMethod() == null
          || uriInfo.getFunctionImport().getHttpMethod().equals(method.toString()))
        return service.getFunctionImportProcessor().executeFunctionImport(uriInfo, contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI14:
      if (uriInfo.getFunctionImport().getHttpMethod() == null
          || uriInfo.getFunctionImport().getHttpMethod().equals(method.toString()))
        if (uriInfo.isValue())
          return service.getFunctionImportValueProcessor().executeFunctionImportValue(uriInfo, contentType);
        else
          return service.getFunctionImportProcessor().executeFunctionImport(uriInfo, contentType);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

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
            && uriInfo.getFilter() == null)
          return service.getEntityMediaProcessor().updateEntityMedia(uriInfo, content, contentType);
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      case DELETE:
        if (uriInfo.getFormat() == null
            && uriInfo.getFilter() == null)
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

  public Class<? extends ProcessorFeature> mapUriTypeToProcessorFeature(final UriInfoImpl uriInfo) {
    Class<? extends ProcessorFeature> feature;

    switch (uriInfo.getUriType()) {
    case URI0:
      feature = ServiceDocument.class;
      break;
    case URI1:
    case URI6B:
    case URI15:
      feature = EntitySet.class;
      break;
    case URI2:
    case URI6A:
    case URI16:
      feature = Entity.class;
      break;
    case URI3:
      feature = EntityComplexProperty.class;
      break;
    case URI4:
    case URI5:
      feature = uriInfo.isValue() ? EntitySimplePropertyValue.class : EntitySimpleProperty.class;
      break;
    case URI7A:
    case URI50A:
      feature = EntityLink.class;
      break;
    case URI7B:
    case URI50B:
      feature = EntityLinks.class;
      break;
    case URI8:
      feature = Metadata.class;
      break;
    case URI9:
      feature = Batch.class;
      break;
    case URI10:
    case URI11:
    case URI12:
    case URI13:
      feature = FunctionImport.class;
      break;
    case URI14:
      feature = uriInfo.isValue() ? FunctionImportValue.class : FunctionImport.class;
      break;
    case URI17:
      feature = EntityMedia.class;
      break;
    default:
      throw new ODataRuntimeException("Unknown or not implemented URI type: " + uriInfo.getUriType());
    }

    return feature;
  }
}
