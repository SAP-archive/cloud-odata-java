package com.sap.core.odata.processor.api.jpa.access;

import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;

public abstract class JPAMethodContext implements JPAMethodContextView {

	protected Object enclosingObject;
	protected JPAFunction jpaFunction;

	@Override
	public Object getEnclosingObject() {
		return this.enclosingObject;
	}

	@Override
	public JPAFunction getJPAFunction() {
		return this.jpaFunction;
	}

	protected void setEnclosingObject(Object enclosingObject) {
		this.enclosingObject = enclosingObject;
	}

	protected void setJpaFunction(JPAFunction jpaFunction) {
		this.jpaFunction = jpaFunction;
	}

	public final static JPAMethodContextBuilder createBuilder(
			JPQLContextType contextType, Object resultsView)
			throws ODataJPARuntimeException {
		return JPAMethodContextBuilder.create(contextType, resultsView);
	}

	public static abstract class JPAMethodContextBuilder {

		public abstract JPAMethodContext build() throws ODataJPAModelException,
				ODataJPARuntimeException;

		protected JPAMethodContextBuilder() {
		}

		private static JPAMethodContextBuilder create(
				JPQLContextType contextType, Object resultsView) throws ODataJPARuntimeException {
			JPAMethodContextBuilder contextBuilder = ODataJPAFactory.createFactory()
					.getJPQLBuilderFactory().getJPAMethodContextBuilder(contextType);
			
			if (contextBuilder == null) {
				throw ODataJPARuntimeException
						.throwException(
								ODataJPARuntimeException.ERROR_JPQLCTXBLDR_CREATE,
								null);
			}
			contextBuilder.setResultsView(resultsView);
			return contextBuilder;
		}

		protected abstract void setResultsView(Object resultsView);
	}
}
