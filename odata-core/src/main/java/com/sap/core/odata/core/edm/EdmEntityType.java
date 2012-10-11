package com.sap.core.odata.core.edm;

import java.util.Collection;
import java.util.List;

public interface EdmEntityType extends EdmNamed, EdmStructuralType, EdmType {

  Collection<String> getKeyPropertyNames();

  List<EdmProperty> getKeyProperties();

  boolean hasStream();

  EdmEntityType getBaseType();

  EdmCustomizableFeedMappings getCustomizableFeedMappings();

  Collection<String> getNavigationPropertyNames();

}
