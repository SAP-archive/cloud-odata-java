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
package com.sap.core.odata.core.uri.expression.antlr;

/*1*/

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.api.uri.expression.UnaryOperator;

public class MyUnaryExpressionImpl implements UnaryExpression {
  private UnaryOperator operator = null;
  private CommonExpression operand = null;
  private EdmType edmType = null;

  public MyUnaryExpressionImpl(final UnaryOperator operator, final CommonExpression operand) {
    this.operator = operator;
    this.operand = operand;
    //   this.edmType = operatorInfo.getReturnType();
  }

  @Override
  public ExpressionKind getKind() {
    return ExpressionKind.UNARY;
  }

  @Override
  public UnaryOperator getOperator() {
    return operator;
  }

  @Override
  public CommonExpression getOperand() {
    return operand;
  }

  @Override
  public EdmType getEdmType() {
    return edmType;
  }

  @Override
  public CommonExpression setEdmType(final EdmType edmType) {
    this.edmType = edmType;
    return this;
  }

  @Override
  public String getUriLiteral() {
    return null;

  }

  @Override
  public Object accept(final ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException {
    final Object retOperand = operand.accept(visitor);

    final Object ret = visitor.visitUnary(this, operator, retOperand);
    return ret;
  }

}
