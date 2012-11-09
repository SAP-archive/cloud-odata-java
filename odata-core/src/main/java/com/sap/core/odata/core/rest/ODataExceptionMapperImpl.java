package com.sap.core.odata.core.rest;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.core.exception.MessageService;
import com.sap.core.odata.core.exception.MessageService.Message;

@Provider
public class ODataExceptionMapperImpl implements ExceptionMapper<Exception> {

  private final static Logger log = LoggerFactory.getLogger(ODataExceptionMapperImpl.class);
  private static final Locale DEFAULT_RESPONSE_LOCALE = Locale.ENGLISH;

  @Context
  protected UriInfo uriInfo;
  @Context
  protected HttpHeaders httpHeaders;

  @Override
  public Response toResponse(Exception exception) {

    final String entity;
    final Status status;

    if(exception instanceof ODataMessageException) {
      ODataMessageException msgException = (ODataMessageException) exception;
      Message localizedMessage = extractEntity(msgException.getContext());
      entity = "Language = '" + localizedMessage.getLang() + "', message = '" + localizedMessage.getText() + "'.";
      status = extractStatus(msgException.getContext());
    } else {
      ODataExceptionMapperImpl.log.error(exception.getMessage(), exception);
      entity = exception.getClass().getCanonicalName() + " - " + exception.getMessage();
      status = Status.INTERNAL_SERVER_ERROR;
    }

    return Response.status(status).entity(entity).build();
  }

  private Status extractStatus(com.sap.core.odata.api.exception.MessageReference context) {
    Status extractedStatus = Status.INTERNAL_SERVER_ERROR;
    if(context.getHttpStatus() != null) {
      try {
        extractedStatus = Status.valueOf(context.getHttpStatus().name());
      } catch (IllegalArgumentException e) {
        // no mapping found -> INTERNAL_SERVER_ERROR
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
      if(httpHeaders.getAcceptableLanguages().isEmpty()) {
        return Arrays.asList(DEFAULT_RESPONSE_LOCALE);
      }
      return httpHeaders.getAcceptableLanguages();
    } catch (WebApplicationException e) {
      if(e.getCause() != null && e.getCause().getClass() == ParseException.class) {
        // invalid accept-language string in http header
        // compensate exception with using default locale
        return Arrays.asList(DEFAULT_RESPONSE_LOCALE);        
      }
      // not able to compensate exception -> re-throw
      throw e;
    }
  }

}
