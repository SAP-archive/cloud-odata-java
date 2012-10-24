package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;

public interface EntityComplexProperty {
  ODataResponse readEntityComplexProperty() throws ODataError;

  ODataResponse updateEntityComplexProperty() throws ODataError;
}
