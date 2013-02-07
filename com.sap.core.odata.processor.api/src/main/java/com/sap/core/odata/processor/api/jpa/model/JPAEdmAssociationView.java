package com.sap.core.odata.processor.api.jpa.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.Association;

/**
 * <p>
 * A View on Java Persistence Entity Relationship and Entity Data Model
 * Association.
 * </p>
 * <p>
 * The implementation of the view provides access to EDM Association created
 * from Java Persistence Entity Relationships. The implementation acts as a
 * container for list of association that are consistent.
 * 
 * An Association is said to be consistent only
 * <ol>
 * <li>If both the Ends of Association are consistent</li>
 * <li>If referential constraint exists for the Association then it should be
 * consistent</li>
 * </ol>
 * </p>
 * 
 * @author SAP AG
 *         <p>
 * @DoNotImplement
 * @see 
 * {@link JPAEdmAssociationSetView}, 
 * {@link JPAEdmReferentialConstraintView}
 * 
 */
public interface JPAEdmAssociationView extends JPAEdmBaseView {

	/**
	 * The method returns an association which is currently being processed.
	 * 
	 * @return an {@link Association}
	 */
	public Association getEdmAssociation();

	/**
	 * The method returns a consistent list of associations. An association is
	 * set to be consistent only if all its mandatory properties can be
	 * completely built from a Java Persistence Relationship.
	 * 
	 * @return a consistent list of {@link Association}
	 * 
	 */
	public List<Association> getConsistentEdmAssociationList();

	/**
	 * The method adds {@link JPAEdmAssociationView} to its container
	 * 
	 * @param associationView
	 *            of type {@link JPAEdmAssociationView}
	 */
	public void addJPAEdmAssociationView(JPAEdmAssociationView associationView);

	/**
	 * The method searches for an Association in its container against the
	 * search parameter <b>view</b> of type {@link JPAEdmAssociationView}.
	 * 
	 * The Association in the container <b>view</b> is searched against the
	 * consistent list of Association stored in this container.
	 * 
	 * @param view
	 *            of type {@link JPAEdmAssociationView}
	 * @return {@link Association} if found in the container
	 */
	public Association searchAssociation(JPAEdmAssociationEndView view);

	/**
	 * The method adds the referential constraint view to its container.
	 * <p>
	 * <b>Note: </b>The referential constraint view is added only if it exists.
	 * </p>
	 * 
	 * @param refView
	 *            of type {@link JPAEdmReferentialConstraintView}
	 */
	public void addJPAEdmRefConstraintView(
			JPAEdmReferentialConstraintView refView);
	
	/**
	 * The method returns the referential constraint view that is currently being processed.
	 * @return {@link JPAEdmReferentialConstraintView}
	 */
	public JPAEdmReferentialConstraintView getJPAEdmReferentialConstraintView();
}
