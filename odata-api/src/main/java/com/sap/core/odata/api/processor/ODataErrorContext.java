package com.sap.core.odata.api.processor;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.ep.EntityProvider;

/**
 * Error context information bean. Usually created and in error situations. See {@link EntityProvider}, {@link ODataErrorCallback}.
 * 
 * @author SAP AG
 */
public class ODataErrorContext {

  private String contentType;
  private HttpStatusCodes httpStatus;
  private String errorCode;
  private String message;
  private Locale locale;
  private Exception exception;
  private Map<String, List<String>> requestHeaders;
  private URI requestUri;
  private String innerError;

  /**
   * create a new context object
   */
  public ODataErrorContext() {
    requestHeaders = new HashMap<String, List<String>>();
  }

  /**
   * Returns the causing exception.
   * @return exception object
   */
  public Exception getException() {
    return exception;
  }

  /**
   * Set the causing exception.
   * @param exception exception object
   */
  public void setException(final Exception exception) {
    this.exception = exception;
  }

  /** 
   * Get the content type which should be used to serialize an error response.
   * @return a content type
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * Set content type which should be used to serialize an error response.
   * @param contentType a content type
   */
  public void setContentType(final String contentType) {
    this.contentType = contentType;
  }

  /**
   * Get the status code which will be returned in error response.
   * @return http status code
   */
  public HttpStatusCodes getHttpStatus() {
    return httpStatus;
  }

  /**
   * Set http status code that should be returned in error response.
   * @param status http status code
   */
  public void setHttpStatus(final HttpStatusCodes status) {
    httpStatus = status;
  }

  /**
   * Return OData error code that is returned in error response. 
   * @return an application defined error code
   */
  public String getErrorCode() {
    return errorCode;
  }

  /**
   * Set OData error code that should be returned in error response. 
   * @param errorCode an application defined error code
   */
  public void setErrorCode(final String errorCode) {
    this.errorCode = errorCode;
  }

  /**
   * Return a translated error message.
   * @return translated message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Set a translated message.
   * @param message translated message
   */
  public void setMessage(final String message) {
    this.message = message;
  }

  /** 
   * Return the locale of the translated message.
   * @return a locale
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * Set the locale for a translated message.
   * @param locale a locale
   */
  public void setLocale(final Locale locale) {
    this.locale = locale;
  }

  /**
   * Put http headers to be returned in error response.
   * @param key header name
   * @param value list of header values
   */
  public void putRequestHeader(final String key, final List<String> value) {
    requestHeaders.put(key, value);
  }

  /**
   * Return a map of http headers to be returned in error response.
   * @return a map of http headers
   */
  public Map<String, List<String>> getRequestHeaders() {
    return Collections.unmodifiableMap(requestHeaders);
  }

  /**
   * Get the list of values for a request header.
   * @param name header name
   * @return list of values
   */
  public List<String> getRequestHeader(final String name) {
    return requestHeaders.get(name);
  }

  /**
   * Set the request uri to be used in a error response.
   * @param requestUri a uri object
   */
  public void setRequestUri(final URI requestUri) {
    this.requestUri = requestUri;
  }

  /**
   * Get the request uri to be used in a error response.
   * @return a uri object
   */
  public URI getRequestUri() {
    return requestUri;
  }

  /**
   * Get a string for a OData inner error to be returned in error response.
   * @return a inner error message
   */
  public String getInnerError() {
    return innerError;
  }

  /**
   * Set a string for a OData inner error to be returned in error response.
   * @param innerError a inner error message
   */
  public void setInnerError(final String innerError) {
    this.innerError = innerError;
  }
}
