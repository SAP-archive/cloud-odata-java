package com.sap.core.odata.api.commons;

public enum ODataVersion {
  V20("2.0"), V30("3.0");

  private String version;

  private ODataVersion(String version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return this.version;
  }

  public static ODataVersion fromString(String version) {
    ODataVersion v;
    if ("2.0".equals(version)) {
      v = V20;
    } else if ("3.0".equals(version)) {
      v = V30;
    } else {
      throw new IllegalArgumentException(version);
    }

    return v;
  }

}
