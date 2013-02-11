package com.sap.core.odata.processor.api.jpa.model;

import com.sap.core.odata.api.edm.provider.ReferentialConstraint;

/**
 * <p>
 * A view on Java Persistence Entity Join Columns and Entity Data Model
 * Referential Constraint. Each java persistence entity with properties
 * annotated with Join Columns are transformed into Referential constraints.
 * </p>
 * <p>
 * The implementation of the view provides access to EDM referential constraint
 * created from Java Persistence Entity Join Columns. The implementation acts as
 * a container for EDM referential constraint. A referential constraint is said
 * to be consistent only if referential constraint role is consistent.
 * </p>
 * 
 * @author SAP AG<br>
 * @DoNotImplement
 * <br>
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmReferentialConstraintRoleView
 * 
 */
public interface JPAEdmReferentialConstraintView extends JPAEdmBaseView {

	/**
	 * The method returns EDM referential constraint created from Java
	 * persistence Entity Join Columns.
	 * 
	 * @return an instance of type
	 *         {@link com.sap.core.odata.api.edm.provider.ReferentialConstraint}
	 */
	public ReferentialConstraint getEdmReferentialConstraint();

	/**
	 * The method returns if a valid referential constraint exists for a given
	 * EDM association. If there exists a JPA entity relationship with join
	 * column having a valid "Name" and "ReferenceColumnName", that can be
	 * mapped to EDM properties in dependent and source EDM entities
	 * respectively then a valid EDM referential constraint exists.
	 * 
	 * @return true if there exists a valid referential constraint else false.
	 */
	public boolean isExists();

	/**
	 * The method returns the name of EDM Association.
	 * 
	 * @return name of an EDM association
	 */
	public String getEdmRelationShipName();
}
