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
 * EdmStructuralType is the base for a complex type or an entity type.
 * <p>Complex types and entity types are described in the Conceptual Schema Definition of the OData protocol.
 * @author SAP AG
 */
public interface EdmStructuralType extends EdmMappable, EdmType {

  /**
   * Get property by name
   * @param name
   * @return simple or complex property as {@link EdmTyped}
   * @throws EdmException
   */
  EdmTyped getProperty(String name) throws EdmException;

  /**
   * Get all property names
   * 
   * @return property names as type List<String>
   * @throws EdmException
   */
  List<String> getPropertyNames() throws EdmException;

  /**
   * Base types are described in the OData protocol specification.
   * 
   * @return {@link EdmStructuralType}
   * @throws EdmException
   */
  EdmStructuralType getBaseType() throws EdmException;
}
