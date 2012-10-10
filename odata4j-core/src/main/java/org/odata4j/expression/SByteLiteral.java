package org.odata4j.expression;

/** -128 (0x80) to 127 (0x7F) */
public interface SByteLiteral extends LiteralExpression {

  byte getValue();

}