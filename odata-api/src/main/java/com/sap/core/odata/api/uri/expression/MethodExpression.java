package com.sap.core.odata.api.uri.expression;

import java.util.Vector;

/**
 * Represents a method expression in the expression tree returned by the methods 
 * <li>{@link FilterParser#ParseExpression(String)}</li>
 * <li>{@link OrderByParser#parseOrderExpression(String)}</li> 
 * <br>
 * <br>
 * A method expression node is inserted in the expression tree for any valid
 * ODATA methodoperator in {@link MethodOperator} (e.g. for "substringof", "concat", "year", ... )
 * <br>
 * <br>
 * @author SAP AG
 * @see {@link FilterParser}
 * @see {@link OrderByParser}
 */
public interface MethodExpression extends CommonExpression
{

  /**
   * @return Method
   * @see MethodOperator
   */
  public MethodOperator getMethod();
  
  public int getParameterCount();

  public Vector<CommonExpression> getParameters();

  
}