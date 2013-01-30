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

  public OrderByExpressionImpl(String orderbyString)
  {
    this.orderbyString = orderbyString;
    this.orders = new ArrayList<OrderExpression>();
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

  public void addOrder(OrderExpression orderNode) 
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
  public CommonExpression setEdmType(EdmType edmType) 
  {
    return this;
  }

  @Override
  public String getUriLiteral() 
  {
    return "";
  }

  @Override
  public Object accept(ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException
  {
    ArrayList<Object> retParameters = new ArrayList<Object>();
    for (OrderExpression order : orders)
    {
      Object retParameter = order.accept(visitor);
      retParameters.add(retParameter);
    }

    Object ret = visitor.visitOrderByExpression(this, this.orderbyString, retParameters);
    return ret;
  }

}
