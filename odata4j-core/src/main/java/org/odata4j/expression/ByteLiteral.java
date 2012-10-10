package org.odata4j.expression;

import org.odata4j.core.UnsignedByte;

/** 0 (0x00) to 255 (0xFF) */
public interface ByteLiteral extends LiteralExpression {

  UnsignedByte getValue();

}
