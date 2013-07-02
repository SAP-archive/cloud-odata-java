/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmMapping;

/**
 * Object of this class represent the mapping of a value to a MIME type.
 * @author SAP AG
 */
public class Mapping implements EdmMapping {

  private String value;
  private String mimeType;
  private Object object;

  @Override
  public String getInternalName() {
    return value;
  }

  @Override
  public String getMimeType() {
    return mimeType;
  }

  @Override
  public Object getObject() {
    return object;
  }

  /**
   * Sets the value for this {@link Mapping}.
   * @param value
   * @return {@link Mapping} for method chaining
   */
  public Mapping setInternalName(final String value) {
    this.value = value;
    return this;
  }

  /**
   * Sets the mime type for this {@link Mapping}.
   * @param mimeType
   * @return {@link Mapping} for method chaining
   */
  public Mapping setMimeType(final String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  /**
   * Sets an object. This method can be used by a provider to set whatever it wants to associate with this.
   * @param object
   * @return {@link Mapping} for method chaining
   */
  public Mapping setObject(final Object object) {
    this.object = object;
    return this;
  }
}
