package com.sap.core.odata.processor.jpa.access.api;

import java.util.List;

import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public interface JPAProcessor {
	public List<Object> process(GetEntitySetUriInfo uriParserResultView) throws ODataJPAModelException, ODataJPARuntimeException;
	public Object process(GetEntityUriInfo uriParserResultView) throws ODataJPAModelException, ODataJPARuntimeException;;
}
