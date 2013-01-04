package com.sap.core.odata.core.uri.expression;

import java.util.List;

import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MemberExpression;
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.MethodOperator;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderExpression;
import com.sap.core.odata.api.uri.expression.SortOrder;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.api.uri.expression.UnaryOperator;

public class VisitorTool implements ExpressionVisitor {

  @Override
  public Object visitBinary(BinaryExpression binaryExpression, BinaryOperator operator, Object leftSide, Object rightSide)
  {
    return "{" + leftSide.toString() + " " + operator.toUriLiteral() + " " + rightSide.toString() + "}";
  }

  @Override
  public Object visitFilterExpression(FilterExpression filterExpression, String expressionString, Object expression) {
    return expression;
  }

  @Override
  public Object visitLiteral(LiteralExpression literal, EdmLiteral edmLiteral)
  {
    return "" + literal.getUriLiteral() + "";
  }

  @Override
  public Object visitMethod(MethodExpression methodExpression, MethodOperator method, List<Object> retParameters)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append(method.toUriLiteral());

    sb.append("(");
    for (int i = 0; i < retParameters.size(); i++)
    {
      if (i != 0) sb.append(",");
      sb.append(retParameters.get(i));
    }

    sb.append(")}");

    return sb.toString();
  }

  @Override
  public Object visitMember(MemberExpression memberExpression, Object source, Object path)
  {
    return "{" + source.toString() + "/" + path.toString() + "}";
  }

  @Override
  public Object visitProperty(PropertyExpression literal, String uriLiteral, EdmTyped edmProperty)
  {
    return uriLiteral;
  }

  @Override
  public Object visitUnary(UnaryExpression unaryExpression, UnaryOperator operator, Object operand)
  {
    return "{" + operator.toUriLiteral() + " " + operand.toString() + "}";
  }

  @Override
  public Object visitOrderByExpression(OrderByExpression orderByExpression, String expressionString, List<Object> orders) 
  {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("oc");

    sb.append("(");
    for (int i = 0; i < orders.size(); i++)
    {
      if (i != 0) sb.append(",");
      sb.append(orders.get(i));
    }

    sb.append(")}");

    return sb.toString();
  }

  @Override
  public Object visitOrder(OrderExpression orderExpression, Object filterResult, SortOrder sortOrder)
  {
    return "{o(" + filterResult + ", " + sortOrder.toString() + ")}";
  }

}
