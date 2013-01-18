package com.sap.core.odata.core.rest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataHttpException;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.ContentType.ODataFormat;
import com.sap.core.odata.core.ep.ODataExceptionSerializer;
import com.sap.core.odata.core.exception.MessageService;
import com.sap.core.odata.core.exception.MessageService.Message;

/**
 * @author SAP AG
 */
@Provider
public class ODataExceptionMapperImpl implements ExceptionMapper<Exception> {

  private final static Logger LOG = LoggerFactory.getLogger(ODataExceptionMapperImpl.class);
  private static final Locale DEFAULT_RESPONSE_LOCALE = Locale.ENGLISH;

  @Context
  UriInfo uriInfo;
  @Context
  HttpHeaders httpHeaders;

  @Override
  public Response toResponse(Exception exception) {

    final Response response;

    final Exception toHandleException = extractException(exception);

    if (toHandleException instanceof ODataApplicationException) {
      response = buildResponseForApplicationException((ODataApplicationException) toHandleException);
    } else if (toHandleException instanceof ODataHttpException) {
      response = buildResponseForHttpException((ODataHttpException) toHandleException);
    } else if (toHandleException instanceof WebApplicationException) {
      response = buildResponseForWebApplicationException((WebApplicationException) toHandleException);
    } else {
      response = buildResponseForException(exception);
    }

    if (isInternalServerError(response)) {
      LOG.error(exception.getMessage(), exception);
    }

    return response;
  }

  private Response buildResponseForWebApplicationException(WebApplicationException webApplicationException) {
    InputStream entity = ODataExceptionSerializer.serialize(buildErrorCode(webApplicationException), webApplicationException.getMessage(), getInnerError(webApplicationException), getContentType(), DEFAULT_RESPONSE_LOCALE);
    return buildResponseInternal(entity, getContentType(), webApplicationException.getResponse().getStatus());
  }

  private boolean isInternalServerError(final Response response) {
    return response.getStatus() >= Status.INTERNAL_SERVER_ERROR.getStatusCode();
  }

  private Exception extractException(Exception exception) {
    if (exception instanceof ODataException) {

      ODataException odataException = (ODataException) exception;
      if (odataException.isCausedByApplicationException()) {
        return odataException.getApplicationExceptionCause();
      } else if (odataException.isCausedByHttpException()) {
        return odataException.getHttpExceptionCause();
      } else if (odataException.isCausedByMessageException()) {
        return odataException.getMessageExceptionCause();
      }
    }
    return exception;
  }

  private Response buildResponseForException(Exception exception) {
    final String innerError = getInnerError(exception);
    final ContentType contentType = getContentType();
    InputStream entity = ODataExceptionSerializer.serialize(buildErrorCode(exception), exception.getMessage(), innerError, contentType, DEFAULT_RESPONSE_LOCALE);
    return buildResponseInternal(entity, contentType, Status.INTERNAL_SERVER_ERROR.getStatusCode());
  }

  private Response buildResponseForApplicationException(ODataApplicationException exception) {
    final int status = extractStatus(exception);
    final String innerError = getInnerError(exception);
    final ContentType contentType = getContentType();
    InputStream entity = ODataExceptionSerializer.serialize(exception.getCode(), exception.getMessage(), innerError, contentType, exception.getLocale());
    return buildResponseInternal(entity, contentType, status);
  }

  private Response buildResponseForHttpException(ODataHttpException msgException) {
    final int status = extractStatus(msgException);
    final Message localizedMessage = extractEntity(msgException.getMessageReference());
    final String innerError = getInnerError(msgException);
    final ContentType contentType = getContentType();
    InputStream entity = ODataExceptionSerializer.serialize(buildErrorCode(msgException), localizedMessage.getText(), innerError, contentType, localizedMessage.getLocale());
    return buildResponseInternal(entity, contentType, status);
  }

  private Response buildResponseInternal(InputStream entity, ContentType contentType, int status) {
    ResponseBuilder responseBuilder = Response.status(status).entity(entity).header(ODataHttpHeaders.DATASERVICEVERSION, ODataServiceVersion.V10);

    if (contentType.getODataFormat() == ODataFormat.JSON)
      return responseBuilder.type(MediaType.APPLICATION_JSON_TYPE).build();
    else
      return responseBuilder.type(MediaType.APPLICATION_XML_TYPE).build();
  }

  private int extractStatus(ODataException exception) {
    int extractedStatusCode = HttpStatusCodes.INTERNAL_SERVER_ERROR.getStatusCode();

    if (exception instanceof ODataHttpException) {
      extractedStatusCode = ((ODataHttpException) exception).getHttpStatus().getStatusCode();
    } else if (exception instanceof ODataApplicationException) {
      extractedStatusCode = ((ODataApplicationException) exception).getHttpStatus().getStatusCode();
    }

    return extractedStatusCode;
  }

  private Message extractEntity(MessageReference context) {
    final Locale locale = MessageService.getSupportedLocale(getLanguages());
    return MessageService.getMessage(locale, context);
  }

  private List<Locale> getLanguages() {
    try {
      if (httpHeaders.getAcceptableLanguages().isEmpty()) {
        return Arrays.asList(DEFAULT_RESPONSE_LOCALE);
      }
      return httpHeaders.getAcceptableLanguages();
    } catch (WebApplicationException e) {
      if (e.getCause() != null && e.getCause().getClass() == ParseException.class) {
        // invalid accept-language string in http header
        // compensate exception with using default locale
        return Arrays.asList(DEFAULT_RESPONSE_LOCALE);
      }
      // not able to compensate exception -> re-throw
      throw e;
    }
  }

  private ContentType getContentType() {
    for (MediaType type : httpHeaders.getAcceptableMediaTypes()) {
      if (type.isWildcardType() || MediaType.APPLICATION_ATOM_XML_TYPE.equals(type) || MediaType.APPLICATION_XML_TYPE.equals(type)) {
        return ContentType.APPLICATION_XML;
      } else if (MediaType.APPLICATION_JSON_TYPE.equals(type)) {
        return ContentType.APPLICATION_JSON;
      }
    }
    return ContentType.APPLICATION_XML;
  }

  private String buildErrorCode(Exception e) {
    String errorCode;

    if (e instanceof ODataMessageException) {
      ODataMessageException msgException = (ODataMessageException) e;
      errorCode = msgException.getMessageReference().getKey();
    } else {
      errorCode = e.getClass().getName();
    }
    return errorCode;
  }

  private String getInnerError(final Exception exception) {
    if (uriInfo.getQueryParameters().containsKey("odata.debug")) {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      PrintWriter writer = new PrintWriter(stream);
      exception.printStackTrace(writer);
      writer.close();
      return stream.toString();
    } else {
      return null;
    }
  }

}
