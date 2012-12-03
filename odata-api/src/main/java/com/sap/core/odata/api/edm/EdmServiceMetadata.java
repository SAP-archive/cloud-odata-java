package com.sap.core.odata.api.edm;

import java.io.InputStream;

import com.sap.core.odata.api.exception.ODataException;

public interface EdmServiceMetadata {

  InputStream getMetadata() throws ODataException;

  String getDataServiceVersion() throws ODataException;
}