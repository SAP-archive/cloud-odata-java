package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmMapping;

public class Mapping implements EdmMapping {

  private String value;
  private String mimeType;

  public Mapping(String value, String mimeType) {
    this.value = value;
    this.mimeType = mimeType;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public String getMimeType() {
    return mimeType;
  }
}
