package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.core.edm.EdmImpl;

public class EdmImplProv extends EdmImpl {

  protected EdmProvider edmProvider;

  public EdmImplProv(EdmProvider edmProvider) {
    super(new EdmServiceMetadataImplProv(edmProvider));
    this.edmProvider = edmProvider;
  }

  @Override
  protected EdmEntityContainer createEntityContainer(String name) throws ODataException {
    return new EdmEntityContainerImplProv(this, edmProvider.getEntityContainer(name));
  }

  @Override
  protected EdmEntityType createEntityType(FullQualifiedName fqName) throws ODataException {
    return new EdmEntityTypeImplProv(this, edmProvider.getEntityType(fqName), fqName.getNamespace());
  }

  @Override
  protected EdmComplexType createComplexType(FullQualifiedName fqName) throws ODataException {
    return new EdmComplexTypeImplProv(this, edmProvider.getComplexType(fqName), fqName.getNamespace());
  }

  @Override
  protected EdmAssociation createAssociation(FullQualifiedName fqName) throws ODataException {
    return new EdmAssociationImplProv(this, edmProvider.getAssociation(fqName), fqName.getNamespace());
  }
}