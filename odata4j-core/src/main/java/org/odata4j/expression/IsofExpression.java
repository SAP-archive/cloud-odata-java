package org.odata4j.expression;

public interface IsofExpression extends BoolCommonExpression {

  CommonExpression getExpression(); // optional

  String getType();

}
