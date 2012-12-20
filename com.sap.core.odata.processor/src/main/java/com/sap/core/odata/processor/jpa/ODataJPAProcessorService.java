package com.sap.core.odata.processor.jpa;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.service.ODataSingleProcessorService;
import com.sap.core.odata.processor.jpa.edm.ODataJPAEdmProvider;

public class ODataJPAProcessorService extends ODataSingleProcessorService {
	
	private ODataJPAProcessor processor;
	private ODataJPAEdmProvider edm;
	
	public ODataJPAProcessorService(EdmProvider edmProvider,
			ODataProcessor processor) {
		
		super(edmProvider, (ODataSingleProcessor) processor);
		
		this.processor = (ODataJPAProcessor) processor;
		this.edm = (ODataJPAEdmProvider) edmProvider;
	}

}
