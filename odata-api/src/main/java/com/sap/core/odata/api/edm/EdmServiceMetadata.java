package com.sap.core.odata.api.edm;

import java.io.Writer;

public interface EdmServiceMetadata {

  //TODO Exception Handling
  Writer getMetadata();
  
  //TODO Exception Handling
  String getDataServiceVersion();

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
