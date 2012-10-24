package com.sap.core.odata.api.edm;

/**
 * Entity Data Model (EDM)
 * 
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface Edm {

  /**
   * Get entity container by name
   * 
   * @param name
   * @return {@link EdmEntityContainer}
   * @throws EdmException
   */
  EdmEntityContainer getEntityContainer(String name) throws EdmException;

  /**
   * Get entity type by full qualified name
   * 
   * @param namespace
   * @param name
   * @return {@link EdmEntityType}
   * @throws EdmException
   */
  EdmEntityType getEntityType(String namespace, String name) throws EdmException;

  /**
   * Get complex type by full qualified name
   * 
   * @param namespace
   * @param name
   * @return {@link EdmComplexType}
   * @throws EdmException
   */
  EdmComplexType getComplexType(String namespace, String name) throws EdmException;

  /**
   * Get association by full qualified name
   * 
   * @param namespace
   * @param name
   * @return {@link EdmAssociation}
   * @throws EdmException
   */
  EdmAssociation getAssociation(String namespace, String name) throws EdmException;

  /**
   * Get service metadata
   * 
   * @return {@link EdmServiceMetadata}
   */
  EdmServiceMetadata getServiceMetadata();

  /**
   * Get default entity container
   * 
   * @return {@link EdmEntityContainer}
   * @throws EdmException
   */
  EdmEntityContainer getDefaultEntityContainer() throws EdmException;
}