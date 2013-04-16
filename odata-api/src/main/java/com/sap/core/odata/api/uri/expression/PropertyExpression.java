/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
