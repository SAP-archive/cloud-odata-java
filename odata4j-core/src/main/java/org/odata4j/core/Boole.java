package org.odata4j.core;

/**
 * A custom Boolean class is necessary to support Boolean representations as
 * defined in the OData standard.
 * <p>The name of this class pays homage to 
 * <a href="http://en.wikipedia.org/wiki/George_Boole">George Boole (1815–1864)</a>.</p>
 */
public enum Boole {
  TRUE(Boolean.TRUE, "true", "1"),
  FALSE(Boolean.FALSE, "false", "0");

  private Boolean booleanValue;
  private String value1, value2;

  Boole(Boolean booleanValue, String value1, String value2) {
    this.booleanValue = booleanValue;
    this.value1 = value1;
    this.value2 = value2;
  }

  public Boolean toBoolean() {
    return booleanValue;
  }

  /**
   * Convert a string value to {@link Boole}. 
   * @param  value OData string representation of a boolean value.
   * @throws IllegalArgumentException if {@code value} is not one of
   *         {@code true}, {@code false}, {@code 1}, or {@code 0}.
   */
  public static Boole fromString(String value) {
    for (Boole boole : Boole.values())
      if (value.equals(boole.value1) || value.equals(boole.value2))
        return boole;
    throw new IllegalArgumentException();
  }
}
