package org.odata4j.core;

/**
 * Unsigned 8-bit integer (0-255)
 */
public class UnsignedByte extends Number implements Comparable<UnsignedByte> {

  private static final long serialVersionUID = 698449810630429786L;

  public static final UnsignedByte MIN_VALUE = new UnsignedByte(0);
  public static final UnsignedByte MAX_VALUE = new UnsignedByte(255);

  private final int value;

  public UnsignedByte(int value) {
    this.value = checkBounds(value);
  }

  @Override
  public String toString() {
    return Integer.toString(value);
  }

  private static int checkBounds(int value) {
    if (value < 0 || value > 255)
      throw new IllegalArgumentException("Value must be between 0 and 255");
    return value;
  }

  @Override
  public int intValue() {
    return value;
  }

  @Override
  public long longValue() {
    return value;
  }

  @Override
  public float floatValue() {
    return value;
  }

  @Override
  public double doubleValue() {
    return value;
  }

  public static UnsignedByte valueOf(int value) {
    return new UnsignedByte(value);
  }

  public static UnsignedByte parseUnsignedByte(String value) {
    return valueOf(Integer.parseInt(value));
  }

  @Override
  public int compareTo(UnsignedByte other) {
    return new Integer(value).compareTo(new Integer(other.value));
  }

  @Override
  public int hashCode() {
    return new Integer(value).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof UnsignedByte && ((UnsignedByte) obj).value == value;
  }

}
