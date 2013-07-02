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

import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmType;

public class Token {

  private TokenKind kind;
  private int position;
  private String uriLiteral;
  private EdmLiteral javaLiteral;

  public Token(final TokenKind kind, final int position, final String uriLiteral, final EdmLiteral javaLiteral) {
    this.kind = kind;
    this.position = position;
    this.uriLiteral = uriLiteral;
    this.javaLiteral = javaLiteral;
  }

  public Token(final TokenKind kind, final int position, final String uriLiteral) {
    this.kind = kind;
    this.position = position;
    this.uriLiteral = uriLiteral;
    javaLiteral = null;
  }

  public TokenKind getKind() {
    return kind;
  }

  public int getPosition() {
    return position;
  }

  public EdmType getEdmType() {
    if (javaLiteral == null) {
      return null;
    }
    return javaLiteral.getType();
  }

  public String getUriLiteral() {
    return uriLiteral;
  }

  public EdmLiteral getJavaLiteral() {
    return javaLiteral;
  }
}
