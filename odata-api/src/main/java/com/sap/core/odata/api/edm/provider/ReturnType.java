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
package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * Objects of this Class represent a return type of a function import
 * @author SAP AG
 */
public class ReturnType {

  private FullQualifiedName typeName;
  private EdmMultiplicity multiplicity;

  /**
   * @return {@link FullQualifiedName} type of this {@link ReturnType}
   */
  public FullQualifiedName getTypeName() {
    return typeName;
  }

  /**
   * @return {@link EdmMultiplicity} of this {@link ReturnType}
   */
  public EdmMultiplicity getMultiplicity() {
    return multiplicity;
  }

  /**
   * Sets the type  of this {@link ReturnType} via the types {@link FullQualifiedName}
   * @param qualifiedName
   * @return {@link ReturnType} for method chaining
   */
  public ReturnType setTypeName(final FullQualifiedName qualifiedName) {
    typeName = qualifiedName;
    return this;
  }

  /**
   * Sets the {@link EdmMultiplicity} of this {@link ReturnType}
   * @param multiplicity
   * @return {@link ReturnType} for method chaining
   */
  public ReturnType setMultiplicity(final EdmMultiplicity multiplicity) {
    this.multiplicity = multiplicity;
    return this;
  }

  @Override
  public String toString() {
    if (EdmMultiplicity.MANY == multiplicity) {
      return "Collection(" + typeName + ")";
    } else {
      return typeName.toString();
    }
  }

}
