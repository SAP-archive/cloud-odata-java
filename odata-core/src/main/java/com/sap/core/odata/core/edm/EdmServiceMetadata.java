package com.sap.core.odata.core.edm;

public interface EdmServiceMetadata {

  //TODO completely, Exception Handling
  
  byte[] getMetadata();

  String getDataServiceVersion();

  // TODO
  // EdmEntityContainerInfo getEntityContainerInfo;
}
