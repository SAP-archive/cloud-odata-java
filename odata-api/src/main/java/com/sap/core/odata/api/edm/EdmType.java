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
 * EdmType holds the namespace of a given type and its type as {@link EdmTypeKind}.
 * @author SAP AG
 */
public interface EdmType extends EdmNamed {

  /**
   * Namespace of this {@link EdmType}
   * @return namespace as String
   * @throws EdmException
   */
  String getNamespace() throws EdmException;

  /**
   * @return {@link EdmTypeKind} of this {@link EdmType}
   */
  EdmTypeKind getKind();
}
