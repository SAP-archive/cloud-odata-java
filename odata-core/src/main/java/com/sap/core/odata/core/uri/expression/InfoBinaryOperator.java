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

  public InfoBinaryOperator(BinaryOperator operator, String category, String syntax, int priority,   ParameterSetCombination combination) {
    this.operator = operator;
    this.category = category;
    this.syntax = syntax;
    this.priority = priority;
    this.combination = combination;
  }

  public String getCategory() {
    return this.category;
  }

  public String getSyntax() {
    return this.syntax;
  }

  public BinaryOperator getOperator() {
    return operator;
  }

  public int getPriority() {
    return priority;
  }

  public EdmType validateParameterSet(List<EdmType> actualParameterTypes) throws FilterParserInternalError {
    return combination.validate(actualParameterTypes);
  }

 

}