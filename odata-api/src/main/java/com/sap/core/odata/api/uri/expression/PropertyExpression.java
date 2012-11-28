package com.sap.core.odata.api.uri.expression;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;

/**
 * Represents a property expression in the expression tree returned by the methods 
 * <li>{@link FilterParser#ParseExpression(String)}</li>
 * <li>{@link OrderByParser#parseOrderExpression(String)}</li> 
 * <br>
 * <br>
 * A property expression node is inserted in the expression tree for any valid
 * property which has an pendant inside the EDM given to the parsers before parsing.
 * If no EDM is available during parsing, or the property is not defined in the EDM a 
 * literal expression node will be created instead of a property expression node.
 * <br>
 * <br>
 * @author SAP AG
 * @see {@link FilterParser}
 * @see {@link OrderByParser}
 */
public interface PropertyExpression extends CommonExpression
{
  public String getPropertyName() throws EdmException;
  
  public EdmProperty getEdmProperty();
  
}