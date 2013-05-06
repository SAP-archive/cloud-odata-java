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

import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;

/**
 * <p>
 * A view on Java Persistence Entity Relationship and Entity Data Model
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
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView
 */
public interface JPAEdmAssociationSetView extends JPAEdmBaseView {

	/**
	 * The method returns a consistent list of association sets. An association
	 * set is set to be consistent only if all its mandatory properties can be
	 * completely built from a Java Persistence Relationship.
	 * 
	 * @return a consistent list of {@link com.sap.core.odata.api.edm.provider.AssociationSet}
	 * 
	 */
	List<AssociationSet> getConsistentEdmAssociationSetList();

	/**
	 * The method returns an association set that is currently being processed.
	 * 
	 * @return an instance of type {@link com.sap.core.odata.api.edm.provider.AssociationSet}
	 */
	AssociationSet getEdmAssociationSet();

	/**
	 * The method returns an association from which the association set is
	 * currently being processed.
	 * 
	 * @return an instance of type {@link com.sap.core.odata.api.edm.provider.Association}
	 */
	Association getEdmAssociation();

}
