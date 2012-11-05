package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmNamed;

public abstract class EdmNamedImplProv implements EdmNamed {

  protected EdmImplProv edm;
  private String name;
  
  public EdmNamedImplProv(EdmImplProv edm, String name) throws EdmException {
    this.edm = edm;
    this.name = name;
    validateName(name);
  }
  
  @Override
  public String getName() throws EdmException {
    return name;
  }
  
  private void validateName(String name) throws EdmException {
    //TODO
  }
}
