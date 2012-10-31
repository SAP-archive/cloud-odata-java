package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.exception.ODataException;

public interface ODataProcessorFactory {

  ODataProcessor create() throws ODataException;

  
}