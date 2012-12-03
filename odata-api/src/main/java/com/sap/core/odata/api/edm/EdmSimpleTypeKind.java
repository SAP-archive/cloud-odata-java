package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.uri.EdmLiteral;
import com.sap.core.odata.api.uri.UriSyntaxException;

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

  public FullQualifiedName getFullQualifiedName() {
    return new FullQualifiedName(edmNamespace, this.toString());
  }
  
  public EdmSimpleType getEdmSimpleTypeInstance(){
    return RuntimeDelegate.getEdmSimpleType(this);
  }

  public static EdmLiteral parseUriLiteral(final String uriLiteral) throws UriSyntaxException {
    return RuntimeDelegate.getSimpleTypeFacade().parseUriLiteral(uriLiteral);
  }

}
