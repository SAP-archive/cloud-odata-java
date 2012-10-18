package com.sap.core.odata.api.edm;

public interface Edm {

  EdmEntityContainer getEntityContainer(String name) throws EdmException;

  EdmEntityType getEntityType(String namespace, String name) throws EdmException;

  EdmComplexType getComplexType(String namespace, String name) throws EdmException;

  EdmAssociation getAssociation(String namespace, String name) throws EdmException;

  EdmServiceMetadata getServiceMetadata();

  EdmEntityContainer getDefaultEntityContainer() throws EdmException;
}