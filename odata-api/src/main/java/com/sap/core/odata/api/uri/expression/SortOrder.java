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
 * Enumeration describing all possible sort orders used in an $orderby expression 
 * @author SAP AG
 */
public enum SortOrder {

  /**
   * Sort order ascending 
   */
  asc("asc"),

  /**
   * Sort order descending 
   */
  desc("desc");

  private String syntax;
  private String stringRespresentation;

  private SortOrder(final String syntax) {
    this.syntax = syntax;
    stringRespresentation = syntax;
  }

  private SortOrder(final String syntax, final String stringRespresentation) {
    this.syntax = syntax;
    this.stringRespresentation = stringRespresentation;
  }

  /** 
   * @return Operators name for usage in in text
   */
  @Override
  public String toString() {
    return stringRespresentation;
  }

  /**
   * @return URI literal of the unary operator as used in the URL. 
   */
  public String toUriLiteral() {
    return syntax;
  }
}
