package com.sap.core.odata.api.uri.expression;

import com.sap.core.odata.api.edm.EdmProperty;

public interface PropertyExpression extends CommonExpression
{
  public String getPropertyName();
  
  public EdmProperty getEdmProperty();
  
}