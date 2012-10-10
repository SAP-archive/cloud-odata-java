package org.odata4j.expression;

import org.odata4j.core.Guid;

public interface GuidLiteral extends LiteralExpression {

  Guid getValue();

}
