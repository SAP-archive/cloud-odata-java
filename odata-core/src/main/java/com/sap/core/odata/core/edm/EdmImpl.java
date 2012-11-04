package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmServiceMetadata;

public class EdmImpl implements Edm {

  @Override
  public EdmEntityContainer getEntityContainer(String name) throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmEntityType getEntityType(String namespace, String name) throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmComplexType getComplexType(String namespace, String name) throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmAssociation getAssociation(String namespace, String name) throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmServiceMetadata getServiceMetadata() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmEntityContainer getDefaultEntityContainer() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }
}