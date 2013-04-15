/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.uri.expression;

/**
 * Represents a literal expression node in the expression tree returned by the methods:
 * <li>{@link com.sap.core.odata.api.uri.UriParser#parseFilterString(com.sap.core.odata.api.edm.EdmEntityType, String) }</li>
 * <li>{@link com.sap.core.odata.api.uri.UriParser#parseOrderByString(com.sap.core.odata.api.edm.EdmEntityType, String) }</li> 
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
 */
public interface LiteralExpression extends CommonExpression {

}