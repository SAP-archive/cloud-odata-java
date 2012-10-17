package com.sap.core.odata.core.edm;

public interface EdmAssociation extends EdmNamed, EdmType {

  EdmEnd getEnd(String role) throws EdmException;
}
