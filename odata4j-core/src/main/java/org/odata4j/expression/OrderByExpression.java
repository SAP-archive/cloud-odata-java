package org.odata4j.expression;

public interface OrderByExpression extends CommonExpression {

  public enum Direction {
    ASCENDING, DESCENDING
  }

  CommonExpression getExpression();

  Direction getDirection();

}
