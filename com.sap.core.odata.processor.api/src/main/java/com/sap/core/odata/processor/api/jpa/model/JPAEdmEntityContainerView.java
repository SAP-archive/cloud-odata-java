package com.sap.core.odata.processor.api.jpa.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.EntityContainer;

/**
 * A view on JPA EDM entity container. JPA EDM entity container is built from
 * consistent JPA EDM entity set and consistent JPA EDM association set views.
 * 
 * <p>
 * The implementation of the view provides access to EDM entity containers. The
 * view acts as container for JPA EDM entity containers. A JPA EDM entity
 * container is said to be consistent only if the JPA EDM association set and
 * JPA EDM Entity Set view are consistent.
 * 
 * @author SAP AG
 * @DoNotImplement
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmEntitySetView
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationSetView
 * 
 */
public interface JPAEdmEntityContainerView extends JPAEdmBaseView {
	/**
	 * The method returns the EDM entity container that is currently being
	 * processed.
	 * 
	 * @return an instance of type
	 *         {@link com.sap.core.odata.api.edm.provider.EntityContainer}
	 */
	public EntityContainer getEdmEntityContainer();

	/**
	 * The method returns a list of consistent EDM entity containers
	 * 
	 * @return a list of consistent EDM entity containers
	 */
	public List<EntityContainer> getConsistentEdmEntityContainerList();

	/**
	 * The method returns the JPA EDM entity set view that is currently being
	 * processed.
	 * 
	 * @return an instance of type
	 *         {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmEntitySetView}
	 */
	public JPAEdmEntitySetView getJPAEdmEntitySetView();

	/**
	 * The method returns the JPA EDM association set view that is currently
	 * being processed.
	 * 
	 * @return an instance of type
	 *         {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationSetView}
	 */
	public JPAEdmAssociationSetView getEdmAssociationSetView();
}
