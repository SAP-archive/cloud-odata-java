package com.sap.core.odata.core;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataHttpException;
import com.sap.core.odata.core.exception.MessageService;
import com.sap.core.odata.core.exception.MessageService.Message;

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

    Exception toHandleException = extractException(exception);

    if (toHandleException instanceof ODataApplicationException) {
      response = buildResponseForApplicationException((ODataApplicationException) toHandleException);
    } else if (toHandleException instanceof ODataHttpException) {
      response = buildResponseForHttpException((ODataHttpException) toHandleException);
    } else {
      response = buildResponseForException(exception);
    }

    if (isInternalServerError(response)) {
      ODataExceptionMapperImpl.LOG.error(exception.getMessage(), exception);
    }

    return response;
  }

  private boolean isInternalServerError(final Response response) {
    return response.getStatus() >= Status.INTERNAL_SERVER_ERROR.getStatusCode();
  }

  private Exception extractException(Exception exception) {
    if (exception instanceof ODataException) {
      //
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
    ResponseBuilder responseBuilder = Response.noContent();
    return responseBuilder.entity(exception.getClass().getName() + " - " + exception.getMessage())
        .status(Status.INTERNAL_SERVER_ERROR).build();
  }

  private Response buildResponseForApplicationException(ODataApplicationException exception) {
    ResponseBuilder responseBuilder = Response.noContent();
    Status status = extractStatus(exception);
    return responseBuilder.entity(exception.getMessage()).status(status).build();
  }

  private Response buildResponseForHttpException(ODataHttpException msgException) {
    Message localizedMessage = extractEntity(msgException.getMessageReference());
    ResponseBuilder responseBuilder = Response.noContent();
    return responseBuilder.entity("Language = '" + localizedMessage.getLang() + "', message = '" + localizedMessage.getText() + "'.")
        .status(extractStatus(msgException)).build();
  }

  private Status extractStatus(ODataException exception) {
    Status extractedStatus = Status.INTERNAL_SERVER_ERROR;

    HttpStatusCodes httpStatus = null;
    if (exception instanceof ODataHttpException) {
      httpStatus = ((ODataHttpException) exception).getHttpStatus();
    } else if (exception instanceof ODataApplicationException) {
      httpStatus = ((ODataApplicationException) exception).getHttpStatus();
    }

    if (httpStatus != null) {
      try {
        extractedStatus = Status.valueOf(httpStatus.name());
      } catch (IllegalArgumentException e) {
        // no mapping found -> INTERNAL_SERVER_ERROR
        LOG.error("no mapping found -> INTERNAL_SERVER_ERROR", e);
      }
    }

    return extractedStatus;
  }

  private Message extractEntity(com.sap.core.odata.api.exception.MessageReference context) {
    List<Locale> locales = getLanguages();
    Locale locale = MessageService.getSupportedLocale(locales);
    return MessageService.getMessage(locale, context);
  }

  /**
   * 
   * @return
   */
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

}
