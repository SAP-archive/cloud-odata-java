package com.sap.core.odata.processor.api.jpql;

import com.sap.core.odata.processor.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.factory.ODataJPAFactory;

public abstract class JPQLContext implements JPQLContextView {
	
	protected String jpaEntityAlias;
	protected String jpaEntityName;
	protected JPQLContextType type;
	
	protected final void setJPAEntityName(String jpaEntityName){
		this.jpaEntityName = jpaEntityName;
	}
	protected final void setJPAEntityAlias(String jpaEntityAlias){
		this.jpaEntityAlias = jpaEntityAlias;
	}
	
	public final String getJPAEntityAlias( ){
		return this.jpaEntityAlias;
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
	
	public final static JPQLContextBuilder createBuilder(JPQLContextType contextType, Object resultsView) throws ODataJPARuntimeException{
		return JPQLContextBuilder.create(contextType,resultsView);
	}
	
	public static abstract class JPQLContextBuilder {
		
		protected int aliasCounter = 0;
		
		protected JPQLContextBuilder( ){ }
		
		private static JPQLContextBuilder create(JPQLContextType contextType,Object resultsView) throws ODataJPARuntimeException{
			JPQLContextBuilder contextBuilder = ODataJPAFactory.createFactory().getJPQLBuilderFactory().getContextBuilder(contextType);
			contextBuilder.setResultsView(resultsView);
			return contextBuilder;
		}
		
		public abstract JPQLContext build( ) throws ODataJPAModelException, ODataJPARuntimeException;
		protected abstract void setResultsView(Object resultsView);
		
		protected void resetAliasCounter( ){
			aliasCounter = 0;
		}
		
		protected String generateJPAEntityAlias( ){
			return new String("E" + ++aliasCounter);
		}
	}
}
