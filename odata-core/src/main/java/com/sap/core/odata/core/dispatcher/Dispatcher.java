package com.sap.core.odata.core.dispatcher;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.exception.ODataMethodNotAllowedException;
import com.sap.core.odata.api.exception.ODataTechnicalException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.rest.ODataContext;
import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.api.uri.UriParserResult;
import com.sap.core.odata.core.enums.ODataHttpMethod;
import com.sap.core.odata.core.rest.ODataContextImpl;

public class Dispatcher {

  private ODataProcessor processor;
  private ODataContext context;

  public void setContext(ODataContextImpl context) {
    this.context = context;
  }

  public void setProcessor(ODataProcessor processor) {
    this.processor = processor;
  }

  public ODataResponse dispatch(final ODataHttpMethod method, final UriParserResult uriParserResult) throws ODataError {
    switch (uriParserResult.getUriType()) {
    case URI0:
      if (method == ODataHttpMethod.GET)
        return processor.getServiceDocumentProcessor().readServiceDocument();
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI1:
    case URI6B:
      switch (method) {
      case GET:
        return processor.getEntitySetProcessor().readEntitySet();
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }

    case URI2:
    case URI6A:
      return null;

    case URI3:
      return null;

    case URI4:
    case URI5:
      return null;

    case URI7A:
      return null;

    case URI7B:
      return null;

    case URI8:
      if (method == ODataHttpMethod.GET)
        return this.processor.getMetadataProcessor().readMetadata();
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI9:
      return null;

    case URI10:
    case URI11:
    case URI12:
    case URI13:
      return null;

    case URI14:
      return null;

    case URI15:
      return null;

    case URI16:
      return null;

    case URI17:
      return null;

    case URI50A:
      if (method == ODataHttpMethod.GET)
        return processor.getEntityLinkProcessor().existsEntityLink();
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    case URI50B:
      if (method == ODataHttpMethod.GET)
        return processor.getEntityLinksProcessor().countEntityLinks();
      else
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);

    default:
      throw new ODataTechnicalException("Unknown or not implemented URI type: " + uriParserResult.getUriType());
    }
  }
}
