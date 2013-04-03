/**
 * Expression Parser
 * <p>This package contains all classes necessary to decribe an expression tree(e.g. a filter or order by tree)
 * 
 * <p>Trees can be traversed by implementing the {@link com.sap.core.odata.api.uri.expression.ExpressionVisitor} interface and calling the accept() method. 
 * <br>Different types of expressions can be found in {@link com.sap.core.odata.api.uri.expression.ExpressionKind}.
 */
package com.sap.core.odata.api.uri.expression;