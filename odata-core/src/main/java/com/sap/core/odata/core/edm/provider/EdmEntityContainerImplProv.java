package com.sap.core.odata.core.edm.provider;

import java.util.HashMap;
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

  private EdmImplProv edm;
  private EntityContainer entityContainer;
  private Map<String, EdmEntitySet> edmEntitySets;
  private Map<String, EdmAssociationSet> edmAssociationSets;
  private Map<String, EdmFunctionImport> edmFunctionImports;
  private EdmEntityContainer edmExtendedEntityContainer;

  public EdmEntityContainerImplProv(EdmImplProv edm, EntityContainer entityContainer) throws EdmException {
    this.edm = edm;
    this.entityContainer = entityContainer;
    edmEntitySets = new HashMap<String, EdmEntitySet>();
    edmAssociationSets = new HashMap<String, EdmAssociationSet>();
    edmFunctionImports = new HashMap<String, EdmFunctionImport>();

    if (entityContainer.getExtendz() != null) {
      edmExtendedEntityContainer = edm.getEntityContainer(entityContainer.getExtendz());
      if (edmExtendedEntityContainer == null) {
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
    if (edmEntitySets.containsKey(name))
      return edmEntitySets.get(name);

    EdmEntitySet edmEntitySet = null;

    EntitySet entitySet;
    try {
      entitySet = edm.edmProvider.getEntitySet(entityContainer.getName(), name);
    } catch (ODataException e) {
      throw new EdmException(e);
    }

    if (entitySet != null) {
      edmEntitySet = createEntitySet(entitySet);
      edmEntitySets.put(name, edmEntitySet);
    } else if (edmExtendedEntityContainer != null) {
      edmEntitySet = edmExtendedEntityContainer.getEntitySet(name);
      edmEntitySets.put(name, edmEntitySet);
    }

    return edmEntitySet;
  }

  @Override
  public EdmFunctionImport getFunctionImport(String name) throws EdmException {
    if (edmFunctionImports.containsKey(name))
      return edmFunctionImports.get(name);

    EdmFunctionImport edmFunctionImport = null;

    FunctionImport functionImport;
    try {
      functionImport = edm.edmProvider.getFunctionImport(entityContainer.getName(), name);
    } catch (ODataException e) {
      throw new EdmException(e);
    }

    if (functionImport != null) {
      edmFunctionImport = createFunctionImport(functionImport);
      edmFunctionImports.put(name, edmFunctionImport);
    } else if (edmExtendedEntityContainer != null) {
      edmFunctionImport = edmExtendedEntityContainer.getFunctionImport(name);
      edmFunctionImports.put(name, edmFunctionImport);
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

    if (edmAssociationSets.containsKey(key)) {
      return edmAssociationSets.get(key);
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
      edmAssociationSets.put(key, edmAssociationSet);
    } else if (edmExtendedEntityContainer != null) {
      edmAssociationSet = edmExtendedEntityContainer.getAssociationSet(sourceEntitySet, navigationProperty);
      edmAssociationSets.put(key, edmAssociationSet);
    }

    return edmAssociationSet;
  }

  private EdmEntitySet createEntitySet(EntitySet entitySet) throws EdmException {
    return new EdmEntitySetImplProv(edm, entitySet, this);
  }

  private EdmFunctionImport createFunctionImport(FunctionImport functionImport) throws EdmException {
    return new EdmFunctionImportImplProv(edm, functionImport, this);
  }

  private EdmAssociationSet createAssociationSet(AssociationSet associationSet) throws EdmException {
    return new EdmAssociationSetImplProv(edm, associationSet, this);
  }
}