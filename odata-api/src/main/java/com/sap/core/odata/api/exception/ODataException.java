package com.sap.core.odata.api.exception;

/**
 * Base exception for all <code>OData</code>-related exceptions.
 * @author SAP AG
 */
public class ODataException extends Exception {

  private static final long serialVersionUID = 1L;

  public ODataException() {
    super();
  }

  public ODataException(final String msg) {
    super(msg);
  }

  public ODataException(final String msg, final Throwable e) {
    super(msg, e);
  }

  public ODataException(final Throwable e) {
    super(e);
  }

  /**
   * Checks whether this exception is an or was caused by an {@link ODataHttpException} exception.
   *
   * @return <code>true</code> if this is an or was caused by an {@link ODataHttpException}, otherwise <code>false</code>
   */
  public boolean isCausedByHttpException() {
    return getHttpExceptionCause() != null;
  }

  /**
   * Search for and return first (from top) {@link ODataHttpException} in the cause hierarchy.
   * If there is no {@link ODataHttpException} in the cause hierarchy, <code>NULL</code> is returned.
   * 
   * @return the first found {@link ODataHttpException} in the cause exception hierarchy
   *         or <code>NULL</code> if no {@link ODataHttpException} is found in the cause hierarchy
   */
  public ODataHttpException getHttpExceptionCause() {
    return getSpecificCause(ODataHttpException.class);
  }

  /**
   * Checks whether this exception is an or was caused by an {@link ODataApplicationException} exception.
   *
   * @return <code>true</code> if this is an or was caused by an {@link ODataApplicationException}, otherwise <code>false</code>
   */
  public boolean isCausedByApplicationException() {
    return getApplicationExceptionCause() != null;
  }

  /**
   * Checks whether this exception is an or was caused by an {@link ODataMessageException} exception.
   * 
   * @return <code>true</code> if this is an or was caused by an {@link ODataMessageException}, otherwise <code>false</code>
   */
  public boolean isCausedByMessageException() {
    return getMessageExceptionCause() != null;
  }

  /**
   * Search for and return first (from top) {@link ODataMessageException} in the cause hierarchy.
   * If there is no {@link ODataMessageException} in the cause hierarchy <code>NULL</code> is returned.
   *
   * @return the first found {@link ODataMessageException} in the cause exception hierarchy 
   *         or <code>NULL</code> if no {@link ODataMessageException} is found in the cause hierarchy
   */
  public ODataMessageException getMessageExceptionCause() {
    return getSpecificCause(ODataMessageException.class);
  }

  /**
   * Search for and return first (from top) {@link ODataApplicationException} in the cause hierarchy.
   * If there is no {@link ODataApplicationException} in the cause hierarchy <code>NULL</code> is returned.
   *
   * @return the first found {@link ODataApplicationException} in the cause exception hierarchy
   *         or <code>NULL</code> if no {@link ODataApplicationException} is found in the cause hierarchy
   */
  public ODataApplicationException getApplicationExceptionCause() {
    return getSpecificCause(ODataApplicationException.class);
  }

  private <T extends ODataException> T getSpecificCause(final Class<T> causeClass) {
    Throwable cause = this;
    while (cause != null) {
      if (causeClass.isInstance(cause)) {
        return causeClass.cast(cause);
      } else {
        cause = cause.getCause();
      }
    }

    return null;
  }

  /**
   * Returns the last Throwable in the chain of caused exceptions or null if there is no cause.
   * @return null or the root cause of this exception
   */
  public Throwable getRootCause() {
    Throwable rootCause = getCause();
    
    if (rootCause != null) {
      if (rootCause instanceof ODataException) {
        ODataException ode = (ODataException) rootCause;
        Throwable t = ode.getRootCause();
        if (t != null) {
          rootCause = t;
        }
      }
    }
     
    return rootCause;
  }
}
