package com.sap.core.odata.core.uri.expression;

import java.util.Vector;

import com.sap.core.odata.api.uri.expression.BinaryOperator;

/**
 * Describes a binary operator which is allowed in OData expressions
 */
class InfoBinaryOperator
{
  
  BinaryOperator operator;
  String category;
  String syntax;
  int priority;
  Vector<ParameterSet> allowedParameterTypes;
  
  public InfoBinaryOperator(BinaryOperator operator, String category, String syntax, int priority) {
    this.allowedParameterTypes = new Vector<ParameterSet>();
    
    this.operator = operator; 
    this.category = category;
    this.syntax = syntax;
    this.priority = priority;
  }
  

  public String getCategory() {
    return this.category;
  }

  public String getSyntax() {
    return this.syntax;
  }
  
  public void addParameterSet(ParameterSet parameterSet)
  {
    this.allowedParameterTypes.add(parameterSet);
  }


  public BinaryOperator getOperator() {
    return operator;
  }
}