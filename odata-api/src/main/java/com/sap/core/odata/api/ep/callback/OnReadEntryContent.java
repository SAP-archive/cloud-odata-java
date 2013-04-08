package com.sap.core.odata.api.ep.callback;

/**
 * Callback interface for the deep insert read calls (read of <code><m:inline></code> content). 
 * <p>If an inlined navigation property which points to an entry is found this callback will be called to retrieve the necessary data to read the inlined entity.
 * <br>Pointing to an entry means the navigation property has a multiplicity of <code>1..1</code> or <code>1..m</code>.
 * 
 * @author SAP AG
 *
 */
public interface OnReadEntryContent {

  /**
   * All contextual information about inlined navigation property is given within {@link ReadEntryCallbackContext}.
   * As result a {@link ReadCallbackResult} object is expected which <b>MUST NOT</b> be <code>NULL</code>.
   * 
   * @param callbackInfo with contextual information about inlined navigation property 
   * @return a {@link ReadCallbackResult} object with all necessary data to read inlined entity (which <b>MUST NOT</b> be <code>NULL</code>).
   */
  ReadCallbackResult retrieveReadResult(ReadEntryCallbackContext callbackInfo);
}
