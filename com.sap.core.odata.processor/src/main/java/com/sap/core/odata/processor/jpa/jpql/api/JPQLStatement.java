package com.sap.core.odata.processor.jpa.jpql.api;

import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.jpql.JPQLBuilderFactory;

public class JPQLStatement {
	
	protected String statement;
	
	public static JPQLStatementBuilder createBuilder(JPQLContext context){
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
		
		public abstract JPQLStatement build( ) throws ODataJPARuntimeException;
	}
	
	public static class Operator {
		public static final String EQ = "=";
		public static final String NE = "<>";
		public static final String LT = "<";
		public static final String LE = "<=";
		public static final String GT = ">";
		public static final String GE = ">=";
		public static final String AND = "AND";
		public static final String NOT = "NOT";
		public static final String OR = "OR";
		
	}
	
}
