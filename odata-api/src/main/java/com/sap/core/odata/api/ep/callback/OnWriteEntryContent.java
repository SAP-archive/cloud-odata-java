package com.sap.core.odata.api.ep.callback;

/**
 * Callback interface for the $expand query option. 
 * <p>If an expand clause for a navigation property which points to an entry is found this callback will be called.
 * <br>Pointing to an entry means the navigation property has a multiplicity of 0..1 or 1..1.
 * 
 * @author SAP AG
 *
 */
public interface OnWriteEntryContent {

  /**
   * Retrieves the data for an entry. See {@link WriteEntryCallbackContext} for details on the context and {@link WriteEntryCallbackResult} for details on the result of this method.
   * @param context of this entry
   * @return result - must not be null.
   */
  WriteEntryCallbackResult retrieveEntryResult(WriteEntryCallbackContext context);

}
