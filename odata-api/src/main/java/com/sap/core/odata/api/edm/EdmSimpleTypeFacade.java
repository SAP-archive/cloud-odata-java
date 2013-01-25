package com.sap.core.odata.api.edm;

/**
 * @author SAP AG
 */
public interface EdmSimpleTypeFacade {

  /**
   * Parses a URI literal and determines its EDM simple type on the way.
   * @param uriLiteral the literal
   * @return an instance of {@link EdmLiteral}, containing the literal
   *         in default String representation and the EDM simple type
   * @throws EdmLiteralException if the literal is malformed
   */
  public EdmLiteral parseUriLiteral(final String uriLiteral) throws EdmLiteralException;

}
