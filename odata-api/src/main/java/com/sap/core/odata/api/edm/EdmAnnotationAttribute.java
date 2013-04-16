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
 * @com.sap.core.odata.DoNotImplement
 * A CSDL AnnotationAttribute element. 
 * <p>EdmAnnotationAttribute is a custom XML attribute which can be applied to a CSDL element.
 * @author SAP AG
 */
public interface EdmAnnotationAttribute {

  /**
   * Get the namespace of the custom attribute
   * 
   * @return String
   */
  String getNamespace();

  /**
   * Get the prefix of the custom attribute
   * 
   * @return String
   */
  String getPrefix();

  /**
   * Get the name of the custom attribute
   * 
   * @return String
   */
  String getName();

  /**
   * Get the text of the custom attribute
   * 
   * @return String
   */
  String getText();
}
