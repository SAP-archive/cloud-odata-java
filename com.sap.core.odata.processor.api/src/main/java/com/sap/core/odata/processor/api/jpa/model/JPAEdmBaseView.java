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

import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmMappingModelAccess;

/**
 * <p>
 * A base view on Java Persistence Model and Entity Data Model.
 * </p>
 * <p>
 * The implementation of the view acts as a base container for containers of
 * Java Persistence Model and Entity Data Model elements.
 * 
 * @author SAP AG
 *         <p>
 * @DoNotImplement
 * 
 */
public interface JPAEdmBaseView {
  /**
   * 
   * @return Java Persistence Unit Name
   */
  public String getpUnitName();

  /**
   * The method returns the Java Persistence MetaModel
   * 
   * @return a meta model of type
   *         {@link javax.persistence.metamodel.Metamodel}
   */
  public Metamodel getJPAMetaModel();

  /**
   * The method returns a builder for building Entity Data Model elements from
   * Java Persistence Model Elements
   * 
   * @return a builder of type
   *         {@link com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder}
   */
  public JPAEdmBuilder getBuilder();

  /**
   * The method returns the if the container is consistent without any errors
   * 
   * @return <ul>
   *         <li>true - if the container is consistent without errors</li>
   *         <li>false - if the container is inconsistent with errors</li>
   * 
   */
  public boolean isConsistent();

  /**
   * The method cleans the container.
   */
  public void clean();

  /**
   * The method returns a reference to JPA EDM mapping model access.
   * 
   * @return an instance to JPA EDM mapping model access
   */
  public JPAEdmMappingModelAccess getJPAEdmMappingModelAccess();

  /**
   * The method returns a reference to JPA EDM extension if available else
   * null.
   * 
   * @return an instance of JPA Edm Extension
   */
  public JPAEdmExtension getJPAEdmExtension();
}
