/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.uri.expression;

import java.util.List;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.expression.BinaryOperator;

/**
 * Describes a binary operator which is allowed in OData expressions
 * @author SAP AG
 */
class InfoBinaryOperator
{
  private BinaryOperator operator;
  private String category;
  private String syntax;
  private int priority;
  ParameterSetCombination combination;

  public InfoBinaryOperator(final BinaryOperator operator, final String category, final int priority, final ParameterSetCombination combination)
  {
    this.operator = operator;
    this.category = category;
    syntax = operator.toUriLiteral();
    this.priority = priority;
    this.combination = combination;
  }

  public String getCategory()
  {
    return category;
  }

  public String getSyntax()
  {
    return syntax;
  }

  public BinaryOperator getOperator()
  {
    return operator;
  }

  public int getPriority()
  {
    return priority;
  }

  public ParameterSet validateParameterSet(final List<EdmType> actualParameterTypes) throws ExpressionParserInternalError
  {
    return combination.validate(actualParameterTypes);
  }

}
