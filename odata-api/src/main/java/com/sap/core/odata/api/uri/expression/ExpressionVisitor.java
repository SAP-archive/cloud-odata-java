package com.sap.core.odata.api.uri.expression;

import java.util.Vector;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.uri.EdmLiteral;

public interface ExpressionVisitor
{
  Object visitBinary(BinaryExpression binaryExpression, BinaryOperator operator, Object leftSide, Object rightSide);
  Object visitFilterExpression(FilterExpression filterExpression, String expressionString, Object expression);
  Object visitLiteral(LiteralExpression literal, EdmLiteral edmLiteral);
  Object visitMethod(MethodExpression methodExpression, MethodOperator method, Vector<Object> retParameters);
  Object visitMember(MemberExpression memberExpression, Object source, Object path);
  Object visitProperty(PropertyExpression literal, String uriLiteral, EdmProperty edmProperty);
  Object visitUnary(UnaryExpression unaryExpression, UnaryOperator operator, Object operand);
}
