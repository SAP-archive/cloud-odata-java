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
package com.sap.core.odata.processor.api.jpa.model;

/**
 * The interface acts a container for storing Java persistence column name. The
 * JPA EDM mapping instance can be associated with any EDM simple, EDM complex
 * property to denote the properties Java persistence column name.
 * 
 * @author SAP AG
 * 
 */
public interface JPAEdmMapping {
  /**
   * The method sets the Java persistence column name into the mapping
   * container.
   * 
   * @param name
   *            is the Java persistence column name
   */
  public void setJPAColumnName(String name);

  /**
   * The method gets the Java persistence column name from the mapping
   * container.
   * 
   * @return a String representing the Java persistence column name set into
   *         the container
   */
  public String getJPAColumnName();

  /**
   * The method sets the Java persistence entity/property type.
   * 
   * @param type
   *            is an instance of type Class<?>
   */
  public void setJPAType(Class<?> type);

  /**
   * The method returns the Java persistence entity/property type.
   * 
   * @return type
   */
  public Class<?> getJPAType();

  /**
   * The method returns the Type Hierarchy for those embeddable types
   * that are expanded as simple properties.
   * @return an array of Types in specific order. The first entry in the
   * array corresponds to the the outer most enclosing entity.
   */
  public Class<?>[] getJPATypeHierachy();
  
  /**
   * The method adds the give type to the end of Type Hierarchy list.
   * 
   * @return an integer reflecting the position where the entry has been added.
   */
  public int addToJPATypeHierachy(Class<?> type);

}
