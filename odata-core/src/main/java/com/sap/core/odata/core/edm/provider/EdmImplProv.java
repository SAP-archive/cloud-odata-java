package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntityType;
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
    EntityContainerInfo enitityContainerInfo = edmProvider.getEntityContainer(name);
    if(enitityContainerInfo == null){
     return null;
    }
    return new EdmEntityContainerImplProv(this, enitityContainerInfo);
  }

  @Override
  protected EdmEntityType createEntityType(FullQualifiedName fqName) throws ODataException {
    EntityType entityType = edmProvider.getEntityType(fqName);
    if(entityType == null){
      return null;
    }
      
    return new EdmEntityTypeImplProv(this,entityType , fqName.getNamespace());
  }

  @Override
  protected EdmComplexType createComplexType(FullQualifiedName fqName) throws ODataException {
    ComplexType complexType = edmProvider.getComplexType(fqName);
    if(complexType == null){
      return null;
    }
    return new EdmComplexTypeImplProv(this, complexType, fqName.getNamespace());
  }

  @Override
  protected EdmAssociation createAssociation(FullQualifiedName fqName) throws ODataException {
    Association association = edmProvider.getAssociation(fqName);
    if(association == null){
      return null;
    }
    return new EdmAssociationImplProv(this,association , fqName.getNamespace());
  }

  public EdmProvider getEdmProvider() {
    return edmProvider;
  } 
}