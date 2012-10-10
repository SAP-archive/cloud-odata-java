package org.odata4j.expression;

public interface ReplaceMethodCallExpression extends MethodCallExpression {

  CommonExpression getTarget();

  CommonExpression getFind();

  CommonExpression getReplace();

}
