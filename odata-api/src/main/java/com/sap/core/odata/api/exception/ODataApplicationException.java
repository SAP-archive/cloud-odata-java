package com.sap.core.odata.api.exception;

import java.util.Locale;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * This class represents a translated application exception. Use this exception class to display custom exception messages. 
 * <br>If a HTTP status is given this exception will result in the set status code like an HTTP exception. 
 * <br>A set status code can be used to show a substatus to a HTTP status as described in the OData protocol specification. 
 * @author SAP AG
 */
public class ODataApplicationException extends ODataException {

  private static final long serialVersionUID = 1L;
  private String errorCode;
  private HttpStatusCodes httpStatus = HttpStatusCodes.INTERNAL_SERVER_ERROR;
  private final Locale locale;

  /**
   * Since this is a translated application exception locale must not be null.
   * @param message
   * @param locale
   */
  public ODataApplicationException(final String message, final Locale locale) {
    super(message);
    this.locale = locale;
  }

  /**
   * Since this is a translated application exception locale must not be null. 
   * <br>The status code given will be  displayed at the client.
   * @param message
   * @param locale
   * @param status
   */
  public ODataApplicationException(final String message, final Locale locale, final HttpStatusCodes status) {
    this(message, locale);
    httpStatus = status;
  }

  /**
   * Since this is a translated application exception locale must not be null.
   * <br>The status code given will be displayed at the client.
   * <br>The error code may be used as a substatus for the HTTP status code as described in the OData protocol specification.
   * @param message
   * @param locale
   * @param status
   * @param errorCode
   */
  public ODataApplicationException(final String message, final Locale locale, final HttpStatusCodes status, final String errorCode) {
    this(message, locale, status);
    this.errorCode = errorCode;
  }

  /**   
   * Since this is a translated application exception locale must not be null.  
   * <br>The status code given will be displayed at the client.
   * <br>The error code may be used as a substatus for the HTTP status code as described in the OData protocol specification.
   * @param message
   * @param locale
   * @param status
   * @param errorCode
   * @param e
   */
  public ODataApplicationException(final String message, final Locale locale, final HttpStatusCodes status, final String errorCode, final Throwable e) {
    super(message, e);
    this.errorCode = errorCode;
    httpStatus = status;
    this.locale = locale;
  }

  /**
   * Since this is a translated application exception locale must not be null.
   * @param message
   * @param locale
   * @param e
   */
  public ODataApplicationException(final String message, final Locale locale, final Throwable e) {
    super(message, e);
    this.locale = locale;
  }

  /**
   * Since this is a translated application exception locale must not be null.
   * <br>The status code given will be displayed at the client.
   * @param message
   * @param locale
   * @param status
   * @param e
   */
  public ODataApplicationException(final String message, final Locale locale, final HttpStatusCodes status, final Throwable e) {
    this(message, locale, e);
    httpStatus = status;
  }

  /**
   * Since this is a translated application exception locale must not be null.
   * <br>The error code may be used as a substatus for the HTTP status code as described in the OData protocol specification.
   * @param message
   * @param locale
   * @param errorCode
   * @param e
   */
  public ODataApplicationException(final String message, final Locale locale, final String errorCode, final Throwable e) {
    this(message, locale, e);
    this.errorCode = errorCode;

  }

  /**
   * @return {@link Locale} the locale
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * Default HttpStatusCodes.INTERNAL_SERVER_ERROR
   * @return {@link HttpStatusCodes} the status code 
   */
  public HttpStatusCodes getHttpStatus() {
    return httpStatus;
  }

  /**
   * Default code is null
   * @return <b>String</b>The error code displayed in the error message.
   */
  public String getCode() {
    return errorCode;
  }

}
