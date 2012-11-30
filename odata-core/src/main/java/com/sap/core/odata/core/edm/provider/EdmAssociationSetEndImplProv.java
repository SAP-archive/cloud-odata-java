package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAnnotatable;
import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.EdmAssociationSetEnd;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;

public class EdmAssociationSetEndImplProv implements EdmAssociationSetEnd, EdmAnnotatable {

  private EdmEntitySet entitySet;
  private String role;
  private AssociationSetEnd end;

  public EdmAssociationSetEndImplProv(AssociationSetEnd end, EdmEntitySet entitySet) throws EdmException {
    this.end = end;
    this.entitySet = entitySet;
    this.role = end.getRole();
  }

  @Override
  public EdmEntitySet getEntitySet() throws EdmException {
    return entitySet;
  }

  @Override
  public String getRole() {
    return role;
  }
  
  @Override
  public EdmAnnotations getAnnotations() throws EdmException {
   return new EdmAnnotationsImplProv(end.getAnnotationAttributes(), end.getAnnotationElements());
  }
}