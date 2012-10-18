/**
 * 
 */
package com.sap.core.odata.core.exception;

/**
 *
 */
public class ODataMethodNotAllowedException extends ODataContextedException {

  public static final Context DISPATCH = createContext(ODataMethodNotAllowedException.class, "DISPATCH");
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * @param context
   */
  public ODataMethodNotAllowedException(Context context) {
    super(context);
  }

}
