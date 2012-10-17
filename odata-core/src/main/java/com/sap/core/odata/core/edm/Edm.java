package com.sap.core.odata.core.edm;

public interface Edm {

  EdmEntityContainer getEntityContainer(String name) throws EdmException;

  EdmEntityType getEntityType(String namespace, String name) throws EdmException;

  EdmComplexType getComplexType(String namespace, String name) throws EdmException;

  EdmAssociation getAssociation(String namespace, String name) throws EdmException;

  EdmServiceMetadata getServiceMetadata();
}
