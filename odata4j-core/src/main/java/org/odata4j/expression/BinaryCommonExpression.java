package org.odata4j.expression;

public interface BinaryCommonExpression extends CommonExpression {

  CommonExpression getLHS();

  CommonExpression getRHS();

}
