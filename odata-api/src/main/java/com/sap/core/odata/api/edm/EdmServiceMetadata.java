package com.sap.core.odata.api.edm;

import java.io.InputStream;

//TODO: Check Exception Handling (ABAP throws server error), check return values
public interface EdmServiceMetadata {

  InputStream getMetadata() throws EdmException;

  String getDataServiceVersion() throws EdmException;
}