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

import java.util.List;

/**
 * @com.sap.core.odata.DoNotImplement
 * EdmAnnotations holds all annotation attributes and elements for a specific CSDL element.
 * @author SAP AG
 */
public interface EdmAnnotations {

  /**
   * Get all annotation elements for the CSDL element
   * 
   * @return List of {@link EdmAnnotationElement}
   */
  List<? extends EdmAnnotationElement> getAnnotationElements();

  /**
   * Get annotation element by full qualified name
   * 
   * @param name
   * @param namespace
   * @return String
   */

  EdmAnnotationElement getAnnotationElement(String name, String namespace);

  /**
   * Get all annotation attributes for the CSDL element
   * 
   * @return List of {@link EdmAnnotationAttribute}
   */
  List<? extends EdmAnnotationAttribute> getAnnotationAttributes();

  /**
   * Get annotation attribute by full qualified name
   * 
   * @param name
   * @param namespace
   * @return String
   */
  EdmAnnotationAttribute getAnnotationAttribute(String name, String namespace);
}
