package com.sap.core.odata.core.dispatcher;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.exception.ODataMethodNotAllowedException;
import com.sap.core.odata.api.exception.ODataTechnicalException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.rest.ODataContext;
import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.core.enums.ODataHttpMethod;
import com.sap.core.odata.core.rest.ODataContextImpl;
import com.sap.core.odata.core.uri.UriParserResultImpl;

public class Dispatcher {

  private ODataProcessor processor;
  private ODataContext context;

  public void setContext(ODataContextImpl context) {
    this.context = context;
  }

  public void setProcessor(ODataProcessor processor) {
    this.processor = processor;
  }

  public ODataResponse dispatch(final ODataHttpMethod method, final UriParserResultImpl uriParserResult) throws ODataError {
    switch (uriParserResult.getUriType()) {
    case URI0:
      if (method == ODataHttpMethod.GET)
        return processor.getServiceDocumentProcessor().readServiceDocument(uriParserResult);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI1:
    case URI6B:
      switch (method) {
      case GET:
        return processor.getEntitySetProcessor().readEntitySet(uriParserResult);
      case POST:
        return processor.getEntitySetProcessor().createEntity();
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI2:
    case URI6A:
      switch (method) {
      case GET:
        return processor.getEntityProcessor().readEntity(uriParserResult);
      case PUT:
      case PATCH:
      case MERGE:
        return processor.getEntityProcessor().updateEntity();
      case DELETE:
        return processor.getEntityProcessor().deleteEntity();
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI3:
      switch (method) {
      case GET:
        return processor.getEntityComplexPropertyProcessor().readEntityComplexProperty(uriParserResult);
      case PUT:
      case PATCH:
      case MERGE:
        return processor.getEntityComplexPropertyProcessor().updateEntityComplexProperty();
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI4:
    case URI5:
      switch (method) {
      case GET:
        if (uriParserResult.isValue())
          return processor.getEntitySimplePropertyValueProcessor().readEntitySimplePropertyValue(uriParserResult);
        else
          return processor.getEntitySimplePropertyProcessor().readEntitySimpleProperty(uriParserResult);
      case PUT:
      case PATCH:
      case MERGE:
        if (uriParserResult.isValue())
          return processor.getEntitySimplePropertyValueProcessor().updateEntitySimplePropertyValue();
        else
          return processor.getEntitySimplePropertyProcessor().updateEntitySimpleProperty();
      case DELETE:
        if (uriParserResult.isValue())
          return processor.getEntitySimplePropertyValueProcessor().deleteEntitySimplePropertyValue();
        else
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI7A:
      switch (method) {
      case GET:
        return processor.getEntityLinkProcessor().readEntityLink(uriParserResult);
      case PUT:
      case PATCH:
      case MERGE:
        return processor.getEntityLinkProcessor().updateEntityLink();
      case DELETE:
        return processor.getEntityLinkProcessor().deleteEntityLink();
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI7B:
      switch (method) {
      case GET:
        return processor.getEntityLinksProcessor().readEntityLinks(uriParserResult);
      case POST:
        return processor.getEntityLinksProcessor().createEntityLink();
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI8:
      if (method == ODataHttpMethod.GET)
        return processor.getMetadataProcessor().readMetadata(uriParserResult);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI9:
      if (method == ODataHttpMethod.POST)
        return processor.getBatchProcessor().executeBatch();
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI10:
    case URI11:
    case URI12:
    case URI13:
      return processor.getFunctionImportProcessor().executeFunctionImport(uriParserResult);

    case URI14:
      if (uriParserResult.isValue())
        return processor.getFunctionImportValueProcessor().executeFunctionImportValue(uriParserResult);
      else
        return processor.getFunctionImportProcessor().executeFunctionImport(uriParserResult);

    case URI15:
      if (method == ODataHttpMethod.GET)
        return processor.getEntitySetProcessor().countEntitySet(uriParserResult);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI16:
      if (method == ODataHttpMethod.GET)
        return processor.getEntityProcessor().existsEntity(uriParserResult);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI17:
      switch (method) {
      case GET:
        return processor.getEntityMediaProcessor().readEntityMedia(uriParserResult);
      case PUT:
        return processor.getEntityMediaProcessor().updateEntityMedia();
      case DELETE:
        return processor.getEntityMediaProcessor().deleteEntityMedia();
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI50A:
      if (method == ODataHttpMethod.GET)
        return processor.getEntityLinkProcessor().existsEntityLink(uriParserResult);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI50B:
      if (method == ODataHttpMethod.GET)
        return processor.getEntityLinksProcessor().countEntityLinks(uriParserResult);
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    default:
      throw new ODataTechnicalException("Unknown or not implemented URI type: " + uriParserResult.getUriType());
    }
  }
}
