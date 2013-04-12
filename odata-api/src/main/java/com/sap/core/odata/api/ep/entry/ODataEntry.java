package com.sap.core.odata.api.ep.entry;

import java.util.Map;

import com.sap.core.odata.api.uri.ExpandSelectTreeNode;

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
  
  /**
   * If this {@link ODataEntry} contains properties of an inline navigation property this method
   * returns <code>true</code>. 
   * Otherwise if this {@link ODataEntry} only contains it own properties this method
   * returns <code>false</code>.
   * 
   * @return <code>true</code> if inline navigation properties are contained, otherwise <code>false</code>.
   */
  public boolean containsInlineEntry();
  
  /**
   * Gets the expand select tree data structure which can be used for <code>$expand</code> query option.
   * 
   * @return parsed tree structure representing the $expand
   */
  public ExpandSelectTreeNode getExpandSelectTree();
}
