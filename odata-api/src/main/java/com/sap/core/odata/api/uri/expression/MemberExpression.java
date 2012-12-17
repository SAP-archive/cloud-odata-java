package com.sap.core.odata.api.uri.expression;

/**
 * Represents a member expression in the expression tree returned by the methods:
 * <li>{@link FilterParser#ParseExpression(String)}</li>
 * <li>{@link OrderByParser#parseOrderExpression(String)}</li> 
 * <br>
 * <br>
 * <p>A member expression node is inserted in the expression tree for any member operator ("/") 
 * which is used to reference a property of an complex type or entity type.
 * <br>
 * <br>
 * <p><b>For example:</b> The expression "address/city eq 'Heidelberg' will result in an expression tree
 * containing a member expression node for accessing property "city" which is part of the 
 * complex property "address" 
 * @author SAP AG
 */
public interface MemberExpression extends CommonExpression
{
  /**
   * @return 
   *   Returns the CommonExpression forming the path (the left side of '/') of the method operator.
   *   For OData 2.0 the value returned by {@link #getPath()} is a {@link PropertyExpression}   
   */
  public CommonExpression getPath();

  /**
   * @return 
   *   Return the CommonExpression forming the property (the right side of '/') of the method operator.
   *   For OData 2.0 the value returned by {@link #getProperty()} is a {@link PropertyExpression}
   */
  public CommonExpression getProperty();
}