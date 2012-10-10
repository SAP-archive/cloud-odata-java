package org.odata4j.expression;

public interface BoolMethodExpression extends MethodCallExpression, BoolCommonExpression {

  CommonExpression getTarget();

  CommonExpression getValue();

}
