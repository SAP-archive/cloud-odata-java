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
 * A CommonAttributes
 * @author SAP AG
 */
public interface CommonAttributes {
  /**
   * Get the a base URI 
   * 
   * @return base as String
   */
  public String getBase();

  /**
   * Get the natural language for the element
   * 
   * @return language as String
   */
  public String getLang();

  /**
   * Get the list of any attributes
   * 
   * @return list of {@link ExtensionAttribute}
   */
  public List<ExtensionAttribute> getAttributes();

}
