package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.uri.UriLiteral;
import com.sap.core.odata.api.uri.UriParserException;

/**
 * EdmSimpleTypeKind holds all EdmSimpleTypes defined as primitive type in the Entity Data Model (EDM).
 * 
 * @author SAP AG
 */
public enum EdmSimpleTypeKind {

  Binary, Boolean, Byte, DateTime, DateTimeOffset, Decimal,
  Double, Guid, Int16, Int32, Int64, SByte, Single, String, Time, Null;

  public static final String edmNamespace = "Edm";
  public static final String systemNamespace = "System";

  public FullQualifiedName getFullqualifiedName() {
    return new FullQualifiedName(edmNamespace, this.toString());
  }

  public static EdmSimpleType getSimpleTypeInstance(EdmSimpleTypeKind edmSimpleType) {
    return RuntimeDelegate.getEdmSimpleType(edmSimpleType);
  }

  public static EdmSimpleType binaryInstance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.Binary);
  }

  public static EdmSimpleType booleanInstance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.Boolean);
  }

  public static EdmSimpleType byteInstance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.Byte);
  }

  public static EdmSimpleType dateTimeInstance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.DateTime);
  }

  public static EdmSimpleType dateTimeOffsetInstance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.DateTimeOffset);
  }

  public static EdmSimpleType decimalInstance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.Decimal);
  }

  public static EdmSimpleType doubleInstance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.Double);
  }

  public static EdmSimpleType guidInstance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.Guid);
  }

  public static EdmSimpleType int16Instance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.Int16);
  }

  public static EdmSimpleType int32Instance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.Int32);
  }

  public static EdmSimpleType int64Instance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.Int64);
  }

  public static EdmSimpleType sByteInstance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.SByte);
  }

  public static EdmSimpleType singleInstance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.Single);
  }

  public static EdmSimpleType stringInstance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.String);
  }

  public static EdmSimpleType timeInstance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.Time);
  }

  public static EdmSimpleType nullInstance() {
    return getSimpleTypeInstance(EdmSimpleTypeKind.Null);
  }

  public static UriLiteral parseUriLiteral(final String uriLiteral) throws UriParserException {
    return RuntimeDelegate.getSimpleTypeFacade().parseUriLiteral(uriLiteral);
  }

}
