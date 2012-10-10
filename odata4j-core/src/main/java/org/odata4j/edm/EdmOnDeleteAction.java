package org.odata4j.edm;

/**
 * The action is performed on one end of the relationship when the state of the other side if
 * the relationship changes
 * The action of an association end can either be "Cascade" or "None".
 */
public enum EdmOnDeleteAction {

  CASCADE("Cascade"), NONE("None");

  private final String symbolString;

  private EdmOnDeleteAction(String symbolString) {
    this.symbolString = symbolString;
  }

  public String getSymbolString() {
    return symbolString;
  }

  public static EdmOnDeleteAction fromSymbolString(String symbolString) {
    for (EdmOnDeleteAction action : EdmOnDeleteAction.values()) {
      if (action.getSymbolString().equals(symbolString))
        return action;
    }
    throw new IllegalArgumentException("Invalid symbolString " + symbolString);
  }

}
