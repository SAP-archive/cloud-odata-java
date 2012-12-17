package com.sap.core.odata.api.uri.expression;
/*TODO add docu*/

/**
 * Represents a $filter expression in the expression tree returned by {@link FilterParser#parseFilterString(String)}
 * Used to define the <b>root</b> expression node in an $filter expression tree. 
 * 
 * @author SAP AG
 * @see OrderByParser
 */
public interface FilterExpression extends CommonExpression
{
  
  /**
   * @return Returns the $filter expression string used to build the expression tree
   */
  String getExpressionString();

  /**
   * @return Returns the expression node representing the first <i>operator</i>,<i>method</i> or <i>property</i> of the expression tree
   */
  CommonExpression getExpression();
 
}
