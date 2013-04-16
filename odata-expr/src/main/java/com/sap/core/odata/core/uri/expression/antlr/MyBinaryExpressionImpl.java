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

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;

public class MyBinaryExpressionImpl implements BinaryExpression {
  private final BinaryOperator operator;
  CommonExpression leftSide;
  CommonExpression rightSide;
  EdmType edmType;

  public MyBinaryExpressionImpl(final BinaryOperator operator, final CommonExpression leftSide, final CommonExpression rightSide) {
    this.operator = operator;
    this.leftSide = leftSide;
    this.rightSide = rightSide;
    edmType = null;

  }

  @Override
  public BinaryOperator getOperator() {
    return operator;
  }

  @Override
  public CommonExpression getLeftOperand() {
    return leftSide;
  }

  @Override
  public CommonExpression getRightOperand() {
    return rightSide;
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
  public ExpressionKind getKind() {
    return ExpressionKind.BINARY;
  }

  @Override
  public String getUriLiteral() {
    return null;
  }

  @Override
  public Object accept(final ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException {
    final Object retLeftSide = leftSide.accept(visitor);
    final Object retRightSide = rightSide.accept(visitor);

    final Object ret = visitor.visitBinary(this, operator, retLeftSide, retRightSide);
    return ret;
  }

}
