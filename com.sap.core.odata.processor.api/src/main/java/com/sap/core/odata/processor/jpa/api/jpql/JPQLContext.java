package com.sap.core.odata.processor.jpa.api.jpql;

import com.sap.core.odata.processor.jpa.api.factory.ODataJPAFactory;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public abstract class JPQLContext {
	
	protected String jpaEntityName;
	protected JPQLContextType type;
	
	protected final void setJPAEntityName(String jpaEntityName){
		this.jpaEntityName = jpaEntityName;
	}
	
	protected final void setType(JPQLContextType type){
		this.type = type;
	}
	public final String getJPAEntityName(){
		return this.jpaEntityName;
	}
	
	public final JPQLContextType getType(){
		return this.type;
	}
	
	public final static JPQLContextBuilder createBuilder(JPQLContextType contextType, Object resultsView){
		return JPQLContextBuilder.create(contextType,resultsView);
	}
	
	public static abstract class JPQLContextBuilder {
		
		protected JPQLContextBuilder( ){ }
		
		private static JPQLContextBuilder create(JPQLContextType contextType,Object resultsView){
			JPQLContextBuilder contextBuilder = ODataJPAFactory.createFactory().getJPQLBuilderFactory().getContextBuilder(contextType);
			contextBuilder.setResultsView(resultsView);
			return contextBuilder;
		}
		
		public abstract JPQLContext build( ) throws ODataJPAModelException, ODataJPARuntimeException;
		protected abstract void setResultsView(Object resultsView);
	}
}
