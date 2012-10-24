package com.sap.core.odata.api.edm;

/**
 * EdmConcurrencyMode can be applied to any primitive Entity Data Model (EDM) type.
 * Possible values are "None", which is the default, and "Fixed".
 * 
 * Fixed implies that the property should be used for optimistic concurrency checks.
 * 
 * @author SAP AG
 */
public enum EdmConcurrencyMode {
  
  None, Fixed;
}