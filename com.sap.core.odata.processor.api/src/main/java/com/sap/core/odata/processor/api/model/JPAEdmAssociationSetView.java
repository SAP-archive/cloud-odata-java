package com.sap.core.odata.processor.api.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;

/**
 * <p>
 * A View on Java Persistence Entity Relationship and Entity Data Model
 * Association Set.
 * </p>
 * <p>
 * The implementation of the view provides access to EDM Association Set created
 * from Java Persistence Entity Relationship. The implementation act as a
 * container for list of association sets that are consistent.
 * </p>
 * 
 * @author SAP AG
 *         <p>
 * @DoNotImplement
 * 
 */
public interface JPAEdmAssociationSetView extends JPAEdmBaseView {

	/**
	 * The method returns a consistent list of association sets. An association
	 * set is set to be consistent only if all its mandatory properties can be
	 * completely built from a Java Persistence Relationship.
	 * 
	 * @return a consistent list of {@link AssociationSet}
	 * 
	 */
	List<AssociationSet> getConsistentEdmAssociationSetList();

	/**
	 * The method returns an association set that is currently being processed.
	 * 
	 * @return an {@link AssociationSet}
	 */
	AssociationSet getEdmAssociationSet();

	/**
	 * The method returns an association from which the association set is
	 * currently being processed.
	 * 
	 * @return an {@link Association}
	 */
	Association getEdmAssociation();

}
