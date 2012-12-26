package com.sap.core.odata.processor.jpa.access;

import com.sap.core.odata.processor.jpa.access.api.JPQLContext;
import com.sap.core.odata.processor.jpa.access.api.JPQLDeleteContext;
import com.sap.core.odata.processor.jpa.access.api.JPQLModifyContext;
import com.sap.core.odata.processor.jpa.access.api.JPQLSelectContext;

public class JPQLContextImpl implements JPQLContext , JPQLSelectContext, JPQLModifyContext, JPQLDeleteContext {

	@Override
	public void setJPAEntityName(String jpaEntityName) {
		// TODO Auto-generated method stub
		
	}

}
