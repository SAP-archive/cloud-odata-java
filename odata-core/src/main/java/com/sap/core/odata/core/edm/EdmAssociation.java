package com.sap.core.odata.core.edm;

public interface EdmAssociation extends EdmNamed, EdmType {

  EdmAssociationEnd getEnd(String role) throws EdmException;
}
