package com.sap.core.odata.processor.ref;

import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.api.ODataJPAProcessor;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;

public class JPAReferenceProcessor extends ODataJPAProcessor {

	public JPAReferenceProcessor(ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException {
		super(oDataJPAContext);
	}

	
}
