package org.odata4j.core;

/**
 * V1 or V2
 */
public enum ODataVersion {

  // order of definition is important
  V1("1.0"),
  V2("2.0");

  /**
   * 1.0 or 2.0
   */
  public final String asString;

  private ODataVersion(String asString) {
    this.asString = asString;
  }

  /**
   * Identify a version by version string.
   *
   * @param str  the version string
   * @return the version enum
   */
  public static ODataVersion parse(String str) {
    if (V1.asString.equals(str))
      return V1;
    else if (V2.asString.equals(str)) {
      return V2;
    } else {
      throw new IllegalArgumentException("Unknown ODataVersion " + str);
    }
  }

  /** Returns true if the version v is greater than the target version */
  public static boolean isVersionGreaterThan(ODataVersion v, ODataVersion target) {
    return v.compareTo(target) > 0;
  }

  /** Returns true if the version v is greater than or equal to the target version */
  public static boolean isVersionGreaterThanOrEqualTo(ODataVersion v, ODataVersion target) {
    return v.compareTo(target) >= 0;
  }

}
