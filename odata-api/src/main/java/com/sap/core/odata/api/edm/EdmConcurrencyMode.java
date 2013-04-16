package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * EdmConcurrencyMode can be applied to any primitive Entity Data Model (EDM) type.
 * <p>Possible values are "None", which is the default, and "Fixed". Fixed implies that the property should be used for optimistic concurrency checks.
 * @author SAP AG
 */
public enum EdmConcurrencyMode {

  None, Fixed;
}