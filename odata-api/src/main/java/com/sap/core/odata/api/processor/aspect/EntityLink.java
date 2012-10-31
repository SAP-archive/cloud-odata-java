package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetEntityLinkCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntityLinkView;


public interface EntityLink {
  ODataResponse readEntityLink(GetEntityLinkView uriParserResultView) throws ODataException;

  ODataResponse existsEntityLink(GetEntityLinkCountView uriParserResultView) throws ODataException;

  ODataResponse updateEntityLink() throws ODataException;

  ODataResponse deleteEntityLink() throws ODataException;
}
