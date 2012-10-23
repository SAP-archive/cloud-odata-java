package com.sap.core.odata.api.edm;

public interface EdmAnnotationElement {

  String getNamespace();
  
  String getPrefix();
  
  String getName();
  
  //TODO return type to be discussed
  String getXmlData();
}
