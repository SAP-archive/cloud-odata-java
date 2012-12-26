package com.sap.core.odata.processor.jpa.access.api;

import com.sap.core.odata.processor.jpa.access.JPQLSelectStatementBuilder;

public class JPQLStatement {
	
	protected String statement;
	
	public static JPQLStatementBuilder setJPQLContext(JPQLContext context){
		switch (context.getContextType()) {
		case SELECT:
			return new JPQLSelectStatementBuilder(context);
		default:
			break;
		}
		
		return null;
	}
	
	public JPQLStatement(String statement){
		this.statement = statement;
	}
	
	@Override
	public String toString( ){
		return statement;
	}
	
	
}
