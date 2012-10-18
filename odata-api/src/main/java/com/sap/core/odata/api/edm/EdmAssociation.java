package com.sap.core.odata.api.edm;

public interface EdmAssociation extends EdmNamed, EdmType {

  EdmAssociationEnd getEnd(String role) throws EdmException;
}
