package com.sap.core.odata.core.experimental.edm.adapter;

import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmSchema;

import com.sap.core.odata.core.edm.Edm;
import com.sap.core.odata.core.edm.EdmAssociation;
import com.sap.core.odata.core.edm.EdmComplexType;
import com.sap.core.odata.core.edm.EdmEntityContainer;
import com.sap.core.odata.core.edm.EdmEntityType;
import com.sap.core.odata.core.edm.EdmException;
import com.sap.core.odata.core.edm.EdmServiceMetadata;

public class EdmAdapter implements Edm {

  private EdmDataServices edmDataServices;

  public EdmAdapter(EdmDataServices edmDataServices) {
    this.edmDataServices = edmDataServices;
  }

  @Override
  public EdmEntityContainer getEntityContainer(String name) throws EdmException {
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
    throw new EdmException("Entity Container " + name + " not found");
  }

  @Override
  public EdmEntityType getEntityType(String namespace, String name) throws EdmException {
    org.odata4j.edm.EdmEntityType edmEntityType = (org.odata4j.edm.EdmEntityType) this.edmDataServices.findEdmEntityType(namespace + "." + name);
    if (edmEntityType != null) {
      return new EdmEntityTypeAdapter(edmEntityType);
    }
    throw new EdmException("Entity Type for full qualified name " + namespace + "." + name + " not found");
  }

  @Override
  public EdmComplexType getComplexType(String namespace, String name) throws EdmException {
    org.odata4j.edm.EdmComplexType edmComplexType = (org.odata4j.edm.EdmComplexType) this.edmDataServices.findEdmComplexType(namespace + "." + name);
    if (edmComplexType != null) {
      return new EdmComplexTypeAdapter(edmComplexType);
    }
    throw new EdmException("Complex Type for full qualified name " + namespace + "." + name + " not found");
  }

  @Override
  public EdmAssociation getAssociation(String namespace, String name) throws EdmException {
    org.odata4j.edm.EdmAssociation edmAssociation = (org.odata4j.edm.EdmAssociation) this.edmDataServices.findEdmAssociation(namespace + "." + name);
    if (edmAssociation != null) {
      return new EdmAssociationAdapter(edmAssociation);
    }
    throw new EdmException("Association for full qualified name " + namespace + "." + name + " not found");
  }

  @Override
  public EdmServiceMetadata getServiceMetadata() {
    return new EdmServiceMetadataAdapter(this.edmDataServices);
  }

  public EdmEntityContainer getDefaultEntityContainer() throws EdmException {
    for (EdmSchema schema : this.edmDataServices.getSchemas()) {
      for (org.odata4j.edm.EdmEntityContainer eec : schema.getEntityContainers()) {
        if (eec.isDefault()) {
          return new EdmEntityContainerAdapter(eec);
        }
      }
    }
    throw new EdmException("No default entity container found");
  }
}