package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetMediaResourceView;


public interface EntityMedia {
  ODataResponse readEntityMedia(GetMediaResourceView uriParserResultView) throws ODataError;

  ODataResponse updateEntityMedia() throws ODataError;

  ODataResponse deleteEntityMedia() throws ODataError;
}
