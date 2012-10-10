package org.odata4j.expression;

public interface BinaryBoolCommonExpression extends CommonExpression {

  BoolCommonExpression getLHS();

  BoolCommonExpression getRHS();

}
