package com.sap.core.odata.core.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.rest.ODataLocator;
import com.sap.core.odata.api.uri.UriParserResult;
import com.sap.core.odata.core.dispatcher.Dispatcher;
import com.sap.core.odata.core.enums.ODataHttpMethod;
import com.sap.core.odata.core.uri.UriParserImpl;

public final class ODataLocatorImpl implements ODataLocator {

  private static final Logger log = LoggerFactory.getLogger(ODataLocatorImpl.class);

  private ODataProcessor processor;

  private Dispatcher dispatcher;

  private UriParserImpl uriParser;

  private ODataContextImpl context;

  private List<PathSegment> odataPathSegments;

  private HttpHeaders httpHeaders;

  private UriInfo uriInfo;

  @GET
  public Response handleGet() throws ODataError {
    try {
      ODataLocatorImpl.log.debug("+++ ODataSubResource:handleGet()");
      this.context.log();

      List<String> pathSegments = this.context.getPathSegmentsAsStrings();
      Map<String, String> queryParameters = this.convertToSinglevaluedMap(this.context.getUriInfo().getQueryParameters());
      
      UriParserResult uriParserResult = this.uriParser.parse(pathSegments, queryParameters);

      Response response = this.dispatcher.dispatch(ODataHttpMethod.GET, uriParserResult);
      return response;
    } catch (ODataException e) {
      throw new RuntimeException(e);
    }
  }

  @POST
  @Produces(MediaType.TEXT_PLAIN)
  public Response handlePost(
      @HeaderParam("X-HTTP-Method") String xmethod
      ) throws ODataError {

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

  @PUT
  public Response handlePut() throws ODataError {
    ODataLocatorImpl.log.debug("+++ ODataSubResource:handlePut()");
    this.context.log();

    return Response.ok().entity("PUT: status 200 ok").build();
  }

  @PATCH
  public Response handlePatch() throws ODataError {
    ODataLocatorImpl.log.debug("+++ ODataSubResource:handlePatch()");
    this.context.log();

    return Response.ok().entity("PATCH: status 200 ok").build();
  }

  @MERGE
  public Response handleMerge() throws ODataError {
    ODataLocatorImpl.log.debug("+++ ODataSubResource:handleMerge()");
    this.context.log();

    return Response.ok().entity("MERGE: status 200 ok").build();
  }

  @DELETE
  public Response handleDelete() throws ODataError {
    ODataLocatorImpl.log.debug("+++ ODataSubResource:handleDelete()");
    this.context.log();

    return Response.ok().entity("DELETE: status 200 ok").build();
  }

  public void setContext(ODataContextImpl context) {
    this.context = context;
  }

  @Override
  public void beforRequest() throws ODataError {

    this.context = new ODataContextImpl();
    this.context.setHttpHeaders(this.httpHeaders);
    this.context.setPathSegments(this.odataPathSegments);
    this.context.setUriInfo(this.uriInfo);
    this.uriParser = new UriParserImpl(this.processor.getMetadataProcessor().getEdm());
    this.dispatcher = new Dispatcher();
    this.dispatcher.setContext(this.context);
    this.dispatcher.setProcessor(this.processor);
    this.processor.setContext(this.context);
  }

  private Map<String, String> convertToSinglevaluedMap(MultivaluedMap<String, String> multi) {
    Map<String, String> single = new HashMap<String, String>();

    for (String key : multi.keySet()) {
      String value = multi.getFirst(key);
      single.put(key, value);
    }

    return single;
  }

  @Override
  public void setPathSegments(List<PathSegment> odataPathSegments) {
    this.odataPathSegments = odataPathSegments;
  }

  @Override
  public void setHttpHeaders(HttpHeaders httpHeaders) {
    this.httpHeaders = httpHeaders;
  }

  @Override
  public void setUriInfo(UriInfo uriInfo) {
    this.uriInfo = uriInfo;
  }

  @Override
  public void setProcessor(ODataProcessor processor) {
    this.processor = processor;
  }

}
