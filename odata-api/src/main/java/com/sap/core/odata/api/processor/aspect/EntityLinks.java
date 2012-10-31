package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetLinksCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetLinksView;


public interface EntityLinks {
  ODataResponse readEntityLinks(GetEntitySetLinksView uriParserResultView) throws ODataException;

  ODataResponse countEntityLinks(GetEntitySetLinksCountView uriParserResultView) throws ODataException;

  ODataResponse createEntityLink() throws ODataException;
}
