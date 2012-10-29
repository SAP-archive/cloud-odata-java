package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetEntityLinkCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntityLinkView;

public interface EntityLink {
  ODataResponse readEntityLink(GetEntityLinkView uriParserResultView) throws ODataError;

  ODataResponse existsEntityLink(GetEntityLinkCountView uriParserResultView) throws ODataError;

  ODataResponse updateEntityLink() throws ODataError;

  ODataResponse deleteEntityLink() throws ODataError;
}
