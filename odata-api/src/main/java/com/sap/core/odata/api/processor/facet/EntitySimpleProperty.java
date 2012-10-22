package com.sap.core.odata.api.processor.facet;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;

public interface EntitySimpleProperty {
  ODataResponse readEntitySimpleProperty() throws ODataError;

  ODataResponse updateEntitySimpleProperty() throws ODataError;
}
