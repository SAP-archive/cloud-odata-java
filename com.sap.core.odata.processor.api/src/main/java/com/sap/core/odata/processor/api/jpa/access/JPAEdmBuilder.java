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
package com.sap.core.odata.processor.api.jpa.access;

import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

/**
 * JPAEdmBuilder interface provides methods for building elements of an Entity Data Model (EDM) from
 * a Java Persistence Model.
 * 
 * @author SAP AG
 * 
 */
public interface JPAEdmBuilder {
	/**
	 * The Method builds EDM Elements by transforming JPA MetaModel. The method
	 * processes EDM JPA Containers which could be accessed using the following
	 * views,
	 * <ul>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationSetView}
	 * </li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView}</li>
	 * <li> {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmBaseView}</li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexPropertyView}
	 * </li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexTypeView}</li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityContainerView}
	 * </li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmEntitySetView}</li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView}</li>
	 * <li> {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmKeyView}</li>
	 * <li> {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmModelView}</li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmNavigationPropertyView}
	 * </li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView}</li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmReferentialConstraintRoleView}
	 * </li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmReferentialConstraintView}
	 * </li>
	 * <li> {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView}</li>
	 * </ul>
	 * 
	 * @throws ODataJPARuntimeException
	 **/
	public void build() throws ODataJPAModelException, ODataJPARuntimeException;
}
