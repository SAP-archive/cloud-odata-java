package com.sap.core.odata.processor.api.jpa.model;

import com.sap.core.odata.api.edm.provider.AssociationEnd;

/**
 * <p>
 * A view on Java Persistence Entity Relationship and Entity Data Model
 * Association End.
 * </p>
 * <p>
 * The implementation of the view provides access to EDM Association Ends
 * created from Java Persistence Entity Relationships. The implementation acts
 * as a container for Association Ends.
 * </p>
 * 
 * @author SAP AG
 *         <p>
 * @DoNotImplement
 * @see JPAEdmAssociationView
 * 
 */
public interface JPAEdmAssociationEndView extends JPAEdmBaseView {

	/**
	 * The method gets the one of the association ends present in the container. 
	 * @return one of the {@link AssociationEnd} for an {@link Association}
	 */
	AssociationEnd getEdmAssociationEnd2();

	/**
	 * The method gets the other association end present in the container.
	 * @return one of the {@link AssociationEnd} for an {@link Association}
	 */
	AssociationEnd getEdmAssociationEnd1();

	/**
	 * The method compares two ends {<b>end1, end2</b>} of an {@link AssociationEnd}
	 * against its two ends.
	 * 
	 * The Method compares the following properties in each end for equality <i>
	 * <ul>
	 * <li>{@link FullQualifiedName} of End Type</li>
	 * <li>{@link EdmMultiplicity} of End</li>
	 * </ul>
	 * </i>
	 * 
	 * @param end1
	 *            one end of type {@link AssociationEnd} of an
	 *            {@link Association}
	 * @param end2
	 *            other end of type {@link AssociationEnd of an
	 *            {@link Association}
	 *            <p>
	 * @return <ul>
	 *         <li><i>true</i> - Only if the properties of <b>end1</b> matches
	 *         with all the properties of any one end and only if the properties
	 *         of <b>end2</b> matches with all the properties of the remaining
	 *         end</li>
	 *         <li><i>false</i> - Otherwise</li>
	 *         </ul>
	 */
	boolean compare(AssociationEnd end1, AssociationEnd end2);

}
