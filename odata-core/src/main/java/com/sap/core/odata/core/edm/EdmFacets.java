package com.sap.core.odata.core.edm;

public interface EdmFacets {

  // TODO: review defaulting
  public boolean nullable = true;
  String defaultValue = null;
  int max_length = -1;
  boolean fixed_length = false;
  String precision = null;
  String scale = null;
  boolean unicode = false;
  String collation = null;
  EdmConcurrencyMode concurrency_mode = null;

}
