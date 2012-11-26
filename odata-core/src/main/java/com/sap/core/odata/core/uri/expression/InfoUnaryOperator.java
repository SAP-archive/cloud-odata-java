package com.sap.core.odata.core.uri.expression;

import java.util.Vector;

import com.sap.core.odata.api.uri.expression.UnaryOperator;


/**
 * Describes a unary operator which is allowed in OData expressions
 */
class InfoUnaryOperator
{
  UnaryOperator operator;
  private String category;
  private String syntax;
  private Vector<ParameterSet> allowedParameterTypes = null;
  

  public InfoUnaryOperator(UnaryOperator operator, String category, String syntax) {
    allowedParameterTypes = new Vector<ParameterSet>();
    this.operator = operator;
    this.category = category;
    this.syntax = syntax;
  }
  
  public String getCategory() {
    return category;
  }

  public String getSyntax() {
    return syntax;
  }
  
  public void addParameterSet(ParameterSet parameterSet)
  {
    this.allowedParameterTypes.add(parameterSet);
  }
}