package com.sap.core.odata.api.uri.expression;

import java.util.Vector;

public interface ExpressionVisitor
{
  Object visitLiteral(LiteralExpression literal);
  Object visitProperty(PropertyExpression literal);
  Object visitUnary(String Operator, UnaryExpression literal);
  Object VisitBinary(String Operator, Object leftSide, Object rightSide);
  Object VisitMethod(String Method, Vector<String> parameter);
  Object VisitMember(String Member, Object source, Object path);
}