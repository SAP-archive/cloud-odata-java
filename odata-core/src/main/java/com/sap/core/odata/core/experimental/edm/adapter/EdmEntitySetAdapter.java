package com.sap.core.odata.core.experimental.edm.adapter;

import org.odata4j.edm.EdmEntityContainer;

import com.sap.core.odata.core.edm.EdmEntitySet;
import com.sap.core.odata.core.edm.EdmEntityType;
import com.sap.core.odata.core.edm.EdmNavigationProperty;

public class EdmEntitySetAdapter extends EdmNamedAdapter implements EdmEntitySet {

  private org.odata4j.edm.EdmEntitySet edmEntitySet;
  private EdmEntityContainer edmEntityContainer;

  public EdmEntitySetAdapter(org.odata4j.edm.EdmEntitySet edmEntitySet, EdmEntityContainer edmEntityContainer) {
    super(edmEntitySet.getName());
    this.edmEntitySet = edmEntitySet;
    this.edmEntityContainer = edmEntityContainer;
  }

  @Override
  public EdmEntityType getEntityType() {
    return new EdmEntityTypeAdapter(this.edmEntitySet.getType());
  }

  @Override
  public EdmEntitySet getRelatedEntitySet(EdmNavigationProperty navigationProperty) {
    String relationship = navigationProperty.getRelationship().getName();
    String toRole = navigationProperty.getToRole();

    for (org.odata4j.edm.EdmAssociationSet edmAssociationSet : this.edmEntityContainer.getAssociationSets()) {
      if (edmAssociationSet.getAssociation().getName().equals(relationship)) {
        if (edmAssociationSet.getEnd1().getRole().getRole().equals(toRole)) {
          return new EdmEntitySetAdapter(edmAssociationSet.getEnd1().getEntitySet(), this.edmEntityContainer);
        } else if (edmAssociationSet.getEnd2().getRole().getRole().equals(toRole)) {
          return new EdmEntitySetAdapter(edmAssociationSet.getEnd2().getEntitySet(), this.edmEntityContainer);
        }
      }
    }
    return null;
  }

  @Override
  public com.sap.core.odata.core.edm.EdmEntityContainer getEntityContainer() {
    return new EdmEntityContainerAdapter(this.edmEntityContainer);
  }

}
