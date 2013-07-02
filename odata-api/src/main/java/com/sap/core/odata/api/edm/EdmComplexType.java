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
 * <p>A CSDL ComplexType element.</p>
 * <p>EdmComplexType holds a set of related information like {@link EdmSimpleType}
 * properties and EdmComplexType properties.
 * @author SAP AG
 * @com.sap.core.odata.DoNotImplement
 */
public interface EdmComplexType extends EdmStructuralType {

  @Override
  EdmComplexType getBaseType() throws EdmException;
}
