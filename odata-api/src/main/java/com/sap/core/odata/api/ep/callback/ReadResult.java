package com.sap.core.odata.api.ep.callback;

import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;

/**
 * A {@link ReadResult} represents an inlined navigation property which points to an entry or feed.
 * The {@link ReadResult} contains the {@link EntityProviderReadProperties} which were used for read, 
 * the <code>navigationPropertyName</code>, the read/de-serialized inlined entity and information whether the inlined content
 * is a <code>feed</code> (multiplicity of <code>1..m</code>) or a single <code>entry</code> (multiplicity of <code>0..1</code> or <code>1..1</code>).
 * If inlined navigation property is <code>nullable</code> the {@link ReadResult} has the 
 * <code>navigationPropertyName</code> and a <code>NULL</code> entry set.
 * 
 * @author SAP AG
 */
public abstract class ReadResult {

  protected final EntityProviderReadProperties readProperties;
  protected final EdmNavigationProperty navigationProperty;

  /**
   * Constructor.
   * Parameters <b>MUST NOT BE NULL</b>.
   * 
   * @param readProperties read properties which are used to read enclosing parent entity
   * @param navigationProperty emd navigation property information of found inline navigation property
   */
  public ReadResult(final EntityProviderReadProperties readProperties, final EdmNavigationProperty navigationProperty) {
    this.readProperties = readProperties;
    this.navigationProperty = navigationProperty;
  }

  /**
   * @return read properties which were used to read enclosing parent entity
   */
  public EntityProviderReadProperties getReadProperties() {
    return readProperties;
  }

  /**
   * @return emd navigation property information of found inline navigation property
   */
  public EdmNavigationProperty getNavigationProperty() {
    return navigationProperty;
  }

  /**
   * Common access method to read result.
   * 
   * @return an {@link com.sap.core.odata.api.ep.entry.ODataEntry ODataEntry}
   *         for the case of an single read entry or a list of
   *         {@link com.sap.core.odata.api.ep.entry.ODataEntry ODataEntry}
   *         in the case of an read feed.
   */
  public abstract Object getResult();

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + " [readProperties=" + readProperties + ", navigationProperty=" + navigationProperty + "]";
  }

  /**
   * Return whether this entry is a <code>feed</code> (multiplicity of <code>1..m</code>) 
   * or a single <code>entry</code> (multiplicity of <code>0..1</code> or <code>1..1</code>).
   * 
   * @return <code>true</code> for a feed and <code>false</code> for an entry
   */
  public abstract boolean isFeed();
}