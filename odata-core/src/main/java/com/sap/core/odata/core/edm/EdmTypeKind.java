package com.sap.core.odata.core.edm;

public enum EdmTypeKind {

  UNDEFINED(" "), SIMPLE("S"), COMPLEX("C"), ENTITY("E"), NAVIGATION("N"), ASSOCIATION("A"), SYSTEM("Y");

  private final String symbolString;

  private EdmTypeKind(String symbolString) {
    this.symbolString = symbolString;
  }

  public String getSymbolString() {
    return symbolString;
  }

  public static EdmTypeKind fromSymbolString(String symbolString) {
    for (EdmTypeKind m : EdmTypeKind.values()) {
      if (m.getSymbolString().equals(symbolString))
        return m;
    }
    throw new IllegalArgumentException("Invalid symbolString " + symbolString);
  }
}
