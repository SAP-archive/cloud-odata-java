package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * This facade is used as a hook into the core implementation.
 * @author SAP AG
 */
public interface EdmSimpleTypeFacade {

  /**
   * IMPORTANT: Use {@link EdmSimpleTypeKind} parseUriLiteral for the implementation.
   * <p>This method definition is used only inside the core of this library.
   * @param uriLiteral
   * @return the parsed literal
   * @throws EdmLiteralException
   */
  public EdmLiteral parseUriLiteral(final String uriLiteral) throws EdmLiteralException;

  /**
   * IMPORTANT: Use {@link EdmSimpleTypeKind#getEdmSimpleTypeInstance()} for the application development.
   * 
   * <p>This method definition is used only inside the core of this library.</p>
   * 
   * @param typeKind for which an instance is requested
   * @return an instance of the corresponding {@link EdmSimpleType} to given {@link EdmSimpleTypeKind}
   */
  public EdmSimpleType getEdmSimpleTypeInstance(final EdmSimpleTypeKind typeKind);
}
