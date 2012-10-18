package com.sap.core.odata.api.edm;


public interface EdmType extends EdmNamed {

  String getNamespace() throws EdmException;;

  EdmTypeKind getKind();
}
