package com.sap.core.odata.api.edm.provider;

import java.util.List;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;

/**
 * Default EDM Provider which is to be extended by the application
 * @author SAP AG
 *
 */
public abstract class EdmProvider {

  /**
   * This method should return an {@link EntityContainerInfo} or <b>null</b> if nothing is found
   * @param name (null for default container)
   * @return {@link EntityContainerInfo} for the given name
   * @throws ODataException
   */
  public EntityContainerInfo getEntityContainerInfo(final String name) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * This method should return an {@link EntityType} or <b>null</b> if nothing is found
   * @param edmFQName
   * @return {@link EntityType} for the given name
   * @throws ODataException
   */
  public EntityType getEntityType(final FullQualifiedName edmFQName) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * This method should return a {@link ComplexType} or <b>null</b> if nothing is found
   * @param edmFQName
   * @return {@link ComplexType} for the given name
   * @throws ODataException
   */
  public ComplexType getComplexType(final FullQualifiedName edmFQName) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * This method should return an {@link Association} or <b>null</b> if nothing is found
   * @param edmFQName
   * @return {@link Association} for the given name
   * @throws ODataException
   */
  public Association getAssociation(final FullQualifiedName edmFQName) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * This method should return an {@link EntitySet} or <b>null</b> if nothing is found
   * @param entityContainer
   * @param name
   * @return {@link EntitySet} for the given container name and entity set name
   * @throws ODataException
   */
  public EntitySet getEntitySet(final String entityContainer, final String name) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * This method should return an {@link AssociationSet} or <b>null</b> if nothing is found
   * @param entityContainer
   * @param association
   * @param sourceEntitySetName
   * @param sourceEntitySetRole
   * @return {@link AssociationSet} for the given container name, association name, source entity set name and source entity set role
   * @throws ODataException
   */
  public AssociationSet getAssociationSet(final String entityContainer, final FullQualifiedName association, final String sourceEntitySetName, final String sourceEntitySetRole) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * This method should return a {@link FunctionImport} or <b>null</b> if nothing is found
   * @param entityContainer
   * @param name
   * @return {@link FunctionImport} for the given container name and function import name
   * @throws ODataException
   */
  public FunctionImport getFunctionImport(final String entityContainer, final String name) throws ODataException {
    throw new ODataNotImplementedException();
  }

  /**
   * This method should return a collection of all {@link Schema} or <b>null</b> if nothing is found
   * @return List<{@link Schema}>
   * @throws ODataException
   */
  public List<Schema> getSchemas() throws ODataException {
    throw new ODataNotImplementedException();
  }
}