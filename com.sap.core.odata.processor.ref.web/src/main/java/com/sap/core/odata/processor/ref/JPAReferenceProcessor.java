package com.sap.core.odata.processor.ref;

import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.ODataJPAProcessor;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

public class JPAReferenceProcessor extends ODataJPAProcessor {

	public JPAReferenceProcessor(ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException {
		super(oDataJPAContext);
	}

	
}
