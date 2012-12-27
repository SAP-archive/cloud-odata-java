package com.sap.core.odata.processor.jpa.jpql.api;

import com.sap.core.odata.processor.jpa.jpql.JPQLBuilderFactory;

public class JPQLStatement {
	
	protected String statement;
	
	public static JPQLStatementBuilder setJPQLContext(JPQLContext context){
		return JPQLStatementBuilder.create(context);
	}
	
	private JPQLStatement(String statement){
		this.statement = statement;
	}
	
	@Override
	public String toString( ){
		return statement;
	}
	
	public static abstract class JPQLStatementBuilder{
		
		protected JPQLStatementBuilder( ){ }
		
		private static final JPQLStatementBuilder create(JPQLContext context){
			return JPQLBuilderFactory.getStatementBuilder(context);
		}
		
		protected final JPQLStatement createStatement(String statement)
		{
			return new JPQLStatement(statement);
		}
		
		public abstract JPQLStatement build( );
	}
	
}
