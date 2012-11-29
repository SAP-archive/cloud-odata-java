package com.sap.core.odata.core.uri.expression.test;

import java.util.Vector;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.uri.EdmLiteral;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MemberExpression;
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.MethodOperator;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.api.uri.expression.UnaryOperator;

public class TestVisitor implements ExpressionVisitor {

  @Override
  public Object visitBinary(BinaryExpression binaryExpression, BinaryOperator operator, Object leftSide, Object rightSide)
  {
    return "{" + leftSide.toString() + " " + operator.toSyntax() + " " + rightSide.toString() + "}";
  }

  @Override
  public Object visitFilterExpression(FilterExpression filterExpression, String expressionString, Object expression) {
    return expression;
  }

  @Override
  public Object visitLiteral(LiteralExpression literal, EdmLiteral edmLiteral)
  {
    return "" + literal.toUriLiteral() + "";
  }

  @Override
  public Object visitMethod(MethodExpression methodExpression, MethodOperator method, Vector<Object> retParameters)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append(method.toString());

    for (int i = 0; i < retParameters.size(); i++)
    {
      if (i != 0) sb.append(",");
      sb.append(retParameters.elementAt(i));
    }

    sb.append("}");

    return sb.toString();
  }

  @Override
  public Object visitMember(MemberExpression memberExpression, Object source, Object path)
  {
    return "{" + source.toString() + "/" + path.toString() + "}";
  }

  @Override
  public Object visitProperty(PropertyExpression literal, String uriLiteral, EdmProperty edmProperty)
  {
    return uriLiteral;
  }

  @Override
  public Object visitUnary(UnaryExpression unaryExpression, UnaryOperator operator, Object operand)
  {
    return "{" + operator.toSyntax() + " " + operand.toString() + "}";
  }

}
