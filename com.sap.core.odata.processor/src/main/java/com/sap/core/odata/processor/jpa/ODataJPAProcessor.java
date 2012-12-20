package com.sap.core.odata.processor.jpa;

import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;

public class ODataJPAProcessor extends ODataSingleProcessor {
	
	private ODataContext odataContext = this.getContext();
	private ODataJPAContext odataJPAContext;
	
	
	public ODataJPAContext getOdataJPAContext() {
		return odataJPAContext;
	}
	public void setOdataJPAContext(ODataJPAContext odataJPAContext) {
		this.odataJPAContext = odataJPAContext;
	}
	
	@Override
	  public ODataResponse readEntitySet(GetEntitySetView uriParserResultView, ContentType contentType) throws ODataException {
	    throw new ODataNotImplementedException();
	  }


}
