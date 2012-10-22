package com.sap.core.odata.api.processor.facet;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;

public interface Entity {
  ODataResponse readEntity() throws ODataError;

  ODataResponse existsEntity() throws ODataError;

  ODataResponse updateEntity() throws ODataError;

  ODataResponse deleteEntity() throws ODataError;
}
