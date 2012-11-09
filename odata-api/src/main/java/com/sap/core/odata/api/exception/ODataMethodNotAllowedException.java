/**
 * 
 */
package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

/**
 *
 */
public class ODataMethodNotAllowedException extends ODataMessageException {

  public static final MessageReference DISPATCH = createContext(ODataMethodNotAllowedException.class, "DISPATCH", HttpStatus.METHOD_NOT_ALLOWED);

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
