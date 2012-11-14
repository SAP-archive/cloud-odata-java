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
import com.sap.core.odata.core.exception.ODataRuntimeException;

public abstract class EdmImpl implements Edm {

  private Map<String, EdmEntityContainer> edmEntityContainers;
  private Map<FullQualifiedName, EdmEntityType> edmEntityTypes;
  private Map<FullQualifiedName, EdmComplexType> edmComplexTypes;
  private Map<FullQualifiedName, EdmAssociation> edmAssociations;

  protected EdmServiceMetadata edmServiceMetadata;
  
  public EdmImpl() {
    edmEntityContainers = new HashMap<String, EdmEntityContainer>();
    edmEntityTypes = new HashMap<FullQualifiedName, EdmEntityType>();
    edmComplexTypes = new HashMap<FullQualifiedName, EdmComplexType>();
    edmAssociations = new HashMap<FullQualifiedName, EdmAssociation>();
  }
  
  @Override
  public EdmEntityContainer getEntityContainer(String name) throws EdmException {
    if (edmEntityContainers.containsKey(name))
      return edmEntityContainers.get(name);

    EdmEntityContainer edmEntityContainer = null;

    try {
      edmEntityContainer = createEntityContainer(name);
      edmEntityContainers.put(name, edmEntityContainer);
    } catch (ODataMessageException e) {
      throw new EdmException(EdmException.COMMON, e);
    }

    return edmEntityContainer;
  }

  @Override
  public EdmEntityType getEntityType(String namespace, String name) throws EdmException {
    FullQualifiedName fqName = new FullQualifiedName(name, namespace);
    if (edmEntityTypes.containsKey(fqName))
      return edmEntityTypes.get(fqName);

    EdmEntityType edmEntityType = null;

    try {
      edmEntityType = createEntityType(fqName);
      edmEntityTypes.put(fqName, edmEntityType);
    } catch (ODataMessageException e) {
      throw new EdmException(EdmException.COMMON, e);
    }

    return edmEntityType;
  }

  @Override
  public EdmComplexType getComplexType(String namespace, String name) throws EdmException {
    FullQualifiedName fqName = new FullQualifiedName(name, namespace);
    if (edmComplexTypes.containsKey(fqName))
      return edmComplexTypes.get(fqName);

    EdmComplexType edmComplexType = null;

    try {
      edmComplexType = createComplexType(fqName);
      edmComplexTypes.put(fqName, edmComplexType);
    } catch (ODataMessageException e) {
      throw new EdmException(EdmException.COMMON, e);
    }

    return edmComplexType;
  }

  @Override
  public EdmAssociation getAssociation(String namespace, String name) throws EdmException {
    FullQualifiedName fqName = new FullQualifiedName(name, namespace);
    if (edmAssociations.containsKey(fqName))
      return edmAssociations.get(fqName);

    EdmAssociation edmAssociation = null;

    try {
      edmAssociation = createAssociation(fqName);
      edmAssociations.put(fqName, edmAssociation);
    } catch (ODataMessageException e) {
      throw new EdmException(EdmException.COMMON, e);
    }

    return edmAssociation;
  }

  @Override
  public EdmServiceMetadata getServiceMetadata() {
    return edmServiceMetadata;
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