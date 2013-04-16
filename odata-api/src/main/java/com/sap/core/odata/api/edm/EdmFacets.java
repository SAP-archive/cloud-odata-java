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
 * <p>A Facet is an element defined in CSDL that provides information
 * that specializes the usage of a type.</p>
 * @author SAP AG
 */
public interface EdmFacets {

  /**
   * Get the information if the type in use is nullable
   * 
   * @return <code>true</code> if the type in use is nullable
   */
  Boolean isNullable();

  /**
   * Get the default value of the type in use
   * 
   * @return a default value of the type in use as String
   */
  String getDefaultValue();

  /**
   * Get the maximum length of the type in use
   *
   * @return the maximum length of the type in use as Integer
   */
  Integer getMaxLength();

  /**
   * Get the information if the type in has a fixed length
   * 
   * @return <code>true</code> if the type in use has a fixed length
   */
  Boolean isFixedLength();

  /**
   * Get the precision of the type in use
   * 
   * @return the precision of the type in use as Integer
   */
  Integer getPrecision();

  /**
   * Get the scale of the type in use
   * 
   * @return the scale of the type in use as Integer
   */
  Integer getScale();

  /**
   * Get the information if UNICODE or ASCII characters are used. Default is UNICODE.
   * 
   * @return <code>true</code> if UNICODE characters are used
   */
  Boolean isUnicode();

  /**
   * Get the sorting sequence to be used.
   * 
   * @return the sorting sequence as String
   */
  String getCollation();

  /**
   * Get the information if the value of the type in use should be used for optimistic concurrency checks.
   * 
   * @return {@link EdmConcurrencyMode}
   */
  EdmConcurrencyMode getConcurrencyMode();
}
