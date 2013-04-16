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
 * A CSDL Association element
 * 
 * <p>EdmAssociation defines the relationship of two entity types. 
 * @author SAP AG
 */
public interface EdmAssociation extends EdmType {

  /**
   * Get the {@link EdmAssociationEnd} by role
   * @param role
   * @return {@link EdmAssociationEnd}
   * @throws EdmException
   */
  EdmAssociationEnd getEnd(String role) throws EdmException;
}
