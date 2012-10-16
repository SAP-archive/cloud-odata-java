package com.sap.core.odata.core.experimental.edm.adapter;

import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmSchema;

import com.sap.core.odata.core.edm.Edm;
import com.sap.core.odata.core.edm.EdmAssociation;
import com.sap.core.odata.core.edm.EdmComplexType;
import com.sap.core.odata.core.edm.EdmEntityContainer;
import com.sap.core.odata.core.edm.EdmEntityType;
import com.sap.core.odata.core.edm.EdmServiceMetadata;

public class EdmAdapter implements Edm {

  private EdmDataServices edmDataServices;

  public EdmAdapter(EdmDataServices edmDataServices) {
    this.edmDataServices = edmDataServices;
  }

  public EdmEntityContainer getDefaultEntityContainer() {
    for (EdmSchema schema : this.edmDataServices.getSchemas()) {
      for (org.odata4j.edm.EdmEntityContainer eec : schema.getEntityContainers()) {
        if (eec.isDefault()) {
          return new EdmEntityContainerAdapter(eec);
        }
      }
    }
    return null;
  }

  @Override
  public EdmEntityContainer getEntityContainer(String name) {
    if (name == null) {
      return getDefaultEntityContainer();
    }
    
    for (EdmSchema schema : this.edmDataServices.getSchemas()) {
      for (org.odata4j.edm.EdmEntityContainer eec : schema.getEntityContainers()) {
        if (name.equals(eec.getName())) {
          return new EdmEntityContainerAdapter(eec);
        }
      }
    }
    return null;
  }

  @Override
  public EdmEntityType getEntityType(String namespace, String name) {
    org.odata4j.edm.EdmEntityType edmEntityType = (org.odata4j.edm.EdmEntityType) this.edmDataServices.findEdmEntityType(namespace + "." + name);
    if (edmEntityType != null) {
      return new EdmEntityTypeAdapter(edmEntityType);
    }
    return null;
  }

  @Override
  public EdmComplexType getComplexType(String namespace, String name) {
    org.odata4j.edm.EdmComplexType edmComplexType = (org.odata4j.edm.EdmComplexType) this.edmDataServices.findEdmComplexType(namespace + "." + name);
    if (edmComplexType != null) {
      return new EdmComplexTypeAdapter(edmComplexType);
    }
    return null;
  }

  @Override
  public EdmAssociation getAssociation(String namespace, String name) {
    org.odata4j.edm.EdmAssociation edmAssociation = (org.odata4j.edm.EdmAssociation) this.edmDataServices.findEdmAssociation(namespace + "." + name);
    if (edmAssociation != null) {
      return new EdmAssociationAdapter(edmAssociation);
    }
    return null;
  }

  @Override
  public EdmServiceMetadata getServiceMetadata() {
    return new EdmServiceMetadataAdapter(this.edmDataServices);
  }

}
