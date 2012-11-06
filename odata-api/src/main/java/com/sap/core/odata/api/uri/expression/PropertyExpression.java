package com.sap.core.odata.api.uri.expression;

import com.sap.core.odata.api.edm.EdmProperty;

//import com.sap.core.odata.api.edm.EdmProperty;

public interface PropertyExpression extends CommonExpression
{
  public String GetPropertyName();
  public EdmProperty GetEdmProperty();
}