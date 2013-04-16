package com.sap.core.odata.core.edm.provider;

import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmAnnotatable;
import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmAssociationSet;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.exception.ODataException;

/**
 * @author SAP AG
 */
public class EdmEntityContainerImplProv implements EdmEntityContainer, EdmAnnotatable {

  private EdmImplProv edm;
  private EntityContainerInfo entityContainer;
  private Map<String, EdmEntitySet> edmEntitySets;
  private Map<String, EdmAssociationSet> edmAssociationSets;
  private Map<String, EdmFunctionImport> edmFunctionImports;
  private EdmEntityContainer edmExtendedEntityContainer;
  private boolean isDefaultContainer;

  public EdmEntityContainerImplProv(final EdmImplProv edm, final EntityContainerInfo entityContainer) throws EdmException {
    this.edm = edm;
    this.entityContainer = entityContainer;
    edmEntitySets = new HashMap<String, EdmEntitySet>();
    edmAssociationSets = new HashMap<String, EdmAssociationSet>();
    edmFunctionImports = new HashMap<String, EdmFunctionImport>();
    isDefaultContainer = entityContainer.isDefaultEntityContainer();

    if (entityContainer.getExtendz() != null) {
      edmExtendedEntityContainer = edm.getEntityContainer(entityContainer.getExtendz());
      if (edmExtendedEntityContainer == null) {
        throw new EdmException(EdmException.COMMON);
      }
    }
  }

  @Override
  public String getName() throws EdmException {
    return entityContainer.getName();
  }

  @Override
  public EdmEntitySet getEntitySet(final String name) throws EdmException {
    EdmEntitySet edmEntitySet = edmEntitySets.get(name);
    if (edmEntitySet != null) {
      return edmEntitySet;
    }

    EntitySet entitySet;
    try {
      entitySet = edm.edmProvider.getEntitySet(entityContainer.getName(), name);
    } catch (ODataException e) {
      throw new EdmException(EdmException.PROVIDERPROBLEM, e);
    }

    if (entitySet != null) {
      edmEntitySet = createEntitySet(entitySet);
      edmEntitySets.put(name, edmEntitySet);
    } else if (edmExtendedEntityContainer != null) {
      edmEntitySet = edmExtendedEntityContainer.getEntitySet(name);
      if (edmEntitySet != null) {
        edmEntitySets.put(name, edmEntitySet);
      }
    }

    return edmEntitySet;
  }

  @Override
  public EdmFunctionImport getFunctionImport(final String name) throws EdmException {
    EdmFunctionImport edmFunctionImport = edmFunctionImports.get(name);
    if (edmFunctionImport != null) {
      return edmFunctionImport;
    }

    FunctionImport functionImport;
    try {
      functionImport = edm.edmProvider.getFunctionImport(entityContainer.getName(), name);
    } catch (ODataException e) {
      throw new EdmException(EdmException.PROVIDERPROBLEM, e);
    }

    if (functionImport != null) {
      edmFunctionImport = createFunctionImport(functionImport);
      edmFunctionImports.put(name, edmFunctionImport);
    } else if (edmExtendedEntityContainer != null) {
      edmFunctionImport = edmExtendedEntityContainer.getFunctionImport(name);
      if (edmFunctionImport != null) {
        edmFunctionImports.put(name, edmFunctionImport);
      }
    }

    return edmFunctionImport;
  }

  @Override
  public EdmAssociationSet getAssociationSet(final EdmEntitySet sourceEntitySet, final EdmNavigationProperty navigationProperty) throws EdmException {
    EdmAssociation edmAssociation = navigationProperty.getRelationship();
    String association = edmAssociation.getNamespace() + "." + edmAssociation.getName();
    String entitySetName = sourceEntitySet.getName();
    String entitySetFromRole = navigationProperty.getFromRole();

    String key = entitySetName + ">>" + association + ">>" + entitySetFromRole;

    EdmAssociationSet edmAssociationSet = edmAssociationSets.get(key);
    if (edmAssociationSet != null) {
      return edmAssociationSet;
    }

    AssociationSet associationSet;
    FullQualifiedName associationFQName = new FullQualifiedName(edmAssociation.getNamespace(), edmAssociation.getName());
    try {
      associationSet = edm.edmProvider.getAssociationSet(entityContainer.getName(), associationFQName, entitySetName, entitySetFromRole);
    } catch (ODataException e) {
      throw new EdmException(EdmException.PROVIDERPROBLEM, e);
    }

    if (associationSet != null) {
      edmAssociationSet = createAssociationSet(associationSet);
      edmAssociationSets.put(key, edmAssociationSet);
      return edmAssociationSet;
    } else if (edmExtendedEntityContainer != null) {
      edmAssociationSet = edmExtendedEntityContainer.getAssociationSet(sourceEntitySet, navigationProperty);
      edmAssociationSets.put(key, edmAssociationSet);
      return edmAssociationSet;
    } else {
      throw new EdmException(EdmException.COMMON);
    }
  }

  private EdmEntitySet createEntitySet(final EntitySet entitySet) throws EdmException {
    return new EdmEntitySetImplProv(edm, entitySet, this);
  }

  private EdmFunctionImport createFunctionImport(final FunctionImport functionImport) throws EdmException {
    return new EdmFunctionImportImplProv(edm, functionImport, this);
  }

  private EdmAssociationSet createAssociationSet(final AssociationSet associationSet) throws EdmException {
    return new EdmAssociationSetImplProv(edm, associationSet, this);
  }

  @Override
  public boolean isDefaultEntityContainer() {
    return isDefaultContainer;
  }

  @Override
  public EdmAnnotations getAnnotations() throws EdmException {
    return new EdmAnnotationsImplProv(entityContainer.getAnnotationAttributes(), entityContainer.getAnnotationElements());
  }
}