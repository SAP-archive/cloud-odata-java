package com.sap.core.odata.core.dispatcher;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.odata4j.exceptions.MethodNotAllowedException;

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

  public void setProcessor(ODataProcessor producer) {
    this.processor = producer;
  }

  public Response dispatch(ODataHttpMethod method, UriParserResult uriParserResult) throws ODataError {
    ODataResponse odataResponse;
    switch (uriParserResult.getUriType()) {
    case URI0: // service document
      switch (method) {
      case GET:
        odataResponse = this.processor.getServiceDocumentProcessor().readServiceDocument();
        break;
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;
    case URI1: // entity set
      switch (method) {
      case GET:
        odataResponse = this.processor.getEntitySetProcessor().readEntitySet();
        break;
      default:
        throw new MethodNotAllowedException("");
      }
      break;
    case URI2: // entity
    case URI3: // entity complex property
    case URI4: // entity simple property or entity simple property value
    case URI5: //
    case URI6A: // navigation property entity target multiplicity 0..1
    case URI6B: // navigation property entity target multiplicity *
    case URI7A: // entity link
    case URI7B: // entity links
    case URI8: // $metadata
      switch (method) {
      case GET:
        odataResponse = this.processor.getMetadataProcessor().readMetadata();
        break;
      default:
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;
    case URI9: // $batch
    case URI10: // function import
    case URI11: //
    case URI12: //
    case URI13: // 
    case URI14: // function import primitve
    case URI15: // entity set count
    case URI16: // entity exists
    case URI17: // media resource
    case URI50A: // entity link exists
    case URI50B: // entity links count
    default:
      throw new ODataTechnicalException("Unknown or non implemented URI type: " + uriParserResult.getUriType());
    }

    Response response = this.convertResponse(odataResponse);
    return response;
  }

  private Response convertResponse(ODataResponse odataResponse) {
    ResponseBuilder responseBuilder = Response.noContent();
    
    responseBuilder = responseBuilder.status(odataResponse.getStatus());
    responseBuilder = responseBuilder.entity(odataResponse.getEntity());
    
    for (String name : odataResponse.getHeaderNames()){
      responseBuilder = responseBuilder.header(name, odataResponse.getHeader(name));
    }
    
    return responseBuilder.build();
  }

}
