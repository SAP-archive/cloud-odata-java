package com.sap.core.odata.api.edm;

/**
 * EdmSimpleTypeKind holds all EdmSimpleTypes defined as primitive type in the Entity Data Model (EDM).
 * 
 * @author SAP AG
 */
public enum EdmSimpleTypeKind {

  Binary, Boolean, Byte, DateTime, DateTimeOffset, Decimal,
  Double, Guid, Int16, Int32, Int64, SByte, Single, String, Time;
}
