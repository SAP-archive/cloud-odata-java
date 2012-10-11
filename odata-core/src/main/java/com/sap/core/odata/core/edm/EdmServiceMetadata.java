package com.sap.core.odata.core.edm;

public interface EdmServiceMetadata {

  byte[] getMetadata();

  String getDataServiceVersion();

  // TODO
  // EdmEntityContainerInfo getEntityContainerInfo;
}
