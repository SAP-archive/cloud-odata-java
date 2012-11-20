package com.sap.core.odata.api.edm;

//TODO: Check Exception Handling (ABAP throws server error) and return values
public interface EdmServiceMetadata {

  String getMetadata() throws EdmException;

  String getDataServiceVersion() throws EdmException;
}