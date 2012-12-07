package com.sap.core.odata.api.edm.provider;

import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.exception.ODataException;

/**
 * Default EDM Provider which is to be extended by the application
 * @author SAP AG
 *
 */
public class EdmProviderDefault implements EdmProvider {

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.EdmProvider#getEntityContainer(java.lang.String)
   */
  @Override
  public EntityContainerInfo getEntityContainerInfo(String name) throws ODataException {
    throw new NotImplementedException();
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.EdmProvider#getEntityType(com.sap.core.odata.api.edm.FullQualifiedName)
   */
  @Override
  public EntityType getEntityType(FullQualifiedName edmFQName) throws ODataException {
    throw new NotImplementedException();
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.EdmProvider#getComplexType(com.sap.core.odata.api.edm.FullQualifiedName)
   */
  @Override
  public ComplexType getComplexType(FullQualifiedName edmFQName) throws ODataException {
    throw new NotImplementedException();
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.EdmProvider#getAssociation(com.sap.core.odata.api.edm.FullQualifiedName)
   */
  @Override
  public Association getAssociation(FullQualifiedName edmFQName) throws ODataException {
    throw new NotImplementedException();
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.EdmProvider#getEntitySet(java.lang.String, java.lang.String)
   */
  @Override
  public EntitySet getEntitySet(String entityContainer, String name) throws ODataException {
    throw new NotImplementedException();
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.EdmProvider#getAssociationSet(java.lang.String, com.sap.core.odata.api.edm.FullQualifiedName, java.lang.String, java.lang.String)
   */
  @Override
  public AssociationSet getAssociationSet(String entityContainer, FullQualifiedName association, String sourceEntitySetName, String sourceEntitySetRole) throws ODataException {
    throw new NotImplementedException();
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.EdmProvider#getFunctionImport(java.lang.String, java.lang.String)
   */
  @Override
  public FunctionImport getFunctionImport(String entityContainer, String name) throws ODataException {
    throw new NotImplementedException();
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.EdmProvider#getSchemas()
   */
  @Override
  public List<Schema> getSchemas() throws ODataException {
    throw new NotImplementedException();
  }
}