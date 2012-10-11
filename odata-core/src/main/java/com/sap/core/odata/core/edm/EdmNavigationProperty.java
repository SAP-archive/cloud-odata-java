package com.sap.core.odata.core.edm;

public interface EdmNavigationProperty extends EdmNamed, EdmTyped {

  EdmAssociation getRelationship();

  String getFromRole();

  String getToRole();
}
