package com.sap.core.odata.api.annotations;

/**
 * EdmTypeKind specifies the type of an edm element.
 * 
 * @author SAP AG
 */
public enum EdmTypeKind {

  // SIMPLE, 
  INT16, 
  STRING,
  DATETIME,
  
  UNDEFINED, COMPLEX, ENTITY, NAVIGATION, ASSOCIATION, SYSTEM;
  
  
}