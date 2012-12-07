package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmMapping;

/**
 * Object of this class represent the mapping of a value to a mime type
 * @author SAP AG
 */
public class Mapping implements EdmMapping {

  private String value;
  private String mimeType;

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmMapping#getValue()
   */
  @Override
  public String getValue() {
    return value;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmMapping#getMimeType()
   */
  @Override
  public String getMimeType() {
    return mimeType;
  }

  /**
   * MANDATORY
   * <p>Sets the value for this {@link Mapping}
   * @param value
   * @return {@link Mapping} for method chaining
   */
  public Mapping setValue(String value) {
    this.value = value;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the mime type for this {@link Mapping}
   * @param mimeType
   * @return {@link Mapping} for method chaining
   */
  public Mapping setMimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }
}
