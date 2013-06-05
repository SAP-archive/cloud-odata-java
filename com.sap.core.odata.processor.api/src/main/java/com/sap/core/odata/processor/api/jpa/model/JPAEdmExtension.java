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
 * The interface provides methods to extend JPA EDM containers.
 * 
 * @author SAP AG
 * 
 */
public interface JPAEdmExtension {
  /**
   * The method is used to extend the JPA EDM schema view. Use this method to
   * register custom operations.
   * 
   * @param view
   *            is the schema view
   * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView#registerOperations(Class,
   *      String[])
   * @deprecated Use {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmExtension#extendWithOperations(JPAEdmSchemaView view)}
   * 
   */
  @Deprecated
  public void extend(JPAEdmSchemaView view);

  /**
   * The method is used to extend the JPA EDM schema view with custom operations. Use this method to
   * register custom operations.
   * 
   * @param view
   *            is the schema view
   * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView#registerOperations(Class,
   *      String[])
   * 
   */
  public void extendWithOperation(JPAEdmSchemaView view);

  /**
   * The method is used to extend the JPA EDM schema view with Entities, Entity Sets, Navigation Property and Association. 
   * 
   * @param view
   *            is the schema view
   * 
   */
  public void extendJPAEdmSchema(JPAEdmSchemaView view);

}
