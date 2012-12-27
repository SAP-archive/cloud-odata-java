package com.sap.core.odata.processor.jpa.jpql.api;

import com.sap.core.odata.processor.jpa.jpql.JPQLBuilderFactory;

public abstract class JPQLContext {
	
	protected String jpaEntityName;
	protected JPQLContextType type;
	protected Object resultsView;
	
	protected final void setJPAEntityName(String jpaEntityName){
		this.jpaEntityName = jpaEntityName;
	}
	
	public final String getJPAEntityName(){
		return this.jpaEntityName;
	}
	
	public final JPQLContextType getType(){
		return this.type;
	}
	
	public final static JPQLContextBuilder setJPQLContextType(JPQLContextType contextType){
		return JPQLContextBuilder.create(contextType);
	}
	
	public static abstract class JPQLContextBuilder {
		
		protected JPQLContextBuilder( ){ }
		
		private static JPQLContextBuilder create(JPQLContextType contextType){
			return JPQLBuilderFactory.getContextBuilder(contextType);
		}
		
		public abstract JPQLContext build( );
	}
}
