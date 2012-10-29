package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.exception.ODataError;

public interface ODataProcessorFactory {

  ODataProcessor create() throws ODataError;

  
}