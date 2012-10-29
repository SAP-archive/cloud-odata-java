package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetMetadataView;

public interface Metadata {

  Edm getEdm() throws ODataError;

  ODataResponse readMetadata(GetMetadataView uriParserResultView) throws ODataError;

}
