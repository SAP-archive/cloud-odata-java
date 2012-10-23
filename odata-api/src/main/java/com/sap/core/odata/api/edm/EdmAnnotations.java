package com.sap.core.odata.api.edm;

import java.util.Collection;

public interface EdmAnnotations {

  Collection<EdmAnnotationElement> getAnnotationElements();

  //TODO return type to be discussed, see EdmAnnotationElement
  String getAnnotationElement(String name, String namespace);

  Collection<EdmAnnotationAttribute> getAnnotationAttributes();

  String getAnnotationAttribute(String name, String namespace);
  
  //TODO do we need a generic data container like
  //
  //  Object getData(String name);
}
