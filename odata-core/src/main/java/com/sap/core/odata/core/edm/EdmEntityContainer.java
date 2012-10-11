package com.sap.core.odata.core.edm;

public interface EdmEntityContainer extends EdmNamed {

  EdmEntitySet getEntitySet(String name);

  EdmFunctionImport getFunctionImport(String name);

  EdmAssociationSet getAssociationSet(EdmEntitySet sourceEntitySet, EdmNavigationProperty navigationProperty);
}
