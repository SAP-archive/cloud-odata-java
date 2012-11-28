package com.sap.core.odata.api.uri.expression;

/**
 * Represents a member expression in the expression tree returned by the methods 
 * <li>{@link FilterParser#ParseExpression(String)}</li>
 * <li>{@link OrderByParser#parseOrderExpression(String)}</li> 
 * <br>
 * <br>
 * A member expression node is inserted in the expression tree for any member operator ("/") 
 * which is used to reference a property of an complex type.
 * For example the expression "address/city eq 'Heidelberg' will result in an expression tree
 * containing a member expression node for access to property "city" which is part of the 
 * complex property "address" 
 * <br>
 * <br>
 * @author SAP AG
 */
public interface MemberExpression extends CommonExpression
{
  public CommonExpression getSource();

  public CommonExpression getPath();
}