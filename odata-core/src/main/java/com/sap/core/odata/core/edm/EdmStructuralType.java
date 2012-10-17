package com.sap.core.odata.core.edm;

import java.util.Collection;

public interface EdmStructuralType extends EdmNamed, EdmType {

  EdmTyped getProperty(String name) throws EdmException;;

  Collection<String> getPropertyNames() throws EdmException;;

  EdmStructuralType getBaseType() throws EdmException;;
}
