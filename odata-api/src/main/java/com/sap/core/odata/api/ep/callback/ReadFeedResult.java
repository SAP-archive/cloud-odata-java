package com.sap.core.odata.api.ep.callback;

import java.util.List;

import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;

/**
 * A {@link ReadFeedResult} represents an inlined navigation property which points to a feed (in the form of a list of
 * {@link ODataEntry} instances).
 * The {@link ReadFeedResult} contains the {@link EntityProviderReadProperties} which were used for read, 
 * the <code>navigationPropertyName</code> and the read/de-serialized inlined entities.
 * If inlined navigation property is <code>nullable</code> the {@link ReadFeedResult} has the 
 * <code>navigationPropertyName</code> and a <code>NULL</code> entry set.
 * 
 * @author SAP AG
 */
public class ReadFeedResult extends ReadResult {

  private final List<ODataEntry> entry;

  /**
   * Constructor.
   * Parameters <b>MUST NOT BE NULL</b>.
   * 
   * @param properties read properties which are used to read enclosing parent entity
   * @param navigationProperty emd navigation property information of found inline navigation property
   * @param entry read entities as list of {@link ODataEntry}
   */
  public ReadFeedResult(final EntityProviderReadProperties properties, final EdmNavigationProperty navigationProperty, final List<ODataEntry> entry) {
    super(properties, navigationProperty);
    this.entry = entry;
  }

  @Override
  public List<ODataEntry> getResult() {
    return entry;
  }

  public boolean isFeed() {
    return true;
  }

  @Override
  public String toString() {
    return super.toString() + "\n\t" + entry.toString();
  }
}
