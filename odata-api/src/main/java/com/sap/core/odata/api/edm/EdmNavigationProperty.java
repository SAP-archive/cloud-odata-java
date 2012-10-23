package com.sap.core.odata.api.edm;

public interface EdmNavigationProperty extends EdmNamed, EdmTyped {

  EdmAssociation getRelationship() throws EdmException;;

  String getFromRole() throws EdmException;;

  String getToRole() throws EdmException;;
}
