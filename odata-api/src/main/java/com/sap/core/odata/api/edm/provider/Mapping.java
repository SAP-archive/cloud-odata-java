package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmMapping;

public class Mapping implements EdmMapping {

  private String value;
  private String mimeType;

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public String getMimeType() {
    return mimeType;
  }

  public Mapping setValue(String value) {
    this.value = value;
    return this;
  }

  public Mapping setMimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }
}
