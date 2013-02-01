package com.sap.core.odata.api.uri.expression;



/**
 * Represents a literal expression node in the expression tree returned by the methods:
 * <li>{@link FilterParser#parseFilterString(String)}</li>
 * <li>{@link OrderByParser#parseOrderByString(String)}</li> 
 * <br>
 * <br>
 * <p>A literal expression node is inserted in the expression tree for any token witch is no
 * valid <i>operator</i>, <i>method</i> or <i>property</i>.
 * <br>
 * <br>
 * <p><b>For example</b> the filter "$filter=age eq 12" will result in an expression tree
 * with a literal expression node for "12".
 * <br>
 * <br>
 * @author SAP AG
 * @see FilterParser
 * @see OrderByParser
 */
public interface LiteralExpression extends CommonExpression
{

}