package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmMapping;

/**
 * Object of this class represent the mapping of a value to a mime type
 * @author SAP AG
 */
public class Mapping implements EdmMapping {

  private String value;
  private String mimeType;
  private Object object;

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmMapping#getValue()
   */
  @Override
  public String getInternalName() {
    return value;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmMapping#getMimeType()
   */
  @Override
  public String getMimeType() {
    return mimeType;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmMapping#getObject()
   */
  @Override
  public Object getObject() {
    return object;
  }

  /**
   * Sets the value for this {@link Mapping}
   * @param value
   * @return {@link Mapping} for method chaining
   */
  public Mapping setInternalName(final String value) {
    this.value = value;
    return this;
  }

  /**
   * Sets the mime type for this {@link Mapping}
   * @param mimeType
   * @return {@link Mapping} for method chaining
   */
  public Mapping setMimeType(final String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  /**
   * Sets an object. This method can be used by a provider to set whatever it wants to associate with this
   * @param object
   * @return {@link Mapping} for method chaining
   */
  public Mapping setObject(final Object object) {
    this.object = object;
    return this;
  }
}
