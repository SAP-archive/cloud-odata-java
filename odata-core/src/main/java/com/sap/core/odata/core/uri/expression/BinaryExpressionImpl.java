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

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;

public class BinaryExpressionImpl implements BinaryExpression
{
  final protected InfoBinaryOperator operatorInfo;
  final protected CommonExpression leftSide;
  final protected CommonExpression rightSide;
  final protected Token token;
  protected EdmType edmType;

  public BinaryExpressionImpl(final InfoBinaryOperator operatorInfo, final CommonExpression leftSide, final CommonExpression rightSide, final Token token) {
    this.operatorInfo = operatorInfo;
    this.leftSide = leftSide;
    this.rightSide = rightSide;
    this.token = token;
    edmType = null;
  }

  @Override
  public BinaryOperator getOperator()
  {
    return operatorInfo.getOperator();
  }

  @Override
  public CommonExpression getLeftOperand()
  {
    return leftSide;
  }

  @Override
  public CommonExpression getRightOperand()
  {
    return rightSide;
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
    return ExpressionKind.BINARY;
  }

  @Override
  public String getUriLiteral()
  {
    return operatorInfo.getSyntax();
  }

  @Override
  public Object accept(final ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException
  {
    Object retLeftSide = leftSide.accept(visitor);
    Object retRightSide = rightSide.accept(visitor);

    return visitor.visitBinary(this, operatorInfo.getOperator(), retLeftSide, retRightSide);
  }

  public Token getToken()
  {
    return token;
  }

}
