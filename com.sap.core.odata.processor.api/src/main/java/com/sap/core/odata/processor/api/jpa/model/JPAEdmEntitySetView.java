package com.sap.core.odata.processor.api.jpa.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.EntitySet;

/**
 * A view on Java Persistence entity type and EDM entity sets. Java persistence
 * entity types are converted into EDM entity types and EDM entity sets.
 * <p>
 * The implementation of the view provides access to EDM entity sets for the
 * given JPA EDM entity type. The view acts as a container for consistent list
 * of EDM entity sets. An EDM entity set is said to be consistent only if it has
 * consistent EDM entity types.
 * 
 * @author SAP AG
 *         <p>
 * @DoNotImplement
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView
 * 
 */
public interface JPAEdmEntitySetView extends JPAEdmBaseView {
  /**
   * The method returns an EDM entity set that is currently being processed.
   * 
   * @return an instance of type
   *         {@link com.sap.core.odata.api.edm.provider.EntitySet}
   */
  public EntitySet getEdmEntitySet();

  /**
   * The method returns a list of consistent EDM entity sets.
   * 
   * @return a list of EDM entity sets
   */
  public List<EntitySet> getConsistentEdmEntitySetList();

  /**
   * The method returns a JPA EDM entity type view that is currently being
   * processed. JPA EDM entity set view is built from JPA EDM entity type
   * view.
   * 
   * @return an instance of type
   *         {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView}
   */
  public JPAEdmEntityTypeView getJPAEdmEntityTypeView();

}
