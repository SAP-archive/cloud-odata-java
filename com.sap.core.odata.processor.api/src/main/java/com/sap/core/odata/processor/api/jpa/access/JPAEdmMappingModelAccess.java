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
package com.sap.core.odata.processor.api.jpa.access;

import com.sap.core.odata.processor.api.jpa.model.mapping.JPAEdmMappingModel;

/**
 * Interface provides methods to access JPA EDM mapping model.
 * 
 * @author SAP AG
 * @see JPAEdmMappingModel
 * 
 */
public interface JPAEdmMappingModelAccess {

  /**
   * The method searches and loads the mapping model stored in &ltfile&gt.xml
   * file into the java object
   * {@link com.sap.core.odata.processor.api.jpa.model.mapping.JPAEdmMappingModel}
   * . The name of the file is set into ODataJPAContext method.
   * 
   * @see com.sap.core.odata.processor.api.jpa.ODataJPAContext#setJPAEdmMappingModel(String)
   */
  public void loadMappingModel();

  /**
   * The method returns if there exists a mapping model.
   * 
   * @return true - if there exists a mapping model for the OData service else
   *         false
   */
  public boolean isMappingModelExists();

  /**
   * The method returns a JPA EDM mapping model Java object. The mapping model
   * in XML files is un-marshaled into the Java object.
   * 
   * @return an instance of type
   *         {@link com.sap.core.odata.processor.api.jpa.model.mapping.JPAEdmMappingModel}
   */
  public JPAEdmMappingModel getJPAEdmMappingModel();

  /**
   * The method returns EDM Schema namespace for the persistence unit name
   * 
   * @param persistenceUnitName
   *            is the Java persistence unit name
   * @return EDM schema name space mapped to Java persistence unit name or
   *         null if no mapping is available
   */
  public String mapJPAPersistenceUnit(String persistenceUnitName);

  /**
   * The method returns EDM entity type name for the Java persistence entity
   * type name
   * 
   * @param jpaEntityTypeName
   *            is the Java persistence entity type name
   * @return EDM entity type name mapped to Java persistence entity type name
   *         or null if no mapping is available
   */
  public String mapJPAEntityType(String jpaEntityTypeName);

  /**
   * The method returns EDM entity set name for the Java persistence entity
   * type name
   * 
   * @param jpaEntityTypeName
   *            is the Java persistence entity type name
   * @return EDM entity set name mapped to Java persistence entity type name
   *         or null if no mapping is available
   */
  public String mapJPAEntitySet(String jpaEntityTypeName);

  /**
   * The method returns EDM property name for the Java persistence entity
   * attribute name.
   * 
   * @param jpaEntityTypeName
   *            is the Java persistence entity type name
   * @param jpaAttributeName
   *            is the Java persistence attribute name
   * @return EDM property name mapped to Java persistence attribute name or
   *         null if no mapping is available
   */
  public String mapJPAAttribute(String jpaEntityTypeName,
      String jpaAttributeName);

  /**
   * The method returns EDM navigation property name for the Java persistence
   * entity relationship name.
   * 
   * @param jpaEntityTypeName
   *            is the Java persistence entity type name
   * @param jpaRelationshipName
   *            is the Java persistence relationship name
   * @return EDM navigation property name mapped to Java persistence entity
   *         relationship name or null if no mapping is available
   */
  public String mapJPARelationship(String jpaEntityTypeName,
      String jpaRelationshipName);

  /**
   * The method returns EDM complex type name for the Java embeddable type
   * name.
   * 
   * @param jpaEmbeddableTypeName
   *            is the Java persistence embeddable type name
   * @return EDM complex type name mapped to Java persistence entity
   *         relationship name or null if no mapping is available
   */
  public String mapJPAEmbeddableType(String jpaEmbeddableTypeName);

  /**
   * The method returns EDM property name for the Java persistence embeddable
   * type's attribute name.
   * 
   * @param jpaEmbeddableTypeName
   *            is the Java persistence
   * @param jpaAttributeName
   *            is the Java persistence attribute name
   * @return EDM property name mapped to Java persistence attribute name or
   *         null if no mapping is available
   */
  public String mapJPAEmbeddableTypeAttribute(String jpaEmbeddableTypeName,
      String jpaAttributeName);

}
