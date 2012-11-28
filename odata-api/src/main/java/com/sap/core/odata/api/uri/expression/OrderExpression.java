package com.sap.core.odata.api.uri.expression;

/**
 * Represents a order expression in the expression tree returned by the method 
 * <li>{@link OrderByParser#parseOrderExpression(String)}</li> 
 * <br>
 * <br>
 * A order expression node is inserted in the expression tree for any valid
 * ODATA order. For example for "$orderby=age desc,name asc" two order expression node
 * will be inserted into the expression tree
 * <br>
 * <br>
 * @author SAP AG
 * @see {@link OrderByParser}
 */
public interface OrderExpression 
{
  void getSortOrder();
  void getExpression();
}
