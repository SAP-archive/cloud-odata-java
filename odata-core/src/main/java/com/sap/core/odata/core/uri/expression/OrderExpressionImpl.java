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
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.OrderExpression;
import com.sap.core.odata.api.uri.expression.SortOrder;

public class OrderExpressionImpl implements OrderExpression {

  SortOrder orderType = SortOrder.asc;
  CommonExpression expression;

  OrderExpressionImpl(final CommonExpression expression)
  {
    this.expression = expression;
  }

  @Override
  public SortOrder getSortOrder()
  {
    return orderType;
  }

  @Override
  public CommonExpression getExpression()
  {
    return expression;
  }

  void setSortOrder(final SortOrder orderType)
  {
    this.orderType = orderType;
  }

  @Override
  public ExpressionKind getKind()
  {
    return ExpressionKind.ORDER;
  }

  @Override
  public EdmType getEdmType()
  {
    return null;
  }

  @Override
  public CommonExpression setEdmType(final EdmType edmType)
  {
    return this;
  }

  @Override
  public String getUriLiteral()
  {
    return "";
  }

  @Override
  public Object accept(final ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException
  {
    Object obj = expression.accept(visitor);
    Object ret = visitor.visitOrder(this, obj, orderType);
    return ret;
  }

}
