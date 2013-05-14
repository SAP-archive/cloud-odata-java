package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * @com.sap.core.odata.DoNotImplement
 * EdmSimpleTypeKind holds all EdmSimpleTypes defined as primitive type in the Entity Data Model (EDM).
 * @author SAP AG
 */
public enum EdmSimpleTypeKind {

  Binary, Boolean, Byte, DateTime, DateTimeOffset, Decimal, Double, Guid, Int16, Int32, Int64, SByte, Single, String, Time, Null;

  /**
   * Returns the {@link FullQualifiedName} for this SimpleTypeKind.
   * @return {@link FullQualifiedName}
   */
  public FullQualifiedName getFullQualifiedName() {
    return new FullQualifiedName(EdmSimpleType.EDM_NAMESPACE, toString());
  }

  /**
   * Returns an instance for this {@link EdmSimpleTypeKind} in the form of {@link EdmSimpleType}.
   * @return {@link EdmSimpleType} instance
   */
  public EdmSimpleType getEdmSimpleTypeInstance() {
    return SimpleTypeFacadeHolder.instance.getEdmSimpleTypeInstance(this);
  }

  /**
   * <p>Parses a URI literal and determines its EDM simple type on the way.</p>
   * <p>If the literal is <code>null</code> or consists of the literal string
   * "null", the EDM simple type <code>Null</code> is returned.</p>
   * <p>The URI literal syntax of EDM simple types allows two ways of determining
   * the type:
   * <ul>
   * <li>The literal has an explicit type indicator (prefix or suffix).</li>
   * <li>The value is of a type compatible to all other possible types, e.g., "256"
   * could be of type <code>Int16</code> or <code>Int32</code> but all possible
   * values of <code>Int16</code> are also legal values of <code>Int32</code>
   * so callers could promote it to <code>Int32</code> in all cases where they
   * deem it necessary.<br/>
   * For a given literal, always the narrowest possible type is chosen.</li>
   * </ul></p>
   * <p>There are two cases where it is not possible to choose unambiguously
   * a compatible type:
   * <ul>
   * <li><code>0</code> or <code>1</code> could be a number but also a boolean value;
   * therefore, the internal (system) type <code>Bit</code> is used for these values.</li>
   * <li>Integer values between <code>0</code> and <code>127</code> (inclusive) could
   * be of type <code>SByte</code> or <code>Byte</code> both of which are not compatible
   * to the other; therefore, the internal (system) type <code>Uint7</code> is used
   * for these values.</li>
   * </ul></p>
   * @param uriLiteral the literal
   * @return an instance of {@link EdmLiteral}, containing the literal
   *         in default String representation and the EDM simple type
   * @throws EdmLiteralException if the literal is malformed
   */
  public static EdmLiteral parseUriLiteral(final String uriLiteral) throws EdmLiteralException {
	return SimpleTypeFacadeHolder.instance.parseUriLiteral(uriLiteral);
  }
  
  /**
   * Cached access to {@link EdmSimpleTypeFacade} which is used i.a. for {@link EdmSimpleType} instance creation 
   * or parsing of {@link EdmLiteral}s.
   */
  private static class SimpleTypeFacadeHolder {
	  static final EdmSimpleTypeFacade instance = RuntimeDelegate.getSimpleTypeFacade();
  }
}
