package com.sap.core.odata.api.ep.callback;

import java.util.List;

import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;

/**
 * A {@link ReadEntryResultContext} represents an inlined navigation property which points to an entry or feed.
 * The {@link ReadEntryResultContext} contains the {@link EntityProviderReadProperties} which were used for read, 
 * the <code>navigationPropertyName</code>, the read/de-serialized inlined entity and information whether the inlined content
 * is a <code>feed</code> (multiplicity of <code>1..m</code>) or a single <code>entry</code> (multiplicity of <code>0..1</code> or <code>1..1</code>).
 * If inlined navigation property is <code>nullable</code> the {@link ReadEntryResultContext} has the 
 * <code>navigationPropertyName</code> and a <code>NULL</code> entry set.
 * 
 * @author SAP AG
 *
 */
public class ReadEntryResultContext {

  private EntityProviderReadProperties readProperties;
  private final String navigationPropertyName;
  private final Object entry;
  private final boolean isFeed;

  public ReadEntryResultContext(final EntityProviderReadProperties properties, final String navigationPropertyName, final ODataEntry entry) {
    this(properties, navigationPropertyName, entry, false);
  }

  public ReadEntryResultContext(final EntityProviderReadProperties properties, final String navigationPropertyName, final List<ODataEntry> entry) {
    this(properties, navigationPropertyName, entry, true);
  }

  public ReadEntryResultContext(final EntityProviderReadProperties properties, final String navigationPropertyName, final Object entry, boolean isFeed) {
    this.readProperties = properties;
    this.navigationPropertyName = navigationPropertyName;
    this.entry = entry;
    this.isFeed = isFeed;
  }

  public EntityProviderReadProperties getReadProperties() {
    return readProperties;
  }

  public String getNavigationPropertyName() {
    return navigationPropertyName;
  }

  public Object getEntry() {
    return entry;
  }

  public ODataEntry getODataEntry() {
    if (isFeed) {
      return null;
    }
    return (ODataEntry) entry;
  }

  @SuppressWarnings("unchecked")
  public List<ODataEntry> getODataFeed() {
    if (isFeed) {
      return (List<ODataEntry>) entry;
    }
    return null;
  }

  /**
   * Return whether this entry is a <code>feed</code> (multiplicity of <code>1..m</code>) 
   * or a single <code>entry</code> (multiplicity of <code>0..1</code> or <code>1..1</code>).
   * 
   * @return <code>true</code> for a feed and <code>false</code> for an entry
   */
  public boolean isFeed() {
    return isFeed;
  }
}
