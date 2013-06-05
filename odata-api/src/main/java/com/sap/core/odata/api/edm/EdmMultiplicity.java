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
 * <p>EdmMultiplicity indicates the number of entity type instances
 * an association end can relate to:
 * <dl>
 * <dt>0..1</dt><dd>one or none</dd>
 * <dt>   1</dt><dd>exactly one</dd>
 * <dt>   *</dt><dd>many</dd>
 * </dl></p> 
 * @author SAP AG
 */
public enum EdmMultiplicity {

  ZERO_TO_ONE("0..1"), MANY("*"), ONE("1");

  private final String literal;

  private EdmMultiplicity(final String literal) {
    this.literal = literal;
  }

  /**
   * Gets the multiplicity for a given name.
   * @param literal
   * @return {@link EdmMultiplicity}
   */
  public static EdmMultiplicity fromLiteral(final String literal) {
    for (final EdmMultiplicity edmMultiplicity : EdmMultiplicity.values()) {
      if (edmMultiplicity.toString().equals(literal)) {
        return edmMultiplicity;
      }
    }
    throw new IllegalArgumentException("Invalid literal " + literal);
  }

  /**
   * Returns the OData literal form of this multiplicity.
   * @return the OData literal form of this multiplicity
   */
  @Override
  public String toString() {
    return literal;
  }
}
