package org.odata4j.expression;

import org.joda.time.DateTime;

public interface DateTimeOffsetLiteral extends LiteralExpression {

  DateTime getValue();

}
