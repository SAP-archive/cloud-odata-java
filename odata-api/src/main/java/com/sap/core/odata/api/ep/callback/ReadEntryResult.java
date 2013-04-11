package com.sap.core.odata.api.ep.callback;

import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;

/**
 * A {@link ReadEntryResult} represents an inlined navigation property which points to an entry.
 * The {@link ReadEntryResult} contains the {@link EntityProviderReadProperties} which were used for read, 
 * the <code>navigationPropertyName</code> and the read/de-serialized inlined entity.
 * If inlined navigation property is <code>nullable</code> the {@link ReadEntryResult} has the 
 * <code>navigationPropertyName</code> and a <code>NULL</code> entry set.
 * 
 * @author SAP AG
 *
 */
public class ReadEntryResult extends ReadResult {

  private final ODataEntry entry;

  /**
   * Constructor.
   * Parameters <b>MUST NOT BE NULL</b>.
   * 
   * @param readProperties read properties which are used to read enclosing parent entity
   * @param navigationProperty emd navigation property information of found inline navigation property
   * @param entry read entity as {@link ODataEntry}
   */
  public ReadEntryResult(final EntityProviderReadProperties properties, final EdmNavigationProperty navigationProperty, final ODataEntry entry) {
    super(properties, navigationProperty);
    this.entry = entry;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ODataEntry getResult() {
    return entry;
  }
    
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isFeed() {
    return false;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return super.toString() + "\n\t" + entry.toString();
  }
}
