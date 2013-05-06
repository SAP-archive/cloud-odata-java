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
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView
 * 
 */
public interface JPAEdmAssociationEndView extends JPAEdmBaseView {

  /**
   * The method gets the one of the association ends present in the container.
   * 
   * @return one of the
   *         {@link com.sap.core.odata.api.edm.provider.AssociationEnd} for an
   *         {@link com.sap.core.odata.api.edm.provider.Association}
   */
  AssociationEnd getEdmAssociationEnd2();

  /**
   * The method gets the other association end present in the container.
   * 
   * @return one of the
   *         {@link com.sap.core.odata.api.edm.provider.AssociationEnd} for an
   *         {@link com.sap.core.odata.api.edm.provider.Association}
   */
  AssociationEnd getEdmAssociationEnd1();

  /**
   * The method compares two ends {<b>end1, end2</b>} of an
   * {@link com.sap.core.odata.api.edm.provider.AssociationEnd} against its
   * two ends.
   * 
   * The Method compares the following properties in each end for equality <i>
   * <ul>
   * <li>{@link com.sap.core.odata.api.edm.FullQualifiedName} of End Type</li>
   * <li>{@link com.sap.core.odata.api.edm.EdmMultiplicity} of End</li>
   * </ul>
   * </i>
   * 
   * @param end1
   *            one end of type
   *            {@link com.sap.core.odata.api.edm.provider.AssociationEnd} of
   *            an {@link com.sap.core.odata.api.edm.provider.Association}
   * @param end2
   *            other end of type
   *            {@link com.sap.core.odata.api.edm.provider.AssociationEnd} of
   *            an {@link com.sap.core.odata.api.edm.provider.Association}
   *            <p>
   * @return <ul>
   *         <li><i>true</i> - Only if the properties of <b>end1</b> matches
   *         with all the properties of any one end and only if the properties
   *         of <b>end2</b> matches with all the properties of the remaining
   *         end</li> <li><i>false</i> - Otherwise</li>
   *         </ul>
   */
  boolean compare(AssociationEnd end1, AssociationEnd end2);

}
