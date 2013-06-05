package com.sap.core.odata.api.ep.callback;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;

/**
 * This interface must be implemented in order to provide tombstone support. 
 * <br>The callback implementing this interface is registered at the {@link EntityProviderWriteProperties} using the callback key of this class.  
 * @author SAP AG
 *
 */
public interface TombstoneCallback extends ODataCallback {

  /**
   * The key to be used when registering the callback at the {@link EntityProviderWriteProperties}
   */
  public static final String CALLBACK_KEY_TOMBSTONE = "~tombstoneCallback";
  public static final String PREFIX_TOMBSTONE = "at";
  public static final String NAMESPACE_TOMBSTONE = "http://purl.org/atompub/tombstones/1.0";

  /**
   * This method is called after all entries have been serialized. The returned list must contain all deleted entries, in the form of Map{@literal <}property name, property value{@literal >}, which should be serialized.
   * <br>A map representing a deleted entry <b>MUST</b> contain all properties which are part of the key for this entry.
   * <br>A map representing a deleted entry <b>MAY</b> contain the property which is mapped on SyndicationUpdated. The provided value here will result in the value of the "when" attribute of the deleted entry. 
   * @return a list of all deleted entries
   */
  List<Map<String, Object>> getTombstoneData();

}
