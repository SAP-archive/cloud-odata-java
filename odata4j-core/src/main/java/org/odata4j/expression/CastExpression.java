package org.odata4j.expression;

public interface CastExpression extends CommonExpression {

  CommonExpression getExpression(); // optional

  String getType();

}
