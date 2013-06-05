/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.core.rest;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataHttpException;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.processor.ODataErrorCallback;
import com.sap.core.odata.api.processor.ODataErrorContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.ProviderFacadeImpl;
import com.sap.core.odata.core.exception.MessageService;
import com.sap.core.odata.core.exception.MessageService.Message;

/**
 * Creates an error response according to the format defined by the OData standard
 * if an exception occurs.
 * @author SAP AG
 */
@Provider
public class ODataExceptionMapperImpl implements ExceptionMapper<Exception> {

  private static final String DOLLAR_FORMAT = "$format";
  private static final String DOLLAR_FORMAT_JSON = "json";

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
      final Exception toHandleException = extractException(exception);

      ODataErrorContext errorContext;
      if (toHandleException instanceof ODataApplicationException) {
        errorContext = extractInformationForApplicationException((ODataApplicationException) toHandleException);
      } else if (toHandleException instanceof ODataHttpException) {
        errorContext = extractInformationForHttpException((ODataHttpException) toHandleException);
      } else if (toHandleException instanceof ODataMessageException) {
        errorContext = extractInformationForMessageException((ODataMessageException) toHandleException);
      } else if (toHandleException instanceof WebApplicationException) {
        errorContext = extractInformationForWebApplicationException((WebApplicationException) toHandleException);
      } else {
        errorContext = extractInformationForException(exception);
      }

      ODataResponse oDataResponse;
      ODataErrorCallback callback = getErrorHandlerCallback();
      if (callback != null) {
        try {
          oDataResponse = callback.handleError(errorContext);
        } catch (ODataApplicationException e) {
          errorContext = extractInformationForApplicationException(e);
          oDataResponse = convertContextToODataResponse(errorContext);
        }

      } else {
        oDataResponse = convertContextToODataResponse(errorContext);
      }

      //Convert ODataResponse to JAXRS Response
      return Util.convertResponse(oDataResponse, oDataResponse.getStatus(), null, null);

    } catch (Exception e) {
      //Exception mapper has to be robust thus we log the exception and just give back a generic error
      ODataResponse response = ODataResponse.entity("Exception during error handling occured!")
          .contentHeader(ContentType.TEXT_PLAIN.toContentTypeString())
          .status(HttpStatusCodes.INTERNAL_SERVER_ERROR).build();
      return Util.convertResponse(response, response.getStatus(), null, null);
    }
  }

  private ODataErrorContext extractInformationForApplicationException(final ODataApplicationException toHandleException) {
    ODataErrorContext context = createDefaultErrorContext();
    context.setContentType(getContentType().toContentTypeString());
    context.setHttpStatus(toHandleException.getHttpStatus());
    context.setErrorCode(toHandleException.getCode());
    context.setMessage(toHandleException.getMessage());
    context.setLocale(toHandleException.getLocale());
    context.setException(toHandleException);
    return context;
  }

  private ODataErrorContext extractInformationForHttpException(final ODataHttpException toHandleException) {
    MessageReference messageReference = toHandleException.getMessageReference();
    Message localizedMessage = messageReference == null ? null : extractEntity(messageReference);

    ODataErrorContext context = createDefaultErrorContext();
    context.setContentType(getContentType().toContentTypeString());
    context.setHttpStatus(toHandleException.getHttpStatus());
    context.setErrorCode(toHandleException.getErrorCode());
    if (localizedMessage != null) {
      context.setMessage(localizedMessage.getText());
      context.setLocale(localizedMessage.getLocale());
    }
    context.setException(toHandleException);
    return context;
  }

  private ODataErrorContext extractInformationForMessageException(final ODataMessageException toHandleException) {
    HttpStatusCodes responseStatusCode;
    if (toHandleException instanceof EntityProviderException) {
      responseStatusCode = HttpStatusCodes.BAD_REQUEST;
    } else {
      responseStatusCode = HttpStatusCodes.INTERNAL_SERVER_ERROR;
    }

    MessageReference messageReference = toHandleException.getMessageReference();
    Message localizedMessage = extractEntity(messageReference);

    ODataErrorContext context = createDefaultErrorContext();
    context.setContentType(getContentType().toContentTypeString());
    context.setHttpStatus(responseStatusCode);
    context.setErrorCode(toHandleException.getErrorCode());
    context.setMessage(localizedMessage.getText());
    context.setLocale(localizedMessage.getLocale());
    context.setException(toHandleException);
    return context;
  }

  private ODataErrorContext extractInformationForWebApplicationException(final WebApplicationException toHandleException) {
    ODataErrorContext context = createDefaultErrorContext();
    context.setContentType(getContentType().toContentTypeString());
    context.setHttpStatus(HttpStatusCodes.fromStatusCode(toHandleException.getResponse().getStatus()));
    if (toHandleException instanceof NotAllowedException) {
      // RFC 2616, 5.1.1: " An origin server SHOULD return the status code
      // 405 (Method Not Allowed) if the method is known by the origin server
      // but not allowed for the requested resource, and 501 (Not Implemented)
      // if the method is unrecognized or not implemented by the origin server."
      // Since all recognized methods are handled elsewhere, we unconditionally
      // switch to 501 here for not-allowed exceptions thrown directly from
      // JAX-RS implementations.
      context.setHttpStatus(HttpStatusCodes.NOT_IMPLEMENTED);
      context.setMessage("The request dispatcher does not allow the HTTP method used for the request.");
      context.setLocale(Locale.ENGLISH);
    } else {
      context.setMessage(toHandleException.getMessage());
      context.setLocale(DEFAULT_RESPONSE_LOCALE);
    }
    context.setErrorCode(null);
    context.setException(toHandleException);
    return context;
  }

  private ODataErrorContext extractInformationForException(final Exception exception) {
    ODataErrorContext context = createDefaultErrorContext();
    context.setContentType(getContentType().toContentTypeString());
    context.setHttpStatus(HttpStatusCodes.INTERNAL_SERVER_ERROR);
    context.setErrorCode(null);
    context.setMessage(exception.getMessage());
    context.setLocale(DEFAULT_RESPONSE_LOCALE);
    context.setException(exception);
    return context;
  }

  private ODataErrorContext createDefaultErrorContext() {
    ODataErrorContext context = new ODataErrorContext();
    if (uriInfo != null) {
      context.setRequestUri(uriInfo.getRequestUri());
    }
    if (httpHeaders != null && httpHeaders.getRequestHeaders() != null) {
      MultivaluedMap<String, String> requestHeaders = httpHeaders.getRequestHeaders();
      Set<Entry<String, List<String>>> entries = requestHeaders.entrySet();
      for (Entry<String, List<String>> entry : entries) {
        context.putRequestHeader(entry.getKey(), entry.getValue());
      }
    }
    return context;
  }

  private ODataResponse convertContextToODataResponse(final ODataErrorContext errorContext) throws EntityProviderException {
    return new ProviderFacadeImpl().writeErrorDocument(errorContext.getContentType(), errorContext.getHttpStatus(), errorContext.getErrorCode(), errorContext.getMessage(), errorContext.getLocale(), null);
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
    ContentType contentType = getContentTypeByUriInfo();
    if (contentType == null) {
      contentType = getContentTypeByAcceptHeader();
    }
    return contentType;
  }

  private ContentType getContentTypeByUriInfo() {
    ContentType contentType = null;
    if (uriInfo != null && uriInfo.getQueryParameters() != null) {
      MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
      if (queryParameters.containsKey(DOLLAR_FORMAT)) {
        String contentTypeString = queryParameters.getFirst(DOLLAR_FORMAT);
        if (DOLLAR_FORMAT_JSON.equals(contentTypeString)) {
          contentType = ContentType.APPLICATION_JSON;
        } else {
          //Any format mentioned in the $format parameter other than json results in an application/xml content type for error messages
          //due to the OData V2 Specification
          contentType = ContentType.APPLICATION_XML;
        }
      }
    }
    return contentType;
  }

  private ContentType getContentTypeByAcceptHeader() {
    for (MediaType type : httpHeaders.getAcceptableMediaTypes()) {
      if (ContentType.isParseable(type.toString())) {
        ContentType convertedContentType = ContentType.create(type.toString());
        if (convertedContentType.isWildcard()
            || ContentType.APPLICATION_XML.equals(convertedContentType) || ContentType.APPLICATION_XML_CS_UTF_8.equals(convertedContentType)
            || ContentType.APPLICATION_ATOM_XML.equals(convertedContentType) || ContentType.APPLICATION_ATOM_XML_CS_UTF_8.equals(convertedContentType)) {
          return ContentType.APPLICATION_XML;
        } else if (ContentType.APPLICATION_JSON.equals(convertedContentType) || ContentType.APPLICATION_JSON_CS_UTF_8.equals(convertedContentType)) {
          return ContentType.APPLICATION_JSON;
        }
      }
    }
    return ContentType.APPLICATION_XML;
  }

  private ODataErrorCallback getErrorHandlerCallback() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    ODataErrorCallback callback = null;
    final String factoryClassName = servletConfig.getInitParameter(ODataServiceFactory.FACTORY_LABEL);
    if (factoryClassName != null) {
      Class<?> factoryClass = Class.forName(factoryClassName);
      final ODataServiceFactory serviceFactory = (ODataServiceFactory) factoryClass.newInstance();

      callback = serviceFactory.getCallback(ODataErrorCallback.class);
    }
    return callback;
  }
}
