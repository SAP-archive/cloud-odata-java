package com.sap.core.odata.api.edm;

public enum EdmAction {

  CASCADE("Cascade"), NONE("None");

  private final String symbolString;

  private EdmAction(String symbolString) {
    this.symbolString = symbolString;
  }

  public String getSymbolString() {
    return symbolString;
  }

  public static EdmAction fromSymbolString(String symbolString) {
    for (EdmAction m : EdmAction.values()) {
      if (m.getSymbolString().equals(symbolString))
        return m;
    }
    throw new IllegalArgumentException("Invalid symbolString " + symbolString);
  }

}
