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

import javax.persistence.metamodel.Attribute;

import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.SimpleProperty;

/**
 * A view on Java Persistence Entity Attributes and EDM properties. Java
 * Persistence Attributes of type
 * <ol>
 * <li>embedded ID - are converted into EDM keys</li>
 * <li>ID - are converted into EDM keys</li>
 * <li>attributes - are converted into EDM properties</li>
 * <li>embeddable type - are converted into EDM complex properties</li>
 * <li>relationships - are converted into Associations/Navigation properties</li>
 * </ol>
 * <p>
 * The implementation of the view provides access to EDM properties for a given
 * JPA EDM entity type. The view acts as a container for consistent list of EDM
 * properties of an EDM entity type. EDM property is consistent only if there
 * exists at least one property in the entity type and there is at least one key
 * property.
 * 
 * @author SAP AG
 *         <p>
 * @DoNotImplement
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmKeyView
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmNavigationPropertyView
 * 
 */
public interface JPAEdmPropertyView extends JPAEdmBaseView {
  /**
   * The method returns a simple EDM property.
   * 
   * @return an instance of type
   *         {@link com.sap.core.odata.api.edm.provider.SimpleProperty}
   */
  SimpleProperty getEdmSimpleProperty();

  /**
   * The method returns a JPA EDM key view.
   * 
   * @return an instance of type
   *         {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmKeyView}
   */
  JPAEdmKeyView getJPAEdmKeyView();

  /**
   * The method returns a list of Properties for the given Entity Type.
   * 
   * @return a list of {@link com.sap.core.odata.api.edm.provider.Property}
   */
  List<Property> getEdmPropertyList();

  /**
   * The method returns a JPA Attribute for the given JPA entity type.
   * 
   * @return an instance of type {@link javax.persistence.metamodel.Attribute
   *         <?, ?>}
   */
  Attribute<?, ?> getJPAAttribute();

  /**
   * The method returns a JPA EDM navigation property view.
   * 
   * @return an instance of type
   *         {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmNavigationPropertyView}
   */
  JPAEdmNavigationPropertyView getJPAEdmNavigationPropertyView();

  /**
   * The method returns a JPA EDM Entity Type view that holds the property
   * view.
   * 
   * @return an instance of type
   *         {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView}
   */
  JPAEdmEntityTypeView getJPAEdmEntityTypeView();

  /**
   * The method returns a JPA EDM Complex Type view that holds the property
   * view.
   * 
   * @return an instance of type
   *         {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexTypeView}
   */
  JPAEdmComplexTypeView getJPAEdmComplexTypeView();
}
