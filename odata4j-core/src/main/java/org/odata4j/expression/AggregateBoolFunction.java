package org.odata4j.expression;

/**
 <source>/<any|all>(<variable>:<predicate>)
 */
public interface AggregateBoolFunction extends BoolCommonExpression {

  CommonExpression getSource(); // .NET docs use this terminology

  String getVariable();

  BoolCommonExpression getPredicate();

  ExpressionParser.AggregateFunction getFunctionType();

}
