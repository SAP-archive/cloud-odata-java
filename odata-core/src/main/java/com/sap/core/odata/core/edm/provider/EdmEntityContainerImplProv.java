package com.sap.core.odata.core.edm.provider;

import java.util.Map;

import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmAssociationSet;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.exception.ODataException;

public class EdmEntityContainerImplProv implements EdmEntityContainer {

  private EntityContainer entityContainer;
  private EdmImplProv edm;
  private Map<String, EdmEntitySet> entitySets;
  private Map<String, EdmAssociationSet> associationSets;
  private Map<String, EdmFunctionImport> functionImports;
  private EdmEntityContainer extendedEntityContainer;

  public EdmEntityContainerImplProv(EntityContainer entityContainer, EdmImplProv edm) throws EdmException {
    this.entityContainer = entityContainer;
    this.edm = edm;

    if (entityContainer.getExtendz() != null) {
      extendedEntityContainer = edm.getEntityContainer(entityContainer.getExtendz());
      if (extendedEntityContainer == null) {
        throw new EdmException();
      }
    }
  }

  @Override
  public String getName() throws EdmException {
    return entityContainer.getName();
  }

  @Override
  public EdmEntitySet getEntitySet(String name) throws EdmException {
    if (entitySets.containsValue(name)) {
      return entitySets.get(name);
    }

    EdmEntitySet edmEntitySet = null;

    EntitySet entitySet;
    try {
      entitySet = edm.edmProvider.getEntitySet(entityContainer.getName(), name);
    } catch (ODataException e) {
      throw new EdmException(e);
    }

    if (entitySet != null) {
      edmEntitySet = createEntitySet(entitySet);
      entitySets.put(name, edmEntitySet);
    } else if (extendedEntityContainer != null) {
      edmEntitySet = extendedEntityContainer.getEntitySet(name);
      entitySets.put(name, edmEntitySet);
    }

    return edmEntitySet;
  }

  @Override
  public EdmFunctionImport getFunctionImport(String name) throws EdmException {
    if (functionImports.containsValue(name)) {
      return functionImports.get(name);
    }

    EdmFunctionImport edmFunctionImport = null;

    FunctionImport functionImport;
    try {
      functionImport = edm.edmProvider.getFunctionImport(entityContainer.getName(), name);
    } catch (ODataException e) {
      throw new EdmException(e);
    }

    if (functionImport != null) {
      edmFunctionImport = createFunctionImport(functionImport);
      functionImports.put(name, edmFunctionImport);
    } else if (extendedEntityContainer != null) {
      edmFunctionImport = extendedEntityContainer.getFunctionImport(name);
      functionImports.put(name, edmFunctionImport);
    }

    return edmFunctionImport;
  }

  @Override
  public EdmAssociationSet getAssociationSet(EdmEntitySet sourceEntitySet, EdmNavigationProperty navigationProperty) throws EdmException {
    EdmAssociation edmAssociation = navigationProperty.getRelationship();
    String association = edmAssociation.getNamespace() + "." + edmAssociation.getName();
    String entitySetName = sourceEntitySet.getName();
    String entitySetFromRole = navigationProperty.getFromRole();

    String key = entitySetName + ">>" + association + ">>" + entitySetFromRole;

    if (associationSets.containsKey(key)) {
      return associationSets.get(key);
    }

    EdmAssociationSet edmAssociationSet = null;

    AssociationSet associationSet;
    FullQualifiedName associationFQName = new FullQualifiedName(edmAssociation.getName(), edmAssociation.getNamespace());
    try {
      associationSet = edm.edmProvider.getAssociationSet(entityContainer.getName(), associationFQName, entitySetName, entitySetFromRole);
    } catch (ODataException e) {
      throw new EdmException(e);
    }

    if (associationSet != null) {
      edmAssociationSet = createAssociationSet(associationSet);
      associationSets.put(key, edmAssociationSet);
    } else if (extendedEntityContainer != null) {
      edmAssociationSet = extendedEntityContainer.getAssociationSet(sourceEntitySet, navigationProperty);
      associationSets.put(key, edmAssociationSet);
    }

    return edmAssociationSet;
  }

  private EdmEntitySet createEntitySet(EntitySet entitySet) {
    return new EdmEntitySetImplProv(edm, entitySet, this);
  }

  private EdmFunctionImport createFunctionImport(FunctionImport functionImport) {
    return new EdmFunctionImportImplProv(edm, functionImport, this);
  }

  private EdmAssociationSet createAssociationSet(AssociationSet associationSet) {
    return new EdmAssociationSetImplProv(edm, associationSet, this);
  }
}