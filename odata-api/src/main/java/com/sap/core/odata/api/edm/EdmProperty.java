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
package com.sap.core.odata.api.edm;

/**
 * A CSDL Property element
 * <p>EdmProperty defines a simple type or a complex type.
 * @com.sap.core.odata.DoNotImplement
 * @author SAP AG
 */
public interface EdmProperty extends EdmElement {

  /**
   * Get customizable feed mappings for this property
   * 
   * @return {@link EdmCustomizableFeedMappings}
   * @throws EdmException
   */
  EdmCustomizableFeedMappings getCustomizableFeedMappings() throws EdmException;

  /**
   * Get the related mime type for the property
   * 
   * @return mime type as String
   * @throws EdmException
   */
  String getMimeType() throws EdmException;

  /**
   * Get the info if the property is a simple property
   * 
   * @return true, if it is a simple property
   */
  boolean isSimple();
}
