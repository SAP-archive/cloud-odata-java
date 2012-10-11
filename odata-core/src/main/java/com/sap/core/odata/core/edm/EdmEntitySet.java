package com.sap.core.odata.core.edm;

public interface EdmEntitySet extends EdmNamed {

  EdmEntityType getEntityType();

  EdmEntitySet getRelatedEntitySet(EdmNavigationProperty navigationProperty);

  EdmEntityContainer getEntityContainer();
}
