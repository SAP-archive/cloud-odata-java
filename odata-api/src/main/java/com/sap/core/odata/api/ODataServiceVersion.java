package com.sap.core.odata.api;

public enum ODataServiceVersion {
  V20("2.0"), V30("3.0");

  final private String version;

  private ODataServiceVersion(String version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return this.version;
  }

  public static ODataServiceVersion fromString(String version) 
  {
    for (ODataServiceVersion it : ODataServiceVersion.values())
    {
      if (it.version.equals(version))
      {
          return it;
      }
    }
    
    throw new IllegalArgumentException(version);
  }
  
}
