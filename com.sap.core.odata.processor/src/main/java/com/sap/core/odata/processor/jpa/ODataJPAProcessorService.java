package com.sap.core.odata.processor.jpa;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.service.ODataSingleProcessorService;

public class ODataJPAProcessorService extends ODataSingleProcessorService {
	
	public ODataJPAProcessorService(EdmProvider edmProvider,
			ODataProcessor processor) {
		
		super(edmProvider, (ODataSingleProcessor) processor);
		
	}

}
