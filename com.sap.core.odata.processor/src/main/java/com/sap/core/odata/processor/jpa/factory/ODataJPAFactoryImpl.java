package com.sap.core.odata.processor.jpa.factory;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.processor.jpa.ODataJPAContextImpl;
import com.sap.core.odata.processor.jpa.ODataJPAProcessor;
import com.sap.core.odata.processor.jpa.access.JPAEdmBuilderV2;
import com.sap.core.odata.processor.jpa.access.JPAProcessorImpl;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.access.JPAProcessor;
import com.sap.core.odata.processor.jpa.api.factory.JPAAccessFactory;
import com.sap.core.odata.processor.jpa.api.factory.JPQLBuilderFactory;
import com.sap.core.odata.processor.jpa.api.factory.ODataJPAAccessFactory;
import com.sap.core.odata.processor.jpa.api.factory.ODataJPAFactory;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement.JPQLStatementBuilder;
import com.sap.core.odata.processor.jpa.edm.ODataJPAEdmProvider;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectContextImpl;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectSingleContextImpl;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectSingleStatementBuilder;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectStatementBuilder;


public class ODataJPAFactoryImpl extends ODataJPAFactory {
	
	public JPQLBuilderFactory getJPQLBuilderFactory() {
		return JPQLBuilderFactoryImpl.create( );
	};

	public JPAAccessFactory getJPAAccessFactory() {
		return JPAAccessFactoryImpl.create( );
	};

	public ODataJPAAccessFactory getODataJPAAccessFactory() {
		return ODataJPAAccessFactoryImpl.create() ;
	};
	
	
	private static class JPQLBuilderFactoryImpl implements JPQLBuilderFactory{
		
		private static JPQLBuilderFactoryImpl factory = null;
		private JPQLBuilderFactoryImpl( ){}
		
		@Override
		public JPQLStatementBuilder getStatementBuilder(JPQLContext context) {
			JPQLStatementBuilder builder = null;
			switch (context.getType()) {
			case SELECT:
				builder = new JPQLSelectStatementBuilder(context);
				return builder;
			case SELECT_SINGLE:
				builder = new JPQLSelectSingleStatementBuilder(context);
				return builder;
			default:
				break;
			}
			
			return null;
		}

		@Override
		public JPQLContextBuilder getContextBuilder(JPQLContextType contextType) {
JPQLContextBuilder contextBuilder = null;
			
			switch (contextType) {
			case SELECT:
				JPQLSelectContextImpl selectContext = new JPQLSelectContextImpl();
				contextBuilder =  selectContext.new JPQLSelectContextBuilder();
				break;
			case SELECT_SINGLE:
				JPQLSelectSingleContextImpl singleSelectContext = new JPQLSelectSingleContextImpl();
				contextBuilder =  singleSelectContext.new JPQLSelectSingleContextBuilder();
				break;
			
			default:
				break;
			}
			
			return contextBuilder;
		}
		
		private static JPQLBuilderFactory create( ){
			if (factory == null)
				return new JPQLBuilderFactoryImpl();
			else
				return factory;
		}
		
	}
	
	private static class ODataJPAAccessFactoryImpl implements ODataJPAAccessFactory {
		
		private static ODataJPAAccessFactoryImpl factory = null;
		private ODataJPAAccessFactoryImpl( ){}
		
		@Override
		public ODataSingleProcessor createODataProcessor(
				ODataJPAContext oDataJPAContext) {
			return new ODataJPAProcessor(oDataJPAContext);
		}

		@Override
		public EdmProvider createJPAEdmProvider(ODataJPAContext oDataJPAContext) {
			return new ODataJPAEdmProvider(oDataJPAContext);
		}

		@Override
		public ODataJPAContext createODataJPAContext() {
			return new ODataJPAContextImpl();
		}
		
		private static ODataJPAAccessFactoryImpl create( ){
			if (factory == null)
				return new ODataJPAAccessFactoryImpl();
			else
				return factory;
		}
		
	}
	
	private static class JPAAccessFactoryImpl implements JPAAccessFactory{
		
		private static JPAAccessFactoryImpl factory = null;
		private JPAAccessFactoryImpl( ){}
		
		@Override
		public JPAEdmBuilder getJPAEdmBuilder(ODataJPAContext oDataJPAContext) {
			JPAEdmBuilder builder = null;

			builder = new JPAEdmBuilderV2(oDataJPAContext.getPersistenceUnitName(),oDataJPAContext.getEntityManagerFactory());
			return builder;

		}
		
		@Override
		public JPAProcessor getJPAProcessor(ODataJPAContext oDataJPAContext) {
			JPAProcessor jpaProcessor = new JPAProcessorImpl(oDataJPAContext);
			
			return jpaProcessor;
		}
		
		private static JPAAccessFactoryImpl create( ){
			if (factory == null)
				return new JPAAccessFactoryImpl();
			else
				return factory;
		}
		
	}
}
