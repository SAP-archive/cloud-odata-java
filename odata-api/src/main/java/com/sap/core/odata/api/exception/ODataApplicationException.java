package com.sap.core.odata.api.exception;

import java.util.Locale;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * This class represents a translated application exception.
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
  public ODataApplicationException(String message, Locale locale) {
    super(message);
    this.locale = locale;
  }

  /**
   * Since this is a translated application exception locale must not be null.
   * @param message
   * @param locale
   * @param status
   */
  public ODataApplicationException(String message, Locale locale, HttpStatusCodes status) {
    this(message, locale);
    httpStatus = status;
  }

  /**
   * Since this is a translated application exception locale must not be null.
   * @param message
   * @param locale
   * @param status
   * @param errorCode
   */
  public ODataApplicationException(String message, Locale locale, HttpStatusCodes status, String errorCode) {
    this(message, locale, status);
    this.errorCode = errorCode;
  }

  /**   
   * Since this is a translated application exception locale must not be null.
   * @param message
   * @param locale
   * @param status
   * @param errorCode
   * @param e
   */
  public ODataApplicationException(String message, Locale locale, HttpStatusCodes status, String errorCode, Throwable e) {
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
  public ODataApplicationException(String message, Locale locale, Throwable e) {
    super(message, e);
    this.locale = locale;
  }

  /**
   * Since this is a translated application exception locale must not be null.
   * @param message
   * @param locale
   * @param status
   * @param e
   */
  public ODataApplicationException(String message, Locale locale, HttpStatusCodes status, Throwable e) {
    this(message, locale, e);
    httpStatus = status;
  }

  /**
   * Since this is a translated application exception locale must not be null.
   * @param message
   * @param locale
   * @param errorCode
   * @param e
   */
  public ODataApplicationException(String message, Locale locale, String errorCode, Throwable e) {
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
   * @return <b>String</b>The error code displayed in the error message. Mandatory after OData specification.
   */
  public String getCode() {
    return errorCode;
  }

}
