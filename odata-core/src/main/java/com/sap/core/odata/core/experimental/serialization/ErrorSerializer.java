package com.sap.core.odata.core.experimental.serialization;

import java.io.StringWriter;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import org.odata4j.core.ODataConstants;
import org.odata4j.core.OErrors;
import org.odata4j.format.FormatWriter;
import org.odata4j.format.FormatWriterFactory;
import org.odata4j.producer.ErrorResponse;
import org.odata4j.producer.Responses;

import com.sap.core.odata.api.rest.ODataResponse;

public class ErrorSerializer {

  private UriInfo uriInfo;
  private HttpHeaders httpHeaders;

  public ErrorSerializer(UriInfo uriInfo, HttpHeaders httpHeaders) {
    super();
    this.uriInfo = uriInfo;
    this.httpHeaders = httpHeaders;
  }

  public ODataResponse serialize(int status, Exception e) {
    FormatWriter<ErrorResponse> fw = FormatWriterFactory.getFormatWriter(ErrorResponse.class, httpHeaders.getAcceptableMediaTypes(),
        uriInfo.getQueryParameters().getFirst("$format"), null);
    StringWriter sw = new StringWriter();
    ErrorResponse response = Responses.error(OErrors.error(Integer.toString(status), e.getMessage(), null));
    fw.write(uriInfo, sw, response);

    ODataResponse odataResponse = ODataResponse
        .status(status)
        .entity(sw.toString())
        .header(ODataConstants.Headers.DATA_SERVICE_VERSION, ODataConstants.DATA_SERVICE_VERSION_HEADER)
        .header(ODataConstants.Headers.CONTENT_TYPE, fw.getContentType())
        .build();
    
    return odataResponse;
  }

}
