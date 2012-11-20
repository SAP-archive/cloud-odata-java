package com.sap.core.odata.api.edm;


public interface EdmServiceMetadata {

  //TODO: Check return value
  String getMetadata() throws EdmException;
  
  String getDataServiceVersion() throws EdmException;

  //TODO required for Atom Service Document
  //TODO Exception Handling
//  EdmEntitySetInfos getEntitySetInfos();
//  
//  BEGIN OF entity_set_info_s,
//    entity_container TYPE string ,
//    is_default_entity_container TYPE abap_bool,
//    entity_set TYPE string,
//    uri TYPE string,
//  END OF entity_set_info_s .
}
