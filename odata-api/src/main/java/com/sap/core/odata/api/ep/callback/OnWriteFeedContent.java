package com.sap.core.odata.api.ep.callback;

/**
 * Callback interface for the $expand query option. 
 * <p>If an expand clause for a navigation property which points to a feed is found this callback will be called.
 * <br>Pointing to an feed means the navigation property has a multiplicity of 0..* or 1..*.
 * 
 * @author SAP AG
 *
 */
public interface OnWriteFeedContent {

  /**
   * Retrieves the data for a feed. See {@link WriteFeedCallbackContext} for details on the context and {@link WriteFeedCallbackResult} for details on the result of this method.
   * @param context of this entry
   * @return result - must not be null.
   */
  WriteFeedCallbackResult retrieveFeedResult(WriteFeedCallbackContext context);
  
}
