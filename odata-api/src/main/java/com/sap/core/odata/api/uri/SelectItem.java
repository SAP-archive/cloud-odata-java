package com.sap.core.odata.api.uri;

import java.util.List;

import com.sap.core.odata.api.edm.EdmTyped;

/**
 * @author SAP AG
 * Select item interface
 */
public interface SelectItem {

  /**
   * true if select=*
   * @return 
   */
  public boolean isStar();

  /**
   * @return {@link EdmTyped} property of this select item
   */
  public EdmTyped getProperty();
  

  /**
   * @return List of {@link NavigationPropertySegment} for this select item
   */
  public List<NavigationPropertySegment> getNavigationPropertySegments();

}
