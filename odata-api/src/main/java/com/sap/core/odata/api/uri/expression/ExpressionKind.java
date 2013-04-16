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
 * Enumeration describing all possible node types inside an expression tree
 * @author SAP AG
 */
public enum ExpressionKind {
  /**
   * Used to mark the root node of a filter expression tree
   * @see FilterExpression  
   */
  FILTER,
  /**
   * Literal expressions like "1.1d" or "'This is a string'"
   * @see LiteralExpression 
   */
  LITERAL,

  /**
   * Unary operator expressions like "not" and "-"
   * @see UnaryExpression
   */
  UNARY,

  /**
   * Binary operator expressions like "eq" and "or" 
   * @see BinaryExpression
   */
  BINARY,

  /**
   * Method operator expressions like "substringof" and "concat" 
   * @see MethodExpression
   */
  METHOD,

  /**
   * Member access expressions like "/" in "adress/street"
   * @see MemberExpression 
   */
  MEMBER,

  /**
   * Property expressions like "age" 
   * @see PropertyExpression
   */
  PROPERTY,

  /**
   * Order expressions like "age desc" 
   * @see OrderExpression
   */
  ORDER,

  /**
   * Orderby expression like "age desc, name asc" 
   * @see OrderByExpression
   */
  ORDERBY;
}
