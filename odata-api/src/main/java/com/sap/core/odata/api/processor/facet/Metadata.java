package com.sap.core.odata.api.processor.facet;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;

public interface Metadata {

  Edm getEdm() throws ODataError;

  ODataResponse readMetadata() throws ODataError;

}
