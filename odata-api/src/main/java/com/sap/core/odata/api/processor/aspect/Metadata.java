package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetMetadataView;


public interface Metadata {

  Edm getEdm() throws ODataException;

  ODataResponse readMetadata(GetMetadataView uriParserResultView) throws ODataException;

}
