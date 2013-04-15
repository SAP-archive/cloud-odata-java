/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.ep.entry;

import java.util.List;

/**
 * {@link EntryMetadata} contains all metadata for an {@link ODataEntry}.
 */
public interface EntryMetadata {

  /**
   * Gets the URI of this entry.
   * 
   * @return the URI
   */
  public abstract String getUri();

  /**
   * Gets the association URIs for a given navigation property.
   * 
   * @param navigationPropertyName the name of the navigation property
   * @return the list of URIs for the given navigation property
   */
  public abstract List<String> getAssociationUris(String navigationPropertyName);

  /**
   * Gets the entity tag for this entry.
   * 
   * @return the entity tag
   */
  public abstract String getEtag();

  /**
   * Gets the ID of this entry.
   * 
   * @return the ID
   */
  public abstract String getId();

}
