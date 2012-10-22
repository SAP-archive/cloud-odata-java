package com.sap.core.odata.core.rest.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.core.processor.Processor;
import com.sap.core.odata.core.rest.ODataLocator;
import com.sap.core.odata.core.uri.UriParser;
import com.sap.core.odata.core.uri.UriParserResult;

public final class ODataLocatorImpl implements ODataLocator {

  private static final Logger log = LoggerFactory.getLogger(ODataLocatorImpl.class);

  private ODataContextImpl context;

  private ODataProcessor producer;

  private Processor processor;

  private UriParser uriParser;

  @Override
  public ODataProcessor getProcessor() {
    return this.producer;
  }

  @Override
  public Response handleGet()  throws ODataError{
    try {
      ODataLocatorImpl.log.debug("+++ ODataSubResource:handleGet()");
      this.context.log();

      List<PathSegment> pathSegments = this.context.getPathSegments();
      Map<String, String> queryParameters = this.convertToSinglevaluedMap(this.context.getUriInfo().getQueryParameters());
      UriParserResult uriParserResult = this.uriParser.parse(pathSegments, queryParameters);

      Response response = this.processor.dispatch(ODataHttpMethod.GET, uriParserResult);
      return response;
    } catch (ODataException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  @POST
  @Produces(MediaType.TEXT_PLAIN)
  public Response handlePost(
      @HeaderParam("X-HTTP-Method") String xmethod
      )  throws ODataError{

    ODataLocatorImpl.log.debug("+++ ODataSubResource:handlePost()");
    Response response;

    /* tunneling */
    if (xmethod == null) {
      this.context.log();
      response = Response.ok().entity("POST: status 200 ok").build();
    } else if ("MERGE".equals(xmethod)) {
      response = this.handleMerge();
    } else if ("PATCH".equals(xmethod)) {
      response = this.handlePatch();
    } else {
      response = Response.status(405).build(); // method not allowed!
    }

    return response;
  }

  @Override
  public Response handlePut() throws ODataError {
    ODataLocatorImpl.log.debug("+++ ODataSubResource:handlePut()");
    this.context.log();

    return Response.ok().entity("PUT: status 200 ok").build();
  }

  @Override
  public Response handlePatch() throws ODataError {
    ODataLocatorImpl.log.debug("+++ ODataSubResource:handlePatch()");
    this.context.log();

    return Response.ok().entity("PATCH: status 200 ok").build();
  }

  @Override
  public Response handleMerge() throws ODataError {
    ODataLocatorImpl.log.debug("+++ ODataSubResource:handleMerge()");
    this.context.log();

    return Response.ok().entity("MERGE: status 200 ok").build();
  }

  @Override
  public Response handleDelete() throws ODataError {
    ODataLocatorImpl.log.debug("+++ ODataSubResource:handleDelete()");
    this.context.log();

    return Response.ok().entity("DELETE: status 200 ok").build();
  }

  public void setContext(ODataContextImpl context) {
    this.context = context;
  }

  public void setProducer(ODataSingleProcessor producer) {
    this.producer = producer;
  }

  @Override
  public void beforRequest() throws ODataError {

//    this.uriParser = new UriParser(this.producer.getEdm());
    this.processor = new Processor();
    
    this.processor.setContext(this.context);
    this.processor.setProducer(this.producer);
  }
  
  private Map<String, String> convertToSinglevaluedMap(MultivaluedMap<String, String> multi) {
    Map<String, String> single = new HashMap<String, String>();

    for (String key : multi.keySet()) {
      String value = multi.getFirst(key);
      single.put(key, value);
    }

    return single;
  }

}
