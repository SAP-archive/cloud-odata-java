package com.sap.core.odata.api.edm.provider;

import java.util.List;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.exception.ODataException;

/**
 * Provider for the EDM. Implemented by {@link EdmProviderDefault} which shall be extended by an application.
 * <p>IMPORTANT
 * Do not implement this interface. This interface is intended for usage only.
 *
 * @author SAP AG
 */
public interface EdmProvider {

  /**
   * This method should return an {@link EntityContainerInfo} or <b>null</b> if nothing is found
   * @param name (null for default container)
   * @return {@link EntityContainerInfo} for the given name
   * @throws ODataException
   */
  EntityContainerInfo getEntityContainerInfo(String name) throws ODataException;

  /**
   * This method should return an {@link EntityType} or <b>null</b> if nothing is found
   * @param edmFQName
   * @return {@link EntityType} for the given name
   * @throws ODataException
   */
  EntityType getEntityType(FullQualifiedName edmFQName) throws ODataException;

  /**
   * This method should return a {@link ComplexType} or <b>null</b> if nothing is found
   * @param edmFQName
   * @return {@link ComplexType} for the given name
   * @throws ODataException
   */
  ComplexType getComplexType(FullQualifiedName edmFQName) throws ODataException;

  /**
   * This method should return an {@link Association} or <b>null</b> if nothing is found
   * @param edmFQName
   * @return {@link Association} for the given name
   * @throws ODataException
   */
  Association getAssociation(FullQualifiedName edmFQName) throws ODataException;

  /**
   * This method should return an {@link EntitySet} or <b>null</b> if nothing is found
   * @param entityContainer
   * @param name
   * @return {@link EntitySet} for the given container name and entity set name
   * @throws ODataException
   */
  EntitySet getEntitySet(String entityContainer, String name) throws ODataException;

  /**
   * This method should return an {@link AssociationSet} or <b>null</b> if nothing is found
   * @param entityContainer
   * @param association
   * @param sourceEntitySetName
   * @param sourceEntitySetRole
   * @return {@link AssociationSet} for the given container name, association name, source entity set name and source entity set role
   * @throws ODataException
   */
  AssociationSet getAssociationSet(String entityContainer, FullQualifiedName association, String sourceEntitySetName, String sourceEntitySetRole) throws ODataException;

  /**
   * This method should return a {@link FunctionImport} or <b>null</b> if nothing is found
   * @param entityContainer
   * @param name
   * @return {@link FunctionImport} for the given container name and function import name
   * @throws ODataException
   */
  FunctionImport getFunctionImport(String entityContainer, String name) throws ODataException;

  /**
   * This method should return a collection of all {@link Schema} or <b>null</b> if nothing is found
   * @return List<{@link Schema}>
   * @throws ODataException
   */
  List<Schema> getSchemas() throws ODataException;

  //TODO required for validation if namspace is defined????
  //List<EdmNamespaceInfo> getNamespaceInfos() throws ODataMessageException;
}