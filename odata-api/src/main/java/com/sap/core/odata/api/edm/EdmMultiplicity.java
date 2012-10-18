package com.sap.core.odata.api.edm;


/**
 * The possible number of entity types at each end of the relationship.
 * The multiplicity of an association end can have a value of one (1), zero or one (0..1), or many (*).
 */
public enum EdmMultiplicity {

  ZERO_TO_ONE("0..1"), MANY("*"), ONE("1");

  private final String symbolString;

  private EdmMultiplicity(String symbolString) {
    this.symbolString = symbolString;
  }

  public String getSymbolString() {
    return symbolString;
  }

  public static EdmMultiplicity fromSymbolString(String symbolString) {
    for (EdmMultiplicity m : EdmMultiplicity.values()) {
      if (m.getSymbolString().equals(symbolString))
        return m;
    }
    throw new IllegalArgumentException("Invalid symbolString " + symbolString);
  }

}
