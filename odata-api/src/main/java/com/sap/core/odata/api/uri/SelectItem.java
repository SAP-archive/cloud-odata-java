package com.sap.core.odata.api.uri;

import java.util.List;

import com.sap.core.odata.api.edm.EdmTyped;

/**
 * An item of a $select system query option.
 * @com.sap.core.odata.DoNotImplement
 * @author SAP AG
 */
public interface SelectItem {

  /**
   * <code>true</code> if select=*
   * @return <code>true</code> if select=*
   */
  public boolean isStar();

  /**
   * Gets the EDM property.
   * @return {@link EdmTyped} property of this select item
   */
  public EdmTyped getProperty();

  /**
   * Gets the navigation-property segments for this select item.
   * @return List of {@link NavigationPropertySegment} for this select item
   */
  public List<NavigationPropertySegment> getNavigationPropertySegments();

}
