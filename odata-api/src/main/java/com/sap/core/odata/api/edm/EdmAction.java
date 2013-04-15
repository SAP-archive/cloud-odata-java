/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * A CSDL Action Element
 * <p>EdmAction can either be Cascade or None. Cascade implies that a delete operation on an entity also should delete the relationship instances.
 * @author SAP AG
 */
public enum EdmAction {

  Cascade, None;
}