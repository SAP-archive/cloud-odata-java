package com.sap.core.odata.core.edm;

import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.exception.ODataRuntimeException;

public abstract class EdmImpl implements Edm {

  private Map<String, EdmEntityContainer> entityContainers;
  private Map<FullQualifiedName, EdmEntityType> entityTypes;
  private Map<FullQualifiedName, EdmComplexType> complexTypes;
  private Map<FullQualifiedName, EdmAssociation> associations;

  public EdmImpl() {
    entityContainers = new HashMap<String, EdmEntityContainer>();
    entityTypes = new HashMap<FullQualifiedName, EdmEntityType>();
    complexTypes = new HashMap<FullQualifiedName, EdmComplexType>();
    associations = new HashMap<FullQualifiedName, EdmAssociation>();
  }

  @Override
  public EdmEntityContainer getEntityContainer(String name) throws EdmException {
    if (entityContainers.containsValue(name)) {
      return entityContainers.get(name);
    }

    EdmEntityContainer edmEntityContainer = null;

    try {
      edmEntityContainer = createEntityContainer(name);
      entityContainers.put(name, edmEntityContainer);
    } catch (ODataMessageException e) {
      throw new EdmException(e);
    }

    return edmEntityContainer;
  }

  @Override
  public EdmEntityType getEntityType(String namespace, String name) throws EdmException {
    FullQualifiedName fqName = new FullQualifiedName(name, namespace);
    if (entityTypes.containsValue(fqName)) {
      return entityTypes.get(fqName);
    }

    EdmEntityType edmEntityType = null;

    try {
      edmEntityType = createEntityType(fqName);
      entityTypes.put(fqName, edmEntityType);
    } catch (ODataMessageException e) {
      throw new EdmException(e);
    }

    return edmEntityType;
  }

  @Override
  public EdmComplexType getComplexType(String namespace, String name) throws EdmException {
    FullQualifiedName fqName = new FullQualifiedName(name, namespace);
    if (complexTypes.containsValue(fqName)) {
      return complexTypes.get(fqName);
    }

    EdmComplexType edmComplexType = null;

    try {
      edmComplexType = createComplexType(fqName);
      complexTypes.put(fqName, edmComplexType);
    } catch (ODataMessageException e) {
      throw new EdmException(e);
    }

    return edmComplexType;
  }

  @Override
  public EdmAssociation getAssociation(String namespace, String name) throws EdmException {
    FullQualifiedName fqName = new FullQualifiedName(name, namespace);
    if (associations.containsValue(fqName)) {
      return associations.get(fqName);
    }

    EdmAssociation edmAssociation = null;

    try {
      edmAssociation = createAssociation(fqName);
      associations.put(fqName, edmAssociation);
    } catch (ODataMessageException e) {
      throw new EdmException(e);
    }

    return edmAssociation;
  }

  @Override
  public EdmServiceMetadata getServiceMetadata() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmEntityContainer getDefaultEntityContainer() throws EdmException {
    return getEntityContainer(null);
  }

  protected abstract EdmEntityContainer createEntityContainer(String name) throws ODataRuntimeException, ODataMessageException, EdmException;

  protected abstract EdmEntityType createEntityType(FullQualifiedName fqName) throws ODataRuntimeException, ODataMessageException, EdmException;

  protected abstract EdmComplexType createComplexType(FullQualifiedName fqName) throws ODataRuntimeException, ODataMessageException, EdmException;

  protected abstract EdmAssociation createAssociation(FullQualifiedName fqName) throws ODataRuntimeException, ODataMessageException, EdmException;
}