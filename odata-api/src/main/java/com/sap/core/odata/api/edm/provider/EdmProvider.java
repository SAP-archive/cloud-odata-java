package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.exception.ODataException;

/**
 * @author SAP AG
 * <p>
 * Provider for the EDM. Is implemented by the application and is used inside the library to get the EDM
 *
 */
public interface EdmProvider {

  /**
   * This method should return an {@link EntityContainer}
   * @param name 
   * @return {@link EntityContainer} for the given name
   * @throws ODataException
   */
  EntityContainer getEntityContainer(String name) throws ODataException;

  /**
   * This method should return an {@link EntityType}
   * @param edmFQName
   * @return {@link EntityType} for the given name
   * @throws ODataException
   */
  EntityType getEntityType(FullQualifiedName edmFQName) throws ODataException;

  /**
   * This method should return a {@link ComplexType}
   * @param edmFQName
   * @return {@link ComplexType} for the given name
   * @throws ODataException
   */
  ComplexType getComplexType(FullQualifiedName edmFQName) throws ODataException;

  /**
   * This method should return an {@link Association}
   * @param edmFQName
   * @return {@link Association} for the given name
   * @throws ODataException
   */
  Association getAssociation(FullQualifiedName edmFQName) throws ODataException;

  /**
   * This method should return an {@link EntitySet}
   * @param entityContainer
   * @param name
   * @return {@link EntitySet} for the given container name and entity set name
   * @throws ODataException
   */
  EntitySet getEntitySet(String entityContainer, String name) throws ODataException;

  /**
   * This method should return an {@link AssociationSet}
   * @param entityContainer
   * @param association
   * @param sourceEntitySetName
   * @param sourceEntitySetRole
   * @return {@link AssociationSet} for the given container name, association name, source entity set name and source entity set role
   * @throws ODataException
   */
  AssociationSet getAssociationSet(String entityContainer, FullQualifiedName association, String sourceEntitySetName, String sourceEntitySetRole) throws ODataException;

  /**
   * This method should return a {@link FunctionImport}
   * @param entityContainer
   * @param name
   * @return {@link FunctionImport} for the given container name and function import name
   * @throws ODataException
   */
  FunctionImport getFunctionImport(String entityContainer, String name) throws ODataException;

  /**
   * This method should return a collection of all {@link Schema}
   * @return Collection<{@link Schema}>
   * @throws ODataException
   */
  Collection<Schema> getSchemas() throws ODataException;

  //TODO required for validation if namspace is defined????
  //List<EdmNamespaceInfo> getNamespaceInfos() throws ODataMessageException;
}