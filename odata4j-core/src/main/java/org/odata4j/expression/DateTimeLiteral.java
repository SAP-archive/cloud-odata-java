package org.odata4j.expression;

import org.joda.time.LocalDateTime;

public interface DateTimeLiteral extends LiteralExpression {

  LocalDateTime getValue();

}
