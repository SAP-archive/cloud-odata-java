package com.sap.core.odata.api.edm;

/**
 * A CSDL Action Element
 * 
 * EdmAction can either be Cascade or None.
 * 
 * Cascade implies that a delete operation on an entity also should delete the relationship instances.
 * 
 * @author SAP AG
 */
public enum EdmAction {
  
  Cascade, None;
}