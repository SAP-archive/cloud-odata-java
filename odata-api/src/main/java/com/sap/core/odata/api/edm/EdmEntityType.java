package com.sap.core.odata.api.edm;

import java.util.Collection;
import java.util.List;

public interface EdmEntityType extends EdmNamed, EdmStructuralType, EdmType {

  Collection<String> getKeyPropertyNames() throws EdmException;

  List<EdmProperty> getKeyProperties() throws EdmException;

  boolean hasStream() throws EdmException;

  EdmEntityType getBaseType() throws EdmException;

  EdmCustomizableFeedMappings getCustomizableFeedMappings() throws EdmException;

  Collection<String> getNavigationPropertyNames() throws EdmException;

}
