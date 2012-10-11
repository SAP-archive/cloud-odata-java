package com.sap.core.odata.core.edm;

public interface EdmAssociationSet extends EdmNamed {

  EdmAssociation getAssociation();

  EdmEnd getEnd(String role);

  EdmEntityContainer getEntityContainer();
}
