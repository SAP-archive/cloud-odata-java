package com.sap.core.odata.core.processor;

import javax.ws.rs.core.Response;

import org.odata4j.exceptions.MethodNotAllowedException;

import com.sap.core.odata.core.producer.ODataProducer;
import com.sap.core.odata.core.rest.ODataContext;
import com.sap.core.odata.core.rest.impl.ODataContextImpl;
import com.sap.core.odata.core.rest.impl.ODataHttpMethod;
import com.sap.core.odata.core.uri.UriParserResult;

public class Processor {

  private ODataProducer producer;
  private ODataContext context;

  public void setContext(ODataContextImpl context) {
    this.context = context;
  }

  public void setProducer(ODataProducer producer) {
    this.producer = producer;
  }

  public Response dispatch(ODataHttpMethod method, UriParserResult uriParserResult) {
    Response response;

    switch (uriParserResult.getUriType()) {
    case URI0: // service document
      switch (method) {
      case GET:
        response = this.producer.getServiceDocument().read();
        break;
      default:
        throw new MethodNotAllowedException("");
      }
      break;
    case URI1: // entity set
      switch (method) {
      case GET:
        response = this.producer.getEntitySet().read();
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
        response = this.producer.getMetadata().read();
        break;
      default:
        throw new MethodNotAllowedException("");
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
      throw new RuntimeException("Unknown or non implemented URI type: " + uriParserResult.getUriType());
    }

    return response;
  }

}