package com.sap.core.odata.core.edm;

public interface EdmAssociationEnd {

  String getRole();

  EdmEntityType getEntityType();

  EdmMultiplicity getMultiplicity();
}
