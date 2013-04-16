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

import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.LiteralExpression;

public class LiteralExpressionImpl implements LiteralExpression {

  private EdmType edmType;
  private EdmLiteral edmLiteral;
  private String uriLiteral;

  public LiteralExpressionImpl(final String uriLiteral, final EdmLiteral javaLiteral) {
    this.uriLiteral = uriLiteral;
    edmLiteral = javaLiteral;
    edmType = edmLiteral.getType();
  }

  @Override
  public EdmType getEdmType()
  {
    return edmType;
  }

  @Override
  public CommonExpression setEdmType(final EdmType edmType)
  {
    this.edmType = edmType;
    return this;
  }

  @Override
  public ExpressionKind getKind()
  {
    return ExpressionKind.LITERAL;
  }

  @Override
  public String getUriLiteral()
  {
    return uriLiteral;
  }

  @Override
  public Object accept(final ExpressionVisitor visitor)
  {
    Object ret = visitor.visitLiteral(this, edmLiteral);
    return ret;
  }

}
