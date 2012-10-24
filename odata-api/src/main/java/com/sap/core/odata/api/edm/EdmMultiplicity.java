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

  private final String name;

  private EdmMultiplicity(String name) {
    this.name = name;
  }

  /**
   * Get the multiplicity for a given name
   * 
   * @param name
   * @return {@link EdmMultiplicity}
   */
  public static EdmMultiplicity fromSymbolString(String name) {
    for (EdmMultiplicity edmMultiplicity : EdmMultiplicity.values()) {
      if (edmMultiplicity.toString().equals(name))
        return edmMultiplicity;
    }
    throw new IllegalArgumentException("Invalid name " + name);
  }

  /* (non-Javadoc)
   * @see java.lang.Enum#toString()
   */
  @Override
  public String toString() {
    return name;
  }
}