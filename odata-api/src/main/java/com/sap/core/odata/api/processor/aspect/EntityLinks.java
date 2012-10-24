package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;

public interface EntityLinks {
  ODataResponse readEntityLinks() throws ODataError;

  ODataResponse countEntityLinks() throws ODataError;

  ODataResponse createEntityLink() throws ODataError;
}
