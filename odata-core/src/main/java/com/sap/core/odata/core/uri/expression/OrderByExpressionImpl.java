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

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderExpression;

public class OrderByExpressionImpl implements OrderByExpression {
  private String orderbyString;

  List<OrderExpression> orders;

  public OrderByExpressionImpl(final String orderbyString)
  {
    this.orderbyString = orderbyString;
    orders = new ArrayList<OrderExpression>();
  }

  @Override
  public String getExpressionString()
  {
    return orderbyString;
  }

  @Override
  public List<OrderExpression> getOrders()
  {
    return orders;
  }

  @Override
  public int getOrdersCount()
  {
    return orders.size();
  }

  public void addOrder(final OrderExpression orderNode)
  {
    orders.add(orderNode);
  }

  @Override
  public ExpressionKind getKind()
  {
    return ExpressionKind.ORDERBY;
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
    ArrayList<Object> retParameters = new ArrayList<Object>();
    for (OrderExpression order : orders)
    {
      Object retParameter = order.accept(visitor);
      retParameters.add(retParameter);
    }

    Object ret = visitor.visitOrderByExpression(this, orderbyString, retParameters);
    return ret;
  }

}
