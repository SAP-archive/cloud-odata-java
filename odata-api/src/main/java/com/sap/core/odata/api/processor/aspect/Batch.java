package com.sap.core.odata.api.processor.aspect;


import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;

public interface Batch {
  ODataResponse executeBatch() throws ODataException;
}
