package com.sap.core.odata.core.edm;

import java.util.Collection;

public interface EdmStructuralType extends EdmNamed, EdmType {

  EdmTyped getProperty(String name);

  Collection<String> getPropertyNames();

  EdmStructuralType getBaseType();
}
