package com.sap.core.odata.core.edm;

public interface EdmEntitySet extends EdmNamed {

  EdmEntityType getEntityType() throws EdmException;

  EdmEntitySet getRelatedEntitySet(EdmNavigationProperty navigationProperty) throws EdmException;

  EdmEntityContainer getEntityContainer() throws EdmException;
}
