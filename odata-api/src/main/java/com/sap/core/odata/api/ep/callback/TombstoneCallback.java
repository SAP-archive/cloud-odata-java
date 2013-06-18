package com.sap.core.odata.api.ep.callback;

import com.sap.core.odata.api.ODataCallback;

/**
 * <p>Interface that must be implemented in order to provide tombstone support.</p>
 * <p>The callback implementing this interface is registered at the
 * {@link com.sap.core.odata.api.ep.EntityProviderWriteProperties EntityProviderWriteProperties}
 * using the callback key of this class.</p>
 * @author SAP AG
 */
public interface TombstoneCallback extends ODataCallback {

  /**
   * The key to be used when registering the callback at the
   * {@link com.sap.core.odata.api.ep.EntityProviderWriteProperties EntityProviderWriteProperties}
   */
  public static final String CALLBACK_KEY_TOMBSTONE = "~tombstoneCallback";
  public static final String PREFIX_TOMBSTONE = "at";
  public static final String NAMESPACE_TOMBSTONE = "http://purl.org/atompub/tombstones/1.0";
  
  @Deprecated
  public static final String REL_DELTA = "delta";

  /**
   * <p>This method is called after all entries have been serialized.</p>
   * <p>The returned {@link TombstoneCallbackResult} must contain all deleted entries,
   * in the form of List{@literal <}Map{@literal <}property name, property value{@literal >}{@literal >},
   * which should be serialized.</p>
   * <p>A map representing a deleted entry
   * <ul><li><b>MUST</b> contain all properties which are part of the key for this entry.</li>
   * <li><b>MAY</b> contain the property which is mapped on SyndicationUpdated.
   * The provided value here will result in the value of the "when" attribute
   * of the deleted entry.</li></ul></p>
   * <p>The provided delta link will be serialized at the end of the feed document.</p>
   */
  TombstoneCallbackResult getTombstoneCallbackResult();
}
