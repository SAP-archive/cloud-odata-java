package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetLinksCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetLinksView;

public interface EntityLinks {
  ODataResponse readEntityLinks(GetEntitySetLinksView uriParserResultView) throws ODataError;

  ODataResponse countEntityLinks(GetEntitySetLinksCountView uriParserResultView) throws ODataError;

  ODataResponse createEntityLink() throws ODataError;
}
