package com.sap.core.odata.core;

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

import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataHttpException;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.core.ep.ODataExceptionSerializer;
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
    String entity = ODataExceptionSerializer.serialize(buildErrorCode(webApplicationException), webApplicationException.getMessage(), getFormat(), DEFAULT_RESPONSE_LOCALE);
    return buildResponseInternal(entity, getFormat(), webApplicationException.getResponse().getStatus());
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
    Format format = getFormat();
    String entity = ODataExceptionSerializer.serialize(buildErrorCode(exception), exception.getMessage(), format, DEFAULT_RESPONSE_LOCALE);
    return buildResponseInternal(entity, format, Status.INTERNAL_SERVER_ERROR.getStatusCode());
  }

  private Response buildResponseForApplicationException(ODataApplicationException exception) {
    int status = extractStatus(exception);
    Format format = getFormat();
    String entity = ODataExceptionSerializer.serialize(exception.getCode(), exception.getMessage(), format, DEFAULT_RESPONSE_LOCALE);
    return buildResponseInternal(entity, format, status);
  }

  private Response buildResponseForHttpException(ODataHttpException msgException) {
    Message localizedMessage = extractEntity(msgException.getMessageReference());
    int status = extractStatus(msgException);
    Format format = getFormat();
    String entity = ODataExceptionSerializer.serialize(buildErrorCode(msgException), localizedMessage.getText(), format, localizedMessage.getLocale());
    return buildResponseInternal(entity, format, status);
  }

  private Response buildResponseInternal(String entity, Format format, int status) {
    ResponseBuilder responseBuilder = Response.noContent();

    switch (format) {
    case JSON:
      return responseBuilder.entity(entity).type(MediaType.APPLICATION_JSON_TYPE).status(status).build();
    case XML:
      return responseBuilder.entity(entity).type(MediaType.APPLICATION_XML_TYPE).status(status).build();
    default:
      return Response.status(Status.UNSUPPORTED_MEDIA_TYPE).build();
    }
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

  private Format getFormat() {
    List<MediaType> acceptableMediaTypes = httpHeaders.getAcceptableMediaTypes();

    for (MediaType type : acceptableMediaTypes) {
      if (type.isWildcardType() || MediaType.APPLICATION_ATOM_XML_TYPE.equals(type) || MediaType.APPLICATION_XML_TYPE.equals(type)) {
        return Format.XML;
      } else if (MediaType.APPLICATION_JSON_TYPE.equals(type)) {
        return Format.JSON;
      }
    }
    return Format.XML;
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

}
