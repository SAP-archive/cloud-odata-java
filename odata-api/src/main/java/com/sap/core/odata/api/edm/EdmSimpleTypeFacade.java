package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.uri.UriLiteral;
import com.sap.core.odata.api.uri.UriParserException;

public abstract class EdmSimpleTypeFacade {

  public static final String edmNamespace = "Edm";
  public static final String systemNamespace = "System";
  
  public static EdmSimpleType getInstance(EdmSimpleTypeKind edmSimpleType) {
    return RuntimeDelegate.getEdmSimpleType(edmSimpleType);
  }

  public static EdmSimpleType binaryInstance() {
    return getInstance(EdmSimpleTypeKind.Binary);
  }

  public static EdmSimpleType booleanInstance() {
    return getInstance(EdmSimpleTypeKind.Boolean);
  }

  public static EdmSimpleType byteInstance() {
    return getInstance(EdmSimpleTypeKind.Byte);
  }

  public static EdmSimpleType dateTimeInstance() {
    return getInstance(EdmSimpleTypeKind.DateTime);
  }

  public static EdmSimpleType dateTimeOffsetInstance() {
    return getInstance(EdmSimpleTypeKind.DateTimeOffset);
  }

  public static EdmSimpleType decimalInstance() {
    return getInstance(EdmSimpleTypeKind.Decimal);
  }

  public static EdmSimpleType doubleInstance() {
    return getInstance(EdmSimpleTypeKind.Double);
  }

  public static EdmSimpleType guidInstance() {
    return getInstance(EdmSimpleTypeKind.Guid);
  }

  public static EdmSimpleType int16Instance() {
    return getInstance(EdmSimpleTypeKind.Int16);
  }

  public static EdmSimpleType int32Instance() {
    return getInstance(EdmSimpleTypeKind.Int32);
  }

  public static EdmSimpleType int64Instance() {
    return getInstance(EdmSimpleTypeKind.Int64);
  }

  public static EdmSimpleType sByteInstance() {
    return getInstance(EdmSimpleTypeKind.SByte);
  }

  public static EdmSimpleType singleInstance() {
    return getInstance(EdmSimpleTypeKind.Single);
  }

  public static EdmSimpleType stringInstance() {
    return getInstance(EdmSimpleTypeKind.String);
  }

  public static EdmSimpleType timeInstance() {
    return getInstance(EdmSimpleTypeKind.Time);
  }

  public static EdmSimpleType nullInstance() {
    return getInstance(EdmSimpleTypeKind.Null);
  }

  public static UriLiteral parseUriLiteral(final String uriLiteral) throws UriParserException {
    return RuntimeDelegate.getSimpleTypeFacade().parse(uriLiteral);
  }
  
  public abstract UriLiteral parse(final String uriLiteral) throws UriParserException;

}
