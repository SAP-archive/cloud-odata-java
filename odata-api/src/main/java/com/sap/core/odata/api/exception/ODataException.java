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
   * Search for and return first (from top) {@link ODataContextedException} in cause hierarchy.
   * If no {@link ODataContextedException}  in cause hierarchy <code>NULL</code> is returned. 
   * 
   * @return
   */
  public ODataContextedException getContextedCause() {
      Throwable cause = getCause();
      while (cause != null) {
        if (cause instanceof ODataContextedException) {
          return (ODataContextedException) cause;
        }
        cause = cause.getCause();
      }
      return (ODataContextedException) cause;
  }
}
