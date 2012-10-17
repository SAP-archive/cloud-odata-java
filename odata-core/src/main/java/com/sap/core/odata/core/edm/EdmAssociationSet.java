package com.sap.core.odata.core.edm;

public interface EdmAssociationSet extends EdmNamed {

  EdmAssociation getAssociation() throws EdmException;

  EdmAssociationSetEnd getEnd(String role) throws EdmException;

  EdmEntityContainer getEntityContainer() throws EdmException;
}
