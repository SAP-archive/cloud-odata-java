package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;

public interface EntitySimplePropertyValue {
  ODataResponse readEntitySimplePropertyValue() throws ODataError;

  ODataResponse updateEntitySimplePropertyValue() throws ODataError;

  ODataResponse deleteEntitySimplePropertyValue() throws ODataError;
}
