package com.sap.core.odata.core.edm;

public interface EdmType extends EdmNamed {

  String getNamespace();

  EdmTypeEnum getKind();
}
