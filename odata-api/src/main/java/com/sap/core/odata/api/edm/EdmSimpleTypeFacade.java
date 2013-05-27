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
 * This facade is used as a hook into the core implementation.
 * @author SAP AG
 */
public interface EdmSimpleTypeFacade {

  /**
   * IMPORTANT: Use {@link EdmSimpleTypeKind} parseUriLiteral for the implementation.
   * <p>This method definition is used only inside the core of this library.
   * @param uriLiteral
   * @return the parsed literal
   * @throws EdmLiteralException
   */
  public EdmLiteral parseUriLiteral(final String uriLiteral) throws EdmLiteralException;

  /**
   * IMPORTANT: Use {@link EdmSimpleTypeKind#getEdmSimpleTypeInstance()} for the application development.
   * 
   * <p>This method definition is used only inside the core of this library.</p>
   * 
   * @param typeKind for which an instance is requested
   * @return an instance of the corresponding {@link EdmSimpleType} to given {@link EdmSimpleTypeKind}
   */
  public EdmSimpleType getEdmSimpleTypeInstance(final EdmSimpleTypeKind typeKind);
}
