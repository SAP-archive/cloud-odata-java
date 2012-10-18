package com.sap.core.odata.api.edm;

public interface EdmMapping {

  //TODO
//  types:
//    BEGIN OF attribute_mapping_s,
//      namespace type string,
//      name TYPE string,
//      value type string,
//   END of attribute_mapping_s .
//  types:
//    attribute_mapping_t type hashed table OF attribute_mapping_s with unique KEY namespace name .
//  types:
//    BEGIN OF mapping_s,
//      value TYPE string,
//      mime_type TYPE string,
//      attribute_mappings TYPE attribute_mapping_t,
//   END OF mapping_s .
//
//  constants GC_ATTR_MAP_MEDIA_SRC type STRING value 'media_src'. "#EC NOTEXT
//  constants GC_ATTR_MAP_CONTENT_SOURCE type STRING value GC_ATTR_MAP_MEDIA_SRC. "#EC NOTEXT
//  constants GC_ATTR_MAP_NAMESPACE_DS type STRING value 'local'. "#EC NOTEXT
//
//  methods GET_MAPPING
//    returning
//      value(RD_MAPPING) type ref to MAPPING_S
//    raising
//      /IWCOR/CX_DS_EDM_ERROR .
}
