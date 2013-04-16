package com.sap.core.odata.api.uri.expression;

import com.sap.core.odata.api.edm.EdmTyped;

/**
 * Represents a property expression in the expression tree returned by the methods:
 * <li>{@link com.sap.core.odata.api.uri.UriParser#parseFilterString(com.sap.core.odata.api.edm.EdmEntityType, String)}</li>
 * <li>{@link com.sap.core.odata.api.uri.UriParser#parseOrderByString(com.sap.core.odata.api.edm.EdmEntityType, String)}</li>
 * <br>
 * <br>
 * <p>A property expression node is inserted in the expression tree for any property.
 * If an EDM is available during parsing the property is automatically verified 
 * against the EDM.
 * <br>
 * <br>
 * @author SAP AG
 */
public interface PropertyExpression extends CommonExpression {
  /**
   * @return the property name as used in the EDM
   */
  public String getPropertyName();

  /**
   * @return Returns the EDM property matching the property name used in the expression String.
   *   This may be an instance of EdmProperty or EdmNavigationProperty
   * @see EdmTyped    
   */
  public EdmTyped getEdmProperty();

}