package com.sap.core.odata.api.edm;

public interface EdmSimpleType extends EdmType {

  //TODO: Exception Handling
  public boolean isCompatible(EdmSimpleType simpleType);

  public boolean validate(String value, EdmLiteralKind literalKind, EdmFacets facets);

  //TODO: Check Returntype
  public Object valueOfString(String value, EdmLiteralKind literalKind, EdmFacets facets);

  //TODO: Check Signature
  public String valueToString(Object value, EdmLiteralKind literalKind, EdmFacets facets);

  public String toUriLiteral(String literal);

}
