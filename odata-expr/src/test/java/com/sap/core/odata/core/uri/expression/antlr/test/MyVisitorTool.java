package com.sap.core.odata.core.uri.expression.antlr.test;

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
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.SortOrder;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.api.uri.expression.UnaryOperator;

public class MyVisitorTool implements ExpressionVisitor {

  @Override
  public Object visitBinary(final BinaryExpression binaryExpression, final BinaryOperator operator, final Object leftSide, final Object rightSide) {
    return "{" + leftSide.toString() + " " + operator.toUriLiteral() + " " + rightSide.toString() + "}";
  }

  @Override
  public Object visitFilterExpression(final FilterExpression filterExpression, final String expressionString, final Object expression) {
    return expression;
  }

  @Override
  public Object visitLiteral(final LiteralExpression literal, final EdmLiteral edmLiteral) {
    return "" + literal.getUriLiteral() + "";
  }

  @Override
  public Object visitMethod(final MethodExpression methodExpression, final MethodOperator method, final List<Object> retParameters) {
    final StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append(method.toUriLiteral());

    sb.append("(");
    for (int i = 0; i < retParameters.size(); i++) {
      if (i != 0) {
        sb.append(",");
      }
      sb.append(retParameters.get(i));
    }

    sb.append(")}");

    return sb.toString();
  }

  @Override
  public Object visitMember(final MemberExpression memberExpression, final Object source, final Object path) {
    return "{" + source.toString() + "/" + path.toString() + "}";
  }

  @Override
  public Object visitProperty(final PropertyExpression literal, final String uriLiteral, final EdmTyped edmProperty) {
    return uriLiteral;
  }

  @Override
  public Object visitUnary(final UnaryExpression unaryExpression, final UnaryOperator operator, final Object operand) {
    return "{" + operator.toUriLiteral() + " " + operand.toString() + "}";
  }

  @Override
  public Object visitOrderByExpression(final OrderByExpression orderByExpression, final String expressionString, final List<Object> orders) {
    final StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("oc");

    sb.append("(");
    for (int i = 0; i < orders.size(); i++) {
      if (i != 0) {
        sb.append(",");
      }
      sb.append(orders.get(i));
    }

    sb.append(")}");

    return sb.toString();
  }

  @Override
  public Object visitOrder(final OrderExpression orderExpression, final Object filterResult, final SortOrder sortOrder) {
    return "{o(" + filterResult + ", " + sortOrder.toString() + ")}";
  }
}
