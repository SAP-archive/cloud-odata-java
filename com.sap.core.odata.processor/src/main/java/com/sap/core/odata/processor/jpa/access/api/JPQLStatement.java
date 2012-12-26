package com.sap.core.odata.processor.jpa.access.api;

import com.sap.core.odata.processor.jpa.access.JPQLSelectStatementBuilder;

public class JPQLStatement {
	
	protected String statement;
	
	public static JPQLStatementBuilder setJPQLContext(JPQLContext context){
		switch (context.getContextType()) {
		case SELECT:
			return new JPQLSelectStatementBuilder(context, new JPQLStatement());
		default:
			break;
		}
		
		return null;
	}
	
	private JPQLStatement(){
		this.statement = null;
	}
	
	@Override
	public String toString( ){
		return statement;
	}
	
	public void setStatement(String statement)
	{
		this.statement = statement;
	}
	
}
