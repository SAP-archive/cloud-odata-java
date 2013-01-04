/**
 * 
 */
package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 *
 */
public class ODataMethodNotAllowedException extends ODataHttpException {

  public static final MessageReference DISPATCH = createMessageReference(ODataMethodNotAllowedException.class, "DISPATCH");

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * @param context
   */
  public ODataMethodNotAllowedException(MessageReference context) {
    super(context, HttpStatusCodes.METHOD_NOT_ALLOWED);
  }

}
