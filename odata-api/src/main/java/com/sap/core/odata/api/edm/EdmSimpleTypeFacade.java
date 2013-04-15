/**
 * (c) 2013 by SAP AG
 */
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

}
