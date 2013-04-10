package com.sap.core.odata.api.ep.callback;

/**
 * Callback interface for the deep insert read calls (read of <code><m:inline></code> content). 
 * <p>
 * If an inlined navigation property which points to an entry or feed is found this callback will be called to deliver the read/de-serialized inlined entity.
 * If inlined navigation property is <code>nullable</code> and not set a {@link ReadEntryResultContext} is given with the 
 * <code>navigationPropertyName</code> and a <code>NULL</code> entry set.
 * <br/>
 * Pointing to an entry means the navigation property has a multiplicity of <code>0..1</code>, <code>1..1</code> or <code>1..m</code>.
 * </p>
 * 
 * @author SAP AG
 *
 */
public interface OnReadEntryContent {

  /**
   * All contextual information and the de-serialized inlined navigation property is given within {@link ReadEntryResultContext}.
   * 
   * Additional an optional {@link ReadCallbackResult} object can be returned which then is added to the properties of the parent entry.
   * If nothing (<code>NULL</code>) is returned nothing is added to the parent entry properties (<b>default behavior</b>).
   * 
   * @param callbackInfo with contextual information about and de-serialized inlined navigation property 
   * @return a {@link ReadCallbackResult} optional result which content is added to parent entry properties.
   */
  ReadCallbackResult handleReadResult(ReadEntryResultContext callbackInfo);
}
