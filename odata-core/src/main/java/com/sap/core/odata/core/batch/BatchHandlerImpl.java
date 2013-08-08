package com.sap.core.odata.core.batch;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.batch.BatchHandler;
import com.sap.core.odata.api.batch.BatchRequestPart;
import com.sap.core.odata.api.batch.BatchResponsePart;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.core.ODataContextImpl;
import com.sap.core.odata.core.ODataPathSegmentImpl;
import com.sap.core.odata.core.ODataRequestHandler;
import com.sap.core.odata.core.PathInfoImpl;

public class BatchHandlerImpl implements BatchHandler {
  private static final int BAD_REQUEST = 400;
  private ODataServiceFactory factory;
  private ODataService service;
  private Map<String, String> contentIdMap;

  public BatchHandlerImpl(final ODataServiceFactory factory, final ODataService service) {
    this.factory = factory;
    this.service = service;
  }

  @Override
  public BatchResponsePart handleBatchPart(final BatchRequestPart batchPart) throws ODataException {
    if (batchPart.isChangeSet()) {
      List<ODataRequest> changeSetRequests = batchPart.getRequests();
      contentIdMap = new HashMap<String, String>();
      return service.getBatchProcessor().executeChangeSet(this, changeSetRequests);
    } else {
      if (batchPart.getRequests().size() != 1) {
        throw new ODataException("Query Operation should contain one request");
      }
      ODataRequest request = batchPart.getRequests().get(0);
      ODataRequestHandler handler = createHandler(request);
      String mimeHeaderContentId = request.getRequestHeaderValue(BatchConstants.MIME_HEADER_CONTENT_ID.toLowerCase(Locale.ENGLISH));
      String requestHeaderContentId = request.getRequestHeaderValue(BatchConstants.REQUEST_HEADER_CONTENT_ID.toLowerCase(Locale.ENGLISH));
      ODataResponse response = setContentIdHeader(handler.handle(request), mimeHeaderContentId, requestHeaderContentId);
      List<ODataResponse> responses = new ArrayList<ODataResponse>(1);
      responses.add(response);
      return BatchResponsePart.responses(responses).changeSet(false).build();
    }
  }

  @Override
  public ODataResponse handleRequest(final ODataRequest suppliedRequest) throws ODataException {
    ODataRequest request;
    String mimeHeaderContentId = suppliedRequest.getRequestHeaderValue(BatchConstants.MIME_HEADER_CONTENT_ID.toLowerCase(Locale.ENGLISH));
    String requestHeaderContentId = suppliedRequest.getRequestHeaderValue(BatchConstants.REQUEST_HEADER_CONTENT_ID.toLowerCase(Locale.ENGLISH));

    List<PathSegment> odataSegments = suppliedRequest.getPathInfo().getODataSegments();
    if (!odataSegments.isEmpty() && odataSegments.get(0).getPath().matches("\\$.*")) {
      request = modifyRequest(suppliedRequest, odataSegments);
    } else {
      request = suppliedRequest;
    }
    ODataRequestHandler handler = createHandler(request);
    ODataResponse response = handler.handle(request);
    if (response.getStatus().getStatusCode() < BAD_REQUEST) {
      response = setContentIdHeader(response, mimeHeaderContentId, requestHeaderContentId);
    }
    if (request.getMethod().equals(ODataHttpMethod.POST)) {
      String baseUri = getBaseUri(request);
      if (mimeHeaderContentId != null) {
        fillContentIdMap(response, mimeHeaderContentId, baseUri);
      } else if (requestHeaderContentId != null) {
        fillContentIdMap(response, requestHeaderContentId, baseUri);
      }
    }
    return response;
  }

  private void fillContentIdMap(final ODataResponse response, final String contentId, final String baseUri) {
    String location = response.getHeader(HttpHeaders.LOCATION);
    String relLocation = location.replace(baseUri + "/", "");
    contentIdMap.put("$" + contentId, relLocation);
  }

  private ODataRequest modifyRequest(final ODataRequest request, final List<PathSegment> odataSegments) throws ODataException {
    String contentId = contentIdMap.get(odataSegments.get(0).getPath());
    PathInfoImpl pathInfo = new PathInfoImpl();
    try {
      List<PathSegment> modifiedODataSegments = new ArrayList<PathSegment>();
      String[] segments = contentId.split("/");
      for (String segment : segments) {
        modifiedODataSegments.add(new ODataPathSegmentImpl(segment, null));
      }
      String newRequestUri = getBaseUri(request);
      newRequestUri += "/" + contentId;
      for (int i = 1; i < odataSegments.size(); i++) {
        newRequestUri += "/" + odataSegments.get(i).getPath();
        modifiedODataSegments.add(odataSegments.get(i));
      }
      for (Map.Entry<String, String> entry : request.getQueryParameters().entrySet()) {
        newRequestUri += "/" + entry;
      }

      pathInfo.setServiceRoot(request.getPathInfo().getServiceRoot());
      pathInfo.setPrecedingPathSegment(request.getPathInfo().getPrecedingSegments());
      pathInfo.setRequestUri(new URI(newRequestUri));
      pathInfo.setODataPathSegment(modifiedODataSegments);
    } catch (URISyntaxException e) {
      throw new ODataException(e);
    }
    ODataRequest modifiedRequest = ODataRequest.fromRequest(request).pathInfo(pathInfo).build();
    return modifiedRequest;
  }

  private ODataResponse setContentIdHeader(final ODataResponse response, final String mimeHeaderContentId, final String requestHeaderContentId) {
    ODataResponse modifiedResponse;
    if (requestHeaderContentId != null && mimeHeaderContentId != null) {
      modifiedResponse = ODataResponse.fromResponse(response).header(BatchConstants.REQUEST_HEADER_CONTENT_ID, requestHeaderContentId)
          .header(BatchConstants.MIME_HEADER_CONTENT_ID, mimeHeaderContentId).build();
    } else if (requestHeaderContentId != null) {
      modifiedResponse = ODataResponse.fromResponse(response).header(BatchConstants.REQUEST_HEADER_CONTENT_ID, requestHeaderContentId).build();
    } else if (mimeHeaderContentId != null) {
      modifiedResponse = ODataResponse.fromResponse(response).header(BatchConstants.MIME_HEADER_CONTENT_ID, mimeHeaderContentId).build();
    } else {
      return response;
    }
    return modifiedResponse;
  }

  private String getBaseUri(final ODataRequest request) {
    String baseUri = request.getPathInfo().getServiceRoot().toASCIIString();
    if (baseUri.endsWith("/")) {
      baseUri = baseUri.substring(0, baseUri.length() - 1);
    }
    for (PathSegment segment : request.getPathInfo().getPrecedingSegments()) {
      baseUri += "/" + segment.getPath();
    }
    return baseUri;
  }

  private ODataRequestHandler createHandler(final ODataRequest request) throws ODataException {
    ODataContextImpl context = new ODataContextImpl(request, factory);
    ODataContext parentContext = service.getProcessor().getContext();
    context.setBatchParentContext(parentContext);
    context.setService(service);
    service.getProcessor().setContext(context);
    return new ODataRequestHandler(factory, service, context);
  }

}
