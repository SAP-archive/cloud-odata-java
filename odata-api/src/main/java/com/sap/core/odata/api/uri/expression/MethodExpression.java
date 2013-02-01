package com.sap.core.odata.api.uri.expression;

import java.util.List;

import com.sap.core.odata.api.uri.UriParser;

/**
 * Represents a method expression in the expression tree returned by the methods:
 * <li>{@link UriParser#parseFilterString(com.sap.core.odata.api.edm.EdmEntityType, String) }</li>
 * <li>{@link UriParser#parseOrderByString(com.sap.core.odata.api.edm.EdmEntityType, String) }</li> 
 * <br>
 * <br>
 * <p>A method expression node is inserted in the expression tree for any valid
 * OData method operator in {@link MethodOperator} (e.g. for "substringof", "concat", "year", ... )
 * <br>
 * <br>
 * @author SAP AG
 */
public interface MethodExpression extends CommonExpression
{

  /**
   * @return Returns the method object that represents the used method 
   * @see MethodOperator
   */
  public MethodOperator getMethod();

  /**
   * @return Returns the number of provided method parameters
   */
  public int getParameterCount();

  /**
   * @return Returns a ordered list of expressions defining the input parameters for the used method
   * @see CommonExpression
   */
  public List<CommonExpression> getParameters();

}