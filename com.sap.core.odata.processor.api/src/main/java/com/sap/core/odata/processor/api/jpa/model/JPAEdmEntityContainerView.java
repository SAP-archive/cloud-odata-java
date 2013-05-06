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
