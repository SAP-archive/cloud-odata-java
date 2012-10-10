package org.odata4j.expression;

import org.joda.time.LocalTime;

public interface TimeLiteral extends LiteralExpression {

  LocalTime getValue();

}
