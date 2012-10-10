package org.odata4j.expression;

public interface IndexOfMethodCallExpression extends MethodCallExpression {

  CommonExpression getTarget();

  CommonExpression getValue();

}
