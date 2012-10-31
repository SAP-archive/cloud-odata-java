package com.sap.core.odata.api.exception;

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

  public boolean causedByContextedException() {
    return getContextedCause() != null;
  }

  /**
   * Search for and return first (from top) {@link ODataMessageException} in cause hierarchy.
   * If no {@link ODataMessageException}  in cause hierarchy <code>NULL</code> is returned. 
   * 
   * @return
   */
  public ODataMessageException getContextedCause() {
    Throwable cause = getCause();
    while (cause != null) {
      if (cause instanceof ODataMessageException) {
        return (ODataMessageException) cause;
      }
      cause = cause.getCause();
    }
    return (ODataMessageException) cause;
  }
}
