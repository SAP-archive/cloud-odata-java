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

import com.sap.core.odata.api.edm.provider.ReferentialConstraintRole;

/**
 * <p>
 * A view on Java Persistence Entity Join Column's "name" and
 * "referenced column name" attributes and Entity Data Model Referential
 * Constraint's dependent and principal roles respectively. Each java
 * persistence entity with properties annotated with Join Columns are
 * transformed into Referential constraints and Referential constraint roles.
 * </p>
 * <p>
 * The implementation of the view provides access to EDM referential constraint
 * roles created from Java Persistence Entity Join Columns. The implementation
 * acts as a container for EDM referential constraint roles. A referential
 * constraint role is consistent only if the principal role and dependent roles
 * can be created from JPA Entity relationships.
 * </p>
 * 
 * @author SAP AG
 *         <p>
 * @DoNotImplement
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmReferentialConstraintView
 * 
 */
public interface JPAEdmReferentialConstraintRoleView extends JPAEdmBaseView {
  /**
   * Two types of EDM roles of a referential constraint.
   */
  public enum RoleType {
    PRINCIPAL, DEPENDENT
  }

  /**
   * The method returns the role type (PRINCIPAL or DEPENDENT)
   * 
   * @return a
   *         {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmReferentialConstraintRoleView.RoleType}
   */
  RoleType getRoleType();

  /**
   * The method returns the Referential constraint role that is currently
   * being processed.
   * 
   * @return an instance of type
   *         {@link com.sap.core.odata.api.edm.provider.ReferentialConstraintRole}
   */
  ReferentialConstraintRole getEdmReferentialConstraintRole();

  /**
   * The method returns the name of JPA attribute's column name (annotated
   * with @Column). The returned Column Name acts as the PRINCIPAL entity
   * type.
   * 
   * @return name of JPA Column name
   */
  String getJPAColumnName();

  /**
   * The method returns the EDM entity type name that holds the
   * relationship/referential constraint. The entity type that acts as a
   * DEPENDENT entity type.
   * 
   * @return name of EDM entity type
   */
  String getEdmEntityTypeName();

  /**
   * The method returns the EDM association name.
   * 
   * @return name of EDM association
   */
  String getEdmAssociationName();

  /**
   * The method tells if there exists a valid referential constraint for a
   * given association.
   * 
   * @return true - if valid referential constraint exits else false
   */
  boolean isExists();

}
