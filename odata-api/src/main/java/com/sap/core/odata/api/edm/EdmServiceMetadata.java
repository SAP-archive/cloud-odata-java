package com.sap.core.odata.api.edm;

import java.io.InputStream;

import com.sap.core.odata.api.exception.ODataException;

//TODO: Check Exception Handling (ABAP throws server error), check return values
public interface EdmServiceMetadata {

  InputStream getMetadata() throws ODataException;

  String getDataServiceVersion() throws ODataException;
}