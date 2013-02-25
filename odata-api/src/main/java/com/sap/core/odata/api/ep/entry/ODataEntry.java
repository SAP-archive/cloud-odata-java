package com.sap.core.odata.api.ep.entry;

import java.util.Map;

/**
 * An {@link ODataEntry} contains all <b>properties</b> (in form of a {@link Map}) and possible <b>metadata</b> 
 * (in form of {@link MediaMetadata} and/or {@link EntryMetadata}) for an entry.
 * 
 */
public interface ODataEntry {

  /**
   * Properties of this {@link ODataEntry} in form of a <b>property name</b> to <b>property value</b> map.
   * 
   * @return a <b>property name</b> to <b>property value</b> map.
   */
  public Map<String, Object> getProperties();

  /**
   * Get {@link MediaMetadata} for this {@link ODataEntry} if available.
   * 
   * @return {@link MediaMetadata} for this {@link ODataEntry} if available.
   */
  public MediaMetadata getMediaMetadata();

  /**
   * Get {@link EntryMetadata} for this {@link ODataEntry} if available.
   * 
   * @return {@link EntryMetadata} for this {@link ODataEntry} if available.
   */
  public EntryMetadata getMetadata();
}
