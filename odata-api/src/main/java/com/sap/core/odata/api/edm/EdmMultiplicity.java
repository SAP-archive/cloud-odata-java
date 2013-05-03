package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * <p>EdmMultiplicity indicates the number of entity type instances
 * an association end can relate to:
 * <dl>
 * <dt>0..1</dt><dd>one or none</dd>
 * <dt>   1</dt><dd>exactly one</dd>
 * <dt>   *</dt><dd>many</dd>
 * </dl></p> 
 * @author SAP AG
 */
public enum EdmMultiplicity {

  ZERO_TO_ONE("0..1"), MANY("*"), ONE("1");

  private final String literal;

  private EdmMultiplicity(final String literal) {
    this.literal = literal;
  }

  /**
   * Gets the multiplicity for a given name.
   * @param literal
   * @return {@link EdmMultiplicity}
   */
  public static EdmMultiplicity fromLiteral(final String literal) {
    for (final EdmMultiplicity edmMultiplicity : EdmMultiplicity.values()) {
      if (edmMultiplicity.toString().equals(literal)) {
        return edmMultiplicity;
      }
    }
    throw new IllegalArgumentException("Invalid literal " + literal);
  }

  /**
   * Returns the OData literal form of this multiplicity.
   * @return the OData literal form of this multiplicity
   */
  @Override
  public String toString() {
    return literal;
  }
}