package com.sap.core.odata.api.uri.expression;


public interface Visitable {
  Object accept(ExpressionVisitor visitor);
}
