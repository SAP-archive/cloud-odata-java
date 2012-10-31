package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetMediaResourceView;


public interface EntityMedia {
  ODataResponse readEntityMedia(GetMediaResourceView uriParserResultView) throws ODataException;

  ODataResponse updateEntityMedia() throws ODataException;

  ODataResponse deleteEntityMedia() throws ODataException;
}
