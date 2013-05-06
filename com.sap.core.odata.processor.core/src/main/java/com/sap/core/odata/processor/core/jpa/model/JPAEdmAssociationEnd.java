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
package com.sap.core.odata.processor.core.jpa.model;

import javax.persistence.metamodel.Attribute.PersistentAttributeType;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationEndView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmNameBuilder;

public class JPAEdmAssociationEnd extends JPAEdmBaseViewImpl implements
    JPAEdmAssociationEndView {

  private JPAEdmEntityTypeView entityTypeView = null;
  private JPAEdmPropertyView propertyView = null;
  private AssociationEnd currentAssociationEnd1 = null;
  private AssociationEnd currentAssociationEnd2 = null;

  public JPAEdmAssociationEnd(final JPAEdmEntityTypeView entityTypeView,
      final JPAEdmPropertyView propertyView) {
    super(entityTypeView);
    this.entityTypeView = entityTypeView;
    this.propertyView = propertyView;
  }

  @Override
  public JPAEdmBuilder getBuilder() {
    if (builder == null) {
      builder = new JPAEdmAssociationEndBuilder();
    }

    return builder;
  }

  @Override
  public AssociationEnd getEdmAssociationEnd1() {
    return currentAssociationEnd1;
  }

  @Override
  public AssociationEnd getEdmAssociationEnd2() {
    return currentAssociationEnd2;
  }

  private class JPAEdmAssociationEndBuilder implements JPAEdmBuilder {

    @Override
    public void build() throws ODataJPAModelException {

      currentAssociationEnd1 = new AssociationEnd();
      currentAssociationEnd2 = new AssociationEnd();

      JPAEdmNameBuilder.build(JPAEdmAssociationEnd.this, entityTypeView,
          propertyView);

      currentAssociationEnd1.setRole(currentAssociationEnd1.getType()
          .getName());
      currentAssociationEnd2.setRole(currentAssociationEnd2.getType()
          .getName());

      setEdmMultiplicity(propertyView.getJPAAttribute()
          .getPersistentAttributeType());
    }

    private void setEdmMultiplicity(final PersistentAttributeType type) {
      switch (type) {
      case ONE_TO_MANY:
        currentAssociationEnd1.setMultiplicity(EdmMultiplicity.ONE);
        currentAssociationEnd2.setMultiplicity(EdmMultiplicity.MANY);
        break;
      case MANY_TO_MANY:
        currentAssociationEnd1.setMultiplicity(EdmMultiplicity.MANY);
        currentAssociationEnd2.setMultiplicity(EdmMultiplicity.MANY);
        break;
      case MANY_TO_ONE:
        currentAssociationEnd1.setMultiplicity(EdmMultiplicity.MANY);
        currentAssociationEnd2.setMultiplicity(EdmMultiplicity.ONE);
        break;
      case ONE_TO_ONE:
        currentAssociationEnd1.setMultiplicity(EdmMultiplicity.ONE);
        currentAssociationEnd2.setMultiplicity(EdmMultiplicity.ONE);
        break;
      default:
        break;
      }
    }
  }

  @Override
  public boolean compare(final AssociationEnd end1, final AssociationEnd end2) {
    if ((end1.getType().equals(currentAssociationEnd1.getType())
        && end2.getType().equals(currentAssociationEnd2.getType())
        && end1.getMultiplicity().equals(
            currentAssociationEnd1.getMultiplicity()) && end2
        .getMultiplicity().equals(
            currentAssociationEnd2.getMultiplicity()))
        || (end1.getType().equals(currentAssociationEnd2.getType())
            && end2.getType().equals(
                currentAssociationEnd1.getType())
            && end1.getMultiplicity().equals(
                currentAssociationEnd2.getMultiplicity()) && end2
            .getMultiplicity().equals(
                currentAssociationEnd1.getMultiplicity()))) {
      return true;
    }

    return false;
  }
}
