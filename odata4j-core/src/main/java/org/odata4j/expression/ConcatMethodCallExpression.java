package org.odata4j.expression;

public interface ConcatMethodCallExpression extends MethodCallExpression {

  CommonExpression getLHS();

  CommonExpression getRHS();

}
