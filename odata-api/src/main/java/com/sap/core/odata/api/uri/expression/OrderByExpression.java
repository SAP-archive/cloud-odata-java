package com.sap.core.odata.api.uri.expression;

import java.util.List;

/**
 * Represents a $orderby expression in the expression tree returned by
 * {@link com.sap.core.odata.api.uri.UriParser#parseOrderByString(com.sap.core.odata.api.edm.EdmEntityType, String) }
 * Used to define the <b>root</b> expression node in an $filter expression tree.
 * 
 * @author SAP AG
 */
public interface OrderByExpression extends CommonExpression {
  /**
   * @return Returns the $filter expression string used to build the expression tree
   */
  String getExpressionString();

  /**
   * @return 
   *   Returns a ordered list of order expressions contained in the $orderby expression string
   *   <p>
   *   <b>For example</b>: The orderby expression build from "$orderby=name asc, age desc" 
   *   would contain to order expression.  
   */
  public List<OrderExpression> getOrders();

  /**
   * @return Returns the count of order expressions contained in the $orderby expression string
   */
  public int getOrdersCount();

}
