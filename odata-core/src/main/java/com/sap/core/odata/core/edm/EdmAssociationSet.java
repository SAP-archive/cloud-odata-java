package com.sap.core.odata.core.edm;

public interface EdmAssociationSet extends EdmNamed {

  EdmAssociation getAssociation() throws EdmException;

  EdmEnd getEnd(String role) throws EdmException;

  EdmEntityContainer getEntityContainer() throws EdmException;
}
