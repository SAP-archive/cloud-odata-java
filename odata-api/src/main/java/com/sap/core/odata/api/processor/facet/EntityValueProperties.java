package com.sap.core.odata.api.processor.facet;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;

public interface EntityValueProperties {
  ODataResponse readEntityValueProperties() throws ODataError;

  ODataResponse updateEntityValueProperties() throws ODataError;

  ODataResponse deleteEntityValueProperties() throws ODataError;
}
