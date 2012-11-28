package com.sap.core.odata.api.uri.expression;

/**
 * Represents a literal expression node in the expression tree returned by the methods 
 * <li>{@link FilterParser#ParseExpression(String)}</li>
 * <li>{@link OrderByParser#parseOrderExpression(String)}</li> 
 * <br>
 * <br>
 * A literal expression node is inserted in the expression tree for any token witch is no
 * valid <i>operator</i>, <i>method</i> or <i>property</i>.
 * For example the filter "$filter=age eq 12" will result in an expression tree
 * with a literal expression node for age if "age" is not a property or there is no
 * EDM available during parsing. A second literal expression nodes is created for "12"
 * <br>
 * <br>
 * @author SAP AG
 * @see {@link FilterParser}
 * @see {@link OrderByParser}
 */
public interface LiteralExpression extends CommonExpression 
{
  
}