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
 * Enumerations for supported binary operators of the ODATA expression parser 
 * for ODATA version 2.0 (with some restrictions)
 * @author SAP AG
*/
public enum BinaryOperator {
  AND("and"), OR("or"), EQ("eq"), NE("ne"), LT("lt"), LE("le"), GT("gt"), GE("ge"), ADD("add"), SUB("sub"), MUL("mul"), DIV("div"), MODULO("mod"),

  /**
   * Property access operator. E.g. $filter=address/city eq "Sydney"
   */
  PROPERTY_ACCESS("/", "property access");

  private String uriSyntax;
  private String stringRespresentation;

  private BinaryOperator(final String uriSyntax) {
    this.uriSyntax = uriSyntax;
    stringRespresentation = uriSyntax;
  }

  private BinaryOperator(final String syntax, final String stringRespresentation) {
    uriSyntax = syntax;
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
    return uriSyntax;
  }
}
