package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.edm.EdmSimpleTypeFacade.EdmSimpleTypes;

public interface EdmSimpleType extends EdmType {

  public boolean isCompatible(EdmSimpleType simpleType);

  public boolean validate(String value, EdmLiteralKind literalKind, EdmFacets facets);

  //TODO: Check Returntype
  public Object valueOfString(String value, EdmLiteralKind literalKind, EdmFacets facets);

  //TODO: Check Signature
  public String valueToString(Object value, EdmLiteralKind literalKind, EdmFacets facets);

  public String toUriLiteral(String literal);

  public EdmSimpleTypes getTypeRepresentation();

}
