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
package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAnnotatable;
import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.NavigationProperty;

public class EdmNavigationPropertyImplProv extends EdmTypedImplProv implements EdmNavigationProperty, EdmAnnotatable {

  private NavigationProperty navigationProperty;

  public EdmNavigationPropertyImplProv(final EdmImplProv edm, final NavigationProperty property) throws EdmException {
    super(edm, property.getName(), null, null);
    navigationProperty = property;
  }

  @Override
  public EdmType getType() throws EdmException {
    return getRelationship().getEnd(navigationProperty.getToRole()).getEntityType();
  }

  @Override
  public EdmMultiplicity getMultiplicity() throws EdmException {
    return ((EdmAssociationImplProv) getRelationship()).getEndMultiplicity(navigationProperty.getToRole());
  }

  @Override
  public EdmAssociation getRelationship() throws EdmException {
    final FullQualifiedName relationship = navigationProperty.getRelationship();
    return edm.getAssociation(relationship.getNamespace(), relationship.getName());
  }

  @Override
  public String getFromRole() throws EdmException {
    return navigationProperty.getFromRole();
  }

  @Override
  public String getToRole() throws EdmException {
    return navigationProperty.getToRole();
  }

  @Override
  public EdmAnnotations getAnnotations() throws EdmException {
    return new EdmAnnotationsImplProv(navigationProperty.getAnnotationAttributes(), navigationProperty.getAnnotationElements());
  }

  @Override
  public EdmMapping getMapping() throws EdmException {
    return navigationProperty.getMapping();
  }

}
