package com.sap.core.odata.api.edm;

/**
 * EdmSimpleType is a primitive type as defined in the Entity Data Model (EDM).
 * 
 * @author SAP AG
 *
 */
public interface EdmSimpleType extends EdmType {

  /**
   * Check type compatibility
   * 
   * @param simpleType
   * @return <code>true</code> if the type is compatible to the provided type
   */
  public boolean isCompatible(EdmSimpleType simpleType);

  /**
   * Validate literal value
   * 
   * @param value
   * @param literalKind
   * @param facets
   * @return <code>true</code> if the validation is successful
   */
  public boolean validate(String value, EdmLiteralKind literalKind, EdmFacets facets);

  /**
   * Convert literal representation of value to system data type
   * 
   * @param value
   * @param literalKind
   * @param facets
   * @return system data type as Object
   */
  //TODO: Check Returntype
  public Object valueOfString(String value, EdmLiteralKind literalKind, EdmFacets facets);

  /**
   * Convert system data type to literal representation of value
   * 
   * @param value
   * @param literalKind
   * @param facets
   * @return literal representation as String
   */
  //TODO: Check Signature
  public String valueToString(Object value, EdmLiteralKind literalKind, EdmFacets facets);

  /**
   * Convert literal representation to URI literal representation
   * 
   * @param literal
   * @return URI literal representation as String
   */
  public String toUriLiteral(String literal);
}