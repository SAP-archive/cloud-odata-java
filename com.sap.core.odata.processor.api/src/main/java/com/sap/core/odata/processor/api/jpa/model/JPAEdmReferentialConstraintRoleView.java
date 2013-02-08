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
 * @see JPAEdmReferentialConstraintView
 * 
 */
public interface JPAEdmReferentialConstraintRoleView extends JPAEdmBaseView {
	/**
	 * This provides values for Role Types.
	 * 
	 */
	public enum RoleType {
		PRINCIPAL, DEPENDENT
	}

	RoleType getRoleType();

	ReferentialConstraintRole getEdmReferentialConstraintRole();

	String getJPAColumnName();

	String getEdmEntityTypeName();

	String getEdmAssociationName();

	boolean isExists();

}
