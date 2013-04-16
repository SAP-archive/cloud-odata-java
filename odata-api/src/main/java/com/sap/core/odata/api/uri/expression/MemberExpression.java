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
 * Represents a member expression in the expression tree returned by the methods:
 * <li>{@link com.sap.core.odata.api.uri.UriParser#parseFilterString(com.sap.core.odata.api.edm.EdmEntityType, String)}</li>
 * <li>{@link com.sap.core.odata.api.uri.UriParser#parseOrderByString(com.sap.core.odata.api.edm.EdmEntityType, String)}</li> 
 * <br>
 * <br>
 * <p>A member expression node is inserted in the expression tree for any member operator ("/") 
 * which is used to reference a property of an complex type or entity type.
 * <br>
 * <br>
 * <p><b>For example:</b> The expression "address/city eq 'Heidelberg' will result in an expression tree
 * containing a member expression node for accessing property "city" which is part of the 
 * complex property "address". Method {@link #getPath()} will return a reference to the "address" property,
 * method {@link #getProperty()} will return a refence to the "city" property.
 * @author SAP AG
 */
public interface MemberExpression extends CommonExpression {
  /**
   * @return 
   *   Returns the CommonExpression forming the path (the left side of '/') of the method operator.
   *   For OData 2.0 the value returned by {@link #getPath()} is a {@link PropertyExpression}   
   */
  public CommonExpression getPath();

  /**
   * @return 
   *   Return the CommonExpression forming the property (the right side of '/') of the method operator.
   *   For OData 2.0 the value returned by {@link #getProperty()} is a {@link PropertyExpression}
   */
  public CommonExpression getProperty();
}
