package com.sap.core.odata.api.edm;

public interface EdmMapping {

  String getValue();
  
  String getMimeType();

  //TODO ?introduce? String getContentSource();
  
  //TODO ?introduce? attribute mapping 
  //
  //  List<EdmAttributeMapping> getAttributeMappings();
  //
  //  EdmAttributeMappings would be a separate interface
  //
  //  public interface EdmAttributeMapping {
  //
  //    String getNamespace();
  //    
  //    String getName();
  //
  //    String getValue();
  //  }
}
