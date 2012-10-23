package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.edm.EdmProperty;


public interface KeyPredicate {

  public String getLiteral();

  public EdmProperty getProperty();

}
