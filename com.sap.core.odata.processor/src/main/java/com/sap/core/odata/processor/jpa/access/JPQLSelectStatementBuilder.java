package com.sap.core.odata.processor.jpa.access;

import com.sap.core.odata.processor.jpa.access.api.JPQLContext;
import com.sap.core.odata.processor.jpa.access.api.JPQLStatement;
import com.sap.core.odata.processor.jpa.access.api.JPQLStatementBuilder;

public class JPQLSelectStatementBuilder extends JPQLStatementBuilder{
	
	JPQLStatement jpqlStatement;
	public JPQLSelectStatementBuilder(JPQLContext context, JPQLStatement jpqlStatement) {
		this.jpqlStatement = jpqlStatement;
	}

	@Override
	public JPQLStatement build() {
		return this.jpqlStatement;
		
	}


}
