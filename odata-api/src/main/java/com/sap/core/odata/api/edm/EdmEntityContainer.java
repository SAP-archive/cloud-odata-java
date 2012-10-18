package com.sap.core.odata.api.edm;

public interface EdmEntityContainer extends EdmNamed {

  EdmEntitySet getEntitySet(String name) throws EdmException;

  EdmFunctionImport getFunctionImport(String name) throws EdmException;

  EdmAssociationSet getAssociationSet(EdmEntitySet sourceEntitySet, EdmNavigationProperty navigationProperty) throws EdmException;
}
