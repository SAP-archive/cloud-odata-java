package com.sap.core.odata.api.uri.expression;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;

public interface PropertyExpression extends CommonExpression
{
  public String getPropertyName() throws EdmException;
  
  public EdmProperty getEdmProperty();
  
}