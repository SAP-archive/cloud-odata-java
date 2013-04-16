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

import com.sap.core.odata.api.edm.EdmType;

/**
 * Parent class of all classes used to build the expression tree returned by 
 * <li>{@link com.sap.core.odata.api.uri.UriParser#parseFilterString(com.sap.core.odata.api.edm.EdmEntityType, String)}</li>
 * <li>{@link com.sap.core.odata.api.uri.UriParser#parseOrderByString(com.sap.core.odata.api.edm.EdmEntityType, String)}</li>
 * <br>
 * <br>
 * <p>This interface defines the default methods for all expression tree nodes 
 * <br>
 * <br>
 * @author SAP AG
 */
public interface CommonExpression extends Visitable {
  /**
   * @return Kind of this expression
   * @see ExpressionKind
   */
  ExpressionKind getKind();

  /**
   * @return The return type of the value represented with 
   * this expression. For example the {@link #getEdmType()} method
   * for an expression representing the "concat" method will return always 
   * "Edm.String".<br>
   * <br>
   * <p>This type information is set while parsing the $filter or $orderby 
   * expressions and used to do a first validation of the expression.
   * For calculating operators like "add, sub, mul" this type
   * information is purely based on input and output types of the 
   * operator as defined in the OData specification.
   * So for $filter=2 add 7 the {@link #getEdmType()} method of the binary expression 
   * will return Edm.Byte and not Edm.Int16 because the parser performs no real 
   * addition.<br>
   * <br>
   * <p>However, the application may change this type while evaluating the 
   * expression tree.
   */
  EdmType getEdmType();

  /**
   * Set the edmType of this expression node
   * @param edmType Type to be set
   * @return A self reference for method chaining"
   */
  CommonExpression setEdmType(EdmType edmType);

  /**
   * Returns the URI literal which lead to the creation of this expression.
   * @return URI literal
   */
  String getUriLiteral();
}
