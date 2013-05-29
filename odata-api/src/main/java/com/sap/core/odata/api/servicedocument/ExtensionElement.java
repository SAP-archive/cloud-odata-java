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
package com.sap.core.odata.api.servicedocument;

import java.util.List;

/**
 * A ExtensionElement
 * <p>ExtensionElement is an element that is defined in any namespace except 
 * the namespace "app"
 * @author SAP AG
 */
public interface ExtensionElement {
  /**
   * Get the namespace 
   * 
   * @return namespace as String
   */
  public String getNamespace();

  /**
   * Get the prefix of the element
   * 
   * @return prefix as String
   */
  public String getPrefix();

  /**
   * Get the local name of the element
   * 
   * @return name as String
   */
  public String getName();

  /**
   * Get the text
   * 
   * @return text as String
   */
  public String getText();

  /**
   * Get nested elements
   * 
   * @return a list of {@link ExtensionElement}
   */
  public List<ExtensionElement> getElements();

  /**
   * Get attributes
   * 
   * @return a list of {@link ExtensionAttribute}
   */
  public List<ExtensionAttribute> getAttributes();
}
