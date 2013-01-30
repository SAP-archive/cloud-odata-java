package com.sap.core.odata.processor.jpa.factory;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.processor.jpa.ODataJPAContextImpl;
import com.sap.core.odata.processor.jpa.ODataJPAProcessorDefault;
import com.sap.core.odata.processor.jpa.access.data.JPAProcessorImpl;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.api.access.JPAProcessor;
import com.sap.core.odata.processor.jpa.api.factory.JPAAccessFactory;
import com.sap.core.odata.processor.jpa.api.factory.JPQLBuilderFactory;
import com.sap.core.odata.processor.jpa.api.factory.ODataJPAAccessFactory;
import com.sap.core.odata.processor.jpa.api.factory.ODataJPAFactory;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement.JPQLStatementBuilder;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmModelView;
import com.sap.core.odata.processor.jpa.edm.ODataJPAEdmProvider;
import com.sap.core.odata.processor.jpa.jpql.JPQLJoinContext;
import com.sap.core.odata.processor.jpa.jpql.JPQLJoinSelectSingleContext;
import com.sap.core.odata.processor.jpa.jpql.JPQLJoinSelectSingleStatementBuilder;
import com.sap.core.odata.processor.jpa.jpql.JPQLJoinStatementBuilder;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectContext;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectSingleContext;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectSingleStatementBuilder;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectStatementBuilder;
import com.sap.core.odata.processor.jpa.model.JPAEdmModel;


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
		public JPQLStatementBuilder getStatementBuilder(JPQLContextView context) {
			JPQLStatementBuilder builder = null;
			switch (context.getType()) {
			case SELECT:
				builder = new JPQLSelectStatementBuilder(context);
				break;
			case SELECT_SINGLE:
				builder = new JPQLSelectSingleStatementBuilder(context);
				break;
			case JOIN:
				builder = new JPQLJoinStatementBuilder(context);
				break;
			case JOIN_SINGLE:
				builder = new JPQLJoinSelectSingleStatementBuilder(context);
			default:
				break;
			}
			
			return builder;
		}

		@Override
		public JPQLContextBuilder getContextBuilder(JPQLContextType contextType) {
JPQLContextBuilder contextBuilder = null;
			
			switch (contextType) {
			case SELECT:
				JPQLSelectContext selectContext = new JPQLSelectContext();
				contextBuilder =  selectContext.new JPQLSelectContextBuilder();
				break;
			case SELECT_SINGLE:
				JPQLSelectSingleContext singleSelectContext = new JPQLSelectSingleContext();
				contextBuilder =  singleSelectContext.new JPQLSelectSingleContextBuilder();
				break;
			case JOIN:
				JPQLJoinContext joinContext = new JPQLJoinContext();
				contextBuilder = joinContext.new JPQLJoinContextBuilder();
				break;
			case JOIN_SINGLE:
				JPQLJoinSelectSingleContext joinSingleContext = new JPQLJoinSelectSingleContext();
				contextBuilder = joinSingleContext.new JPQLJoinSelectSingleContextBuilder();
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
			return new ODataJPAProcessorDefault(oDataJPAContext);
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
		public JPAEdmModelView getJPAEdmModelView(ODataJPAContext oDataJPAContext) {
			JPAEdmModelView view = null;

			view = new JPAEdmModel(oDataJPAContext.getEntityManagerFactory().getMetamodel(),oDataJPAContext.getPersistenceUnitName());
			return view;
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
