package com.sap.core.odata.api.processor.facet;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;

public interface EntitySet {
  ODataResponse readEntitySet() throws ODataError;

  ODataResponse countEntitySet() throws ODataError;

  ODataResponse createEntitySet() throws ODataError;
}
