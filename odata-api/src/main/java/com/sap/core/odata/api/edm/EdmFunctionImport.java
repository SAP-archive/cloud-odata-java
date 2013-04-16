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

import java.util.Collection;

/**
 * @com.sap.core.odata.DoNotImplement
 * A CSDL FunctionImport element
 * 
 * EdmFunctionImport can be used model functions which have input parameters, an associated HTTP Method
 * and a return type which can be of different kinds:
 * 
 * <li>{@link EdmSimpleType} or a collection of simple types
 * <li>{@link EdmEntityType} or a collection of entity types
 * <li>{@link EdmEntitySet}
 * @author SAP AG
 */
public interface EdmFunctionImport extends EdmMappable, EdmNamed {

  /**
   * Get the parameter by name
   * @param name
   * @return {@link EdmParameter}
   * @throws EdmException
   */
  EdmParameter getParameter(String name) throws EdmException;

  /**
   * Get all parameter names
   * @return collection of parameter names of type Collection<String>
   * @throws EdmException
   */
  Collection<String> getParameterNames() throws EdmException;

  /**
   * Get the edm entity set
   * @return {@link EdmEntitySet}
   * @throws EdmException
   */
  EdmEntitySet getEntitySet() throws EdmException;

  /**
   * Get the HTTP Method
   * @return HTTP Method as String
   * @throws EdmException
   */
  String getHttpMethod() throws EdmException;

  /**
   * @return {@link EdmTyped}
   * @throws EdmException
   */
  EdmTyped getReturnType() throws EdmException;

  /**
   * Get the entity container the function import is contained in
   * @return {@link EdmEntityContainer}
   * @throws EdmException
   */
  EdmEntityContainer getEntityContainer() throws EdmException;
}
