package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.core.edm.EdmImpl;

public class EdmImplProv extends EdmImpl implements EdmServiceMetadata {

  protected EdmProvider edmProvider;
  double dataServiceVersion = 0;

  public EdmImplProv(EdmProvider edmProvider) {
    super();
    this.edmProvider = edmProvider;
    this.edmServiceMetadata = (EdmServiceMetadata) this;
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

  @Override
  public String getMetadata() throws EdmException {
    Metadata metadata = new Metadata();
    try {
      metadata.setSchemas(edmProvider.getSchemas());
    } catch (ODataException e) {
      throw new EdmException(EdmException.COMMON, e);
    }
    metadata.setDataServiceVersion(this.dataServiceVersion);
    //TODO: Convert Metadata into the right format
    return metadata.toString();

  }

  @Override
  public String getDataServiceVersion() throws EdmException {

    return "" + this.dataServiceVersion;
  }
}