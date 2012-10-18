package com.sap.core.odata.api.edm;

public enum EdmConcurrencyMode {

  NONE("None"), FIXED("Fixed");

  private final String symbolString;

  private EdmConcurrencyMode(String symbolString) {
    this.symbolString = symbolString;
  }

  public String getSymbolString() {
    return symbolString;
  }

  public static EdmConcurrencyMode fromSymbolString(String symbolString) {
    for (EdmConcurrencyMode m : EdmConcurrencyMode.values()) {
      if (m.getSymbolString().equals(symbolString))
        return m;
    }
    throw new IllegalArgumentException("Invalid symbolString " + symbolString);
  }

}
