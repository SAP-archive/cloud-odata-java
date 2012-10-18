package com.sap.core.odata.api.edm;

public interface EdmAssociationEnd {

  String getRole();

  EdmEntityType getEntityType();

  EdmMultiplicity getMultiplicity();
}
