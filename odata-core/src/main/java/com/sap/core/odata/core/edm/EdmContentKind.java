package com.sap.core.odata.core.edm;

public enum EdmContentKind {

  TEXT("text"), HTML("html"), XHTML("xhtml");

  private final String symbolString;

  private EdmContentKind(String symbolString) {
    this.symbolString = symbolString;
  }

  public String getSymbolString() {
    return symbolString;
  }

  public static EdmContentKind fromSymbolString(String symbolString) {
    for (EdmContentKind m : EdmContentKind.values()) {
      if (m.getSymbolString().equals(symbolString))
        return m;
    }
    throw new IllegalArgumentException("Invalid symbolString " + symbolString);
  }

}
