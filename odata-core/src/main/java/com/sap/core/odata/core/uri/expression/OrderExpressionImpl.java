/**
 * (c) 2013 by SAP AG
 */
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
