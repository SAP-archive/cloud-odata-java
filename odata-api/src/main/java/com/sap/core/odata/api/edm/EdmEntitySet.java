package com.sap.core.odata.api.edm;

public interface EdmEntitySet extends EdmNamed {

  EdmEntityType getEntityType() throws EdmException;

  EdmEntitySet getRelatedEntitySet(EdmNavigationProperty navigationProperty) throws EdmException;

  EdmEntityContainer getEntityContainer() throws EdmException;
}
