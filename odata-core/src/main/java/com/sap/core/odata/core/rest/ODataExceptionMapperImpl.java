package com.sap.core.odata.core.rest;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataHttpException;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.exception.ODataNotAcceptableException;
import com.sap.core.odata.api.processor.ODataErrorHandlerCallback;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.ProviderFacadeImpl;
import com.sap.core.odata.core.exception.MessageService;
import com.sap.core.odata.core.exception.MessageService.Message;
import com.sap.core.odata.core.exception.ODataRuntimeException;

/**
 * Creates an error response according to the format defined by the OData standard
 * if an exception occurs.
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
  @Context
  ServletConfig servletConfig;

  @Override
  public Response toResponse(final Exception exception) {
    try {
      ODataResponse oDataResponse;

      final Exception toHandleException = extractException(exception);
      if (toHandleException instanceof ODataApplicationException) {
        oDataResponse = buildResponseForApplicationException((ODataApplicationException) toHandleException);
      } else if (toHandleException instanceof ODataHttpException) {
        oDataResponse = buildResponseForHttpException((ODataHttpException) toHandleException);
      } else if (toHandleException instanceof ODataMessageException) {
        oDataResponse = buildResponseForMessageException((ODataMessageException) toHandleException);
      } else if (toHandleException instanceof WebApplicationException) {
        oDataResponse = buildResponseForWebApplicationException((WebApplicationException) toHandleException);
      } else {
        oDataResponse = buildResponseForException(exception);
      }

      if (isInternalServerError(oDataResponse)) {
        LOG.error(exception.getMessage(), exception);
      }

      ODataErrorHandlerCallback callback = getErrorHandlerCallback();
      if (callback != null) {
        oDataResponse = callback.handleError(oDataResponse, toHandleException);
      }

      Response response = Util.convertResponse(oDataResponse, oDataResponse.getStatus(), null, null);

      return response;

    } catch (Exception e) {
      //Exception mapper has to be robust thus we log the exception and just give back a generic error
      LOG.error(exception.getMessage(), exception);
      ODataResponseBuilder responseBuilder = ODataResponse.entity("Fatal error when serializing an exception")
          .contentHeader(ContentType.APPLICATION_XML.toContentTypeString())
          .header(ODataHttpHeaders.DATASERVICEVERSION, ODataServiceVersion.V10)
          .status(HttpStatusCodes.INTERNAL_SERVER_ERROR);

      if (e instanceof EntityProviderException) {
        if (((EntityProviderException) e).getHttpExceptionCause() instanceof ODataNotAcceptableException) {
          responseBuilder.status(HttpStatusCodes.NOT_ACCEPTABLE);
        }
      }
      ODataResponse response = responseBuilder.build();
      return Util.convertResponse(response, response.getStatus(), null, null);
    }
  }

  private ODataResponse buildResponseForMessageException(final ODataMessageException messageException) throws EntityProviderException {
    HttpStatusCodes responseStatusCode;

    if (messageException instanceof EntityProviderException) {
      responseStatusCode = HttpStatusCodes.BAD_REQUEST;
    } else {
      responseStatusCode = HttpStatusCodes.INTERNAL_SERVER_ERROR;
    }

    MessageReference messageReference = messageException.getMessageReference();
    Message localizedMessage = extractEntity(messageReference);
    String localizedMessageText = localizedMessage.getText();
    Locale localizedMessageLocale = localizedMessage.getLocale();
    return new ProviderFacadeImpl().writeErrorDocument(getContentType().toContentTypeString(), responseStatusCode, messageException.getErrorCode(), localizedMessageText, localizedMessageLocale, null);
  }

  private ODataResponse buildResponseForWebApplicationException(final WebApplicationException webApplicationException) throws EntityProviderException {
    return new ProviderFacadeImpl().writeErrorDocument(getContentType().toContentTypeString(), HttpStatusCodes.fromStatusCode(webApplicationException.getResponse().getStatus()), null, webApplicationException.getMessage(), DEFAULT_RESPONSE_LOCALE, null);
  }

  private ODataResponse buildResponseForException(final Exception exception) throws EntityProviderException {
    return new ProviderFacadeImpl().writeErrorDocument(getContentType().toContentTypeString(), HttpStatusCodes.INTERNAL_SERVER_ERROR, null, exception.getMessage(), DEFAULT_RESPONSE_LOCALE, null);
  }

  private ODataResponse buildResponseForApplicationException(final ODataApplicationException exception) throws EntityProviderException {
    return new ProviderFacadeImpl().writeErrorDocument(getContentType().toContentTypeString(), exception.getHttpStatus(), exception.getCode(), exception.getMessage(), exception.getLocale(), null);
  }

  private ODataResponse buildResponseForHttpException(final ODataHttpException httpException) throws EntityProviderException {
    MessageReference messageReference = httpException.getMessageReference();
    Message localizedMessage = extractEntity(messageReference);
    String localizedMessageText = localizedMessage.getText();
    Locale localizedMessageLocale = localizedMessage.getLocale();
    return new ProviderFacadeImpl().writeErrorDocument(getContentType().toContentTypeString(), httpException.getHttpStatus(), httpException.getErrorCode(), localizedMessageText, localizedMessageLocale, null);
  }

  private boolean isInternalServerError(final ODataResponse response) {
    return response.getStatus().getStatusCode() == Status.INTERNAL_SERVER_ERROR.getStatusCode();
  }

  private Exception extractException(final Exception exception) {
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

  private Message extractEntity(final MessageReference context) {
    final Locale locale = MessageService.getSupportedLocale(getLanguages(), DEFAULT_RESPONSE_LOCALE);
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
      if (ContentType.isParseable(type.toString())) {
        ContentType convertedContentType = ContentType.create(type.toString());
        if (convertedContentType.isWildcard()
            || ContentType.APPLICATION_XML.equals(convertedContentType) || ContentType.APPLICATION_XML_CS_UTF_8.equals(convertedContentType)
            || ContentType.APPLICATION_ATOM_XML.equals(convertedContentType) || ContentType.APPLICATION_ATOM_XML_CS_UTF_8.equals(convertedContentType)) {
          return ContentType.APPLICATION_XML_CS_UTF_8;
        } else if (ContentType.APPLICATION_JSON.equals(convertedContentType) || ContentType.APPLICATION_JSON_CS_UTF_8.equals(convertedContentType)) {
          return ContentType.APPLICATION_JSON_CS_UTF_8;
        }
      }
    }
    return ContentType.APPLICATION_XML_CS_UTF_8;
  }

  private ODataErrorHandlerCallback getErrorHandlerCallback() {
    try {
      ODataErrorHandlerCallback callback = null;
      final String factoryClassName = servletConfig.getInitParameter(ODataServiceFactory.FACTORY_LABEL);
      if (factoryClassName != null) {
        Class<?> factoryClass = Class.forName(factoryClassName);
        final ODataServiceFactory serviceFactory = (ODataServiceFactory) factoryClass.newInstance();

        callback = serviceFactory.getCallback(ODataErrorHandlerCallback.class);
      }
      return callback;
    } catch (Exception e) {
      throw new ODataRuntimeException("Exception during error handling occured!", e);
    }

  }
}
