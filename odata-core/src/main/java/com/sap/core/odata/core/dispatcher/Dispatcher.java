package com.sap.core.odata.core.dispatcher;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMethodNotAllowedException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.service.ODataService;
import com.sap.core.odata.core.enums.ODataHttpMethod;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.core.uri.UriParserResultImpl;

public class Dispatcher {

  public Dispatcher(ODataService service) {
    this.service = service;
  }

  private ODataService service;

  public ODataResponse dispatch(final ODataHttpMethod method, final UriParserResultImpl uriParserResult) throws ODataException {
    switch (uriParserResult.getUriType()) {
    case URI0:
      if (method == ODataHttpMethod.GET)
        return this.service.getServiceDocumentProcessor().readServiceDocument(uriParserResult);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI1:
    case URI6B:
      switch (method) {
      case GET:
        return this.service.getEntitySetProcessor().readEntitySet(uriParserResult);
      case POST:
        return this.service.getEntitySetProcessor().createEntity();
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI2:
    case URI6A:
      switch (method) {
      case GET:
        return this.service.getEntityProcessor().readEntity(uriParserResult);
      case PUT:
      case PATCH:
      case MERGE:
        return this.service.getEntityProcessor().updateEntity();
      case DELETE:
        return this.service.getEntityProcessor().deleteEntity();
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI3:
      switch (method) {
      case GET:
        return this.service.getEntityComplexPropertyProcessor().readEntityComplexProperty(uriParserResult);
      case PUT:
      case PATCH:
      case MERGE:
        return this.service.getEntityComplexPropertyProcessor().updateEntityComplexProperty();
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI4:
    case URI5:
      switch (method) {
      case GET:
        if (uriParserResult.isValue())
          return this.service.getEntitySimplePropertyValueProcessor().readEntitySimplePropertyValue(uriParserResult);
        else
          return this.service.getEntitySimplePropertyProcessor().readEntitySimpleProperty(uriParserResult);
      case PUT:
      case PATCH:
      case MERGE:
        if (uriParserResult.isValue())
          return this.service.getEntitySimplePropertyValueProcessor().updateEntitySimplePropertyValue();
        else
          return this.service.getEntitySimplePropertyProcessor().updateEntitySimpleProperty();
      case DELETE:
        if (uriParserResult.isValue())
          return this.service.getEntitySimplePropertyValueProcessor().deleteEntitySimplePropertyValue();
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI7A:
      switch (method) {
      case GET:
        return this.service.getEntityLinkProcessor().readEntityLink(uriParserResult);
      case PUT:
      case PATCH:
      case MERGE:
        return this.service.getEntityLinkProcessor().updateEntityLink();
      case DELETE:
        return this.service.getEntityLinkProcessor().deleteEntityLink();
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI7B:
      switch (method) {
      case GET:
        return this.service.getEntityLinksProcessor().readEntityLinks(uriParserResult);
      case POST:
        return this.service.getEntityLinksProcessor().createEntityLink();
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI8:
      if (method == ODataHttpMethod.GET)
        return this.service.getMetadataProcessor().readMetadata(uriParserResult);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI9:
      if (method == ODataHttpMethod.POST)
        return this.service.getBatchProcessor().executeBatch();
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI10:
    case URI11:
    case URI12:
    case URI13:
      return this.service.getFunctionImportProcessor().executeFunctionImport(uriParserResult);

    case URI14:
      if (uriParserResult.isValue())
        return this.service.getFunctionImportValueProcessor().executeFunctionImportValue(uriParserResult);
      else
        return this.service.getFunctionImportProcessor().executeFunctionImport(uriParserResult);

    case URI15:
      if (method == ODataHttpMethod.GET)
        return this.service.getEntitySetProcessor().countEntitySet(uriParserResult);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI16:
      if (method == ODataHttpMethod.GET)
        return this.service.getEntityProcessor().existsEntity(uriParserResult);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI17:
      switch (method) {
      case GET:
        return this.service.getEntityMediaProcessor().readEntityMedia(uriParserResult);
      case PUT:
        return this.service.getEntityMediaProcessor().updateEntityMedia();
      case DELETE:
        return this.service.getEntityMediaProcessor().deleteEntityMedia();
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI50A:
      if (method == ODataHttpMethod.GET)
        return this.service.getEntityLinkProcessor().existsEntityLink(uriParserResult);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI50B:
      if (method == ODataHttpMethod.GET)
        return this.service.getEntityLinksProcessor().countEntityLinks(uriParserResult);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    default:
      throw new ODataRuntimeException("Unknown or not implemented URI type: " + uriParserResult.getUriType());
    }
  }
}
