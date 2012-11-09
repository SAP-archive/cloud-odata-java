/**
 * 
 */
package com.sap.core.odata.api.exception;

/**
 *
 */
public class ODataMethodNotAllowedException extends ODataMessageException {

  public static final MessageReference DISPATCH = createContext(ODataMethodNotAllowedException.class, "DISPATCH");

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * @param context
   */
  public ODataMethodNotAllowedException(MessageReference context) {
    super(context);
  }

}
