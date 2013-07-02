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
package com.sap.core.odata.core.uri.expression;

/*1*/

/**
 * The token kind is used to categorize a single {@link Token}.
 * The Expression parser ({@link FilterParserImpl}) uses this information 
 * to build the expression tree.
 */
public enum TokenKind {
  /**
   * Indicates that the token is a whitespace character
   */
  WHITESPACE,

  /**
   * Indicates that the token is a '(' character
   */
  OPENPAREN,

  /**
   * Indicates that the token is a ')' character
   */
  CLOSEPAREN,

  /**
   * Indicates that the token is a ',' character
   */
  COMMA,

  /**
   * Indicates that the token is a typed literal. That may be a 
   * Edm.String like 'TEST'
   * Edm.Double like '1.1D' 
   * or any other Edm.Simple Type
   */
  SIMPLE_TYPE,

  /**
   * Indicates that the token is a single symbol. That may be a
   * '-', '=', '/', '?', '.' or a '*' character  
   */
  SYMBOL,

  /**
   * Indicates that the token is a set of alphanumeric characters starting
   * with a letter
   */
  LITERAL, TYPED_LITERAL_TODO_CHECK, UNKNOWN

}
