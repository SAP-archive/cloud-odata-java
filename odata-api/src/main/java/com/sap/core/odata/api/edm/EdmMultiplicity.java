package com.sap.core.odata.api.edm;

/**
 * EdmMultiplicity indicates the number of entity type instances association end can relate to.
 * 
 * The number can by "0..1", "1" which means exactly one or "*" which means many. 
 * 
 * @author SAP AG
 */
public enum EdmMultiplicity {

  ZERO_TO_ONE("0..1"), MANY("*"), ONE("1");

  private final String literal;

  private EdmMultiplicity(String literal) {
    this.literal = literal;
  }

  /**
   * Get the multiplicity for a given name
   * 
   * @param literal
   * @return {@link EdmMultiplicity}
   */
  public static EdmMultiplicity fromLiteral(String literal) {
    for (final EdmMultiplicity edmMultiplicity : EdmMultiplicity.values()) {
      if (edmMultiplicity.toString().equals(literal)) {
        return edmMultiplicity;
      }
    }
    throw new IllegalArgumentException("Invalid literal " + literal);
  }

  /* (non-Javadoc)
   * @see java.lang.Enum#toString()
   */
  @Override
  public String toString() {
    return literal;
  }
}