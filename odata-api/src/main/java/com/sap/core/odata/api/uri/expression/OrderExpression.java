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

/**
 * Represents a order expression in the expression tree returned by the method 
 * <li>{@link com.sap.core.odata.api.uri.UriParser#parseOrderByString(com.sap.core.odata.api.edm.EdmEntityType, String) }</li> 
 * <br>
 * <br>
 * <p>A order expression node is inserted in the expression tree for any valid
 * OData order. For example for "$orderby=age desc, name asc" two order expression node
 * will be inserted into the expression tree
 * <br>
 * <br>
 * @author SAP AG
 */
public interface OrderExpression extends CommonExpression {

  /**
   * @return Returns the sort order (ascending or descending) of the order expression  
   */
  SortOrder getSortOrder();

  /**
   * @return Returns the expression node which defines the data used to order the output
   * send back to the client. In the simplest case this would be a {@link PropertyExpression}.
   * @see CommonExpression
   */
  CommonExpression getExpression();

}
