package com.sap.core.odata.processor.jpa.access;

import com.sap.core.odata.processor.jpa.access.api.JPQLContext;
import com.sap.core.odata.processor.jpa.access.api.JPQLStatement;
import com.sap.core.odata.processor.jpa.access.api.JPQLStatementBuilder;

public class JPQLSelectStatementBuilder extends JPQLStatementBuilder{

	public JPQLSelectStatementBuilder(JPQLContext context) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public JPQLStatement build() {
		return new JPQLStatement("Select * from XYZ");
		
	}


}
