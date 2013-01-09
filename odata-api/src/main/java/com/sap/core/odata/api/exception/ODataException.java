package com.sap.core.odata.api.exception;

/**
 * Common checked exception for the <code>OData</code> library
 * and base exception for all <code>OData</code>-related exceptions.
 * @author SAP AG
 */
public class ODataException extends Exception {

  private static final long serialVersionUID = 1L;

  public ODataException() {
    super();
  }

  public ODataException(String msg) {
    super(msg);
  }

  public ODataException(String msg, Throwable e) {
    super(msg, e);
  }

  public ODataException(Throwable e) {
    super(e);
  }

  /**
   * Check whether this exception was caused by a {@link ODataHttpException} exception.
   * 
   * @return <code>true</code> if it was caused by an {@link ODataHttpException}, otherwise <code>false</code>.
   */
  public boolean isCausedByHttpException() {
    return getHttpExceptionCause() != null;
  }

  /**
   * Search for and return first (from top) {@link ODataHttpException} in cause hierarchy.
   * If no {@link ODataHttpException} in cause hierarchy <code>NULL</code> is returned. 
   * 
   * @return the first found {@link ODataHttpException} in the cause exception hierarchy. 
   *          Or <code>NULL</code> if no {@link ODataHttpException} is found in cause hierarchy.
   */
  public ODataHttpException getHttpExceptionCause() {
    return getSpecificCause(ODataHttpException.class);
  }

  /**
   * Check whether this exception was caused by a {@link ODataApplicationException} exception.
   * 
   * @return <code>true</code> if it was caused by an {@link ODataApplicationException}, otherwise <code>false</code>.
   */
  public boolean isCausedByApplicationException() {
    return getApplicationExceptionCause() != null;
  }

  /**
   * Check whether this exception was caused by a {@link ODataMessageException} exception.
   * 
   * @return <code>true</code> if it was caused by an {@link ODataMessageException}, otherwise <code>false</code>.
   */
  public boolean isCausedByMessageException() {
    return getMessageExceptionCause() != null;
  }

  /**
   * Search for and return first (from top) {@link ODataMessageException} in cause hierarchy.
   * If no {@link ODataMessageException} in cause hierarchy <code>NULL</code> is returned. 
   * 
   * @return the first found {@link ODataMessageException} in the cause exception hierarchy. 
   *          Or <code>NULL</code> if no {@link ODataMessageException} is found in cause hierarchy.
   */
  public ODataMessageException getMessageExceptionCause() {
    return getSpecificCause(ODataMessageException.class);
  }

  /**
   * Search for and return first (from top) {@link ODataApplicationException} in cause hierarchy.
   * If no {@link ODataApplicationException} in cause hierarchy <code>NULL</code> is returned. 
   * 
   * @return the first found {@link ODataApplicationException} in the cause exception hierarchy. 
   *          Or <code>NULL</code> if no {@link ODataApplicationException} is found in cause hierarchy.
   */
  public ODataApplicationException getApplicationExceptionCause() {
    return getSpecificCause(ODataApplicationException.class);
  }

  private <T> T getSpecificCause(Class<T> causeClass) {
    Throwable cause = getCause();
    while (cause != null) {
      if (causeClass.isInstance(cause)) {
        return causeClass.cast(cause);
      }
      cause = cause.getCause();
    }
    return null;
  }
}
