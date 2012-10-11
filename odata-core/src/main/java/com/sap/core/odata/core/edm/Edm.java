package com.sap.core.odata.core.edm;

public interface Edm {

  // TODO: exception handling in general

  EdmEntityContainer getEntityContainer(String name);

  EdmEntityType getEntityType(String namespace, String name);

  EdmComplexType getComplexType(String namespace, String name);

  EdmAssociation getAssociation(String namespace, String name);

  EdmServiceMetadata getServiceMetadata();

  EdmEntityContainer getDefaultEntityContainer();
}
