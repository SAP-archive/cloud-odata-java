package org.odata4j.expression;

import java.math.BigDecimal;

public interface DecimalLiteral extends LiteralExpression {

  BigDecimal getValue();

}
