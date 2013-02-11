package com.sap.core.odata.processor.api.jpa.jpql;

import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;

/**
 * The abstract class is a compilation of objects required for building
 * {@link com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement}. Extend this
 * class to implement specific implementations of JPQL context types (Select,
 * Insert, Delete, Modify, Join). A JPQL Context is constructed from an OData
 * request. Depending on OData CRUD operation performed on an Entity, a
 * corresponding JPQL context object is built. The JPQL context object thus
 * built can be used for constructing JPQL statements. <br>
 * A default implementation is provided by the library.
 * 
 * @author SAP AG
 * @see com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement
 * @see com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType
 * @see com.sap.core.odata.processor.api.jpa.factory.JPQLBuilderFactory
 * 
 */
public abstract class JPQLContext implements JPQLContextView {

	/**
	 * An alias for Java Persistence Entity
	 */
	protected String jpaEntityAlias;
	/**
	 * Java Persistence Entity name
	 */
	protected String jpaEntityName;
	/**
	 * The type of JPQL context. Based on the type JPQL statements can be built.
	 */
	protected JPQLContextType type;

	/**
	 * sets JPA Entity Name into the context
	 * 
	 * @param jpaEntityName
	 *            is the name of JPA Entity
	 */
	protected final void setJPAEntityName(String jpaEntityName) {
		this.jpaEntityName = jpaEntityName;
	}

	/**
	 * sets JPA Entity alias name into the context
	 * 
	 * @param jpaEntityAlias
	 *            is the JPA entity alias name
	 */
	protected final void setJPAEntityAlias(String jpaEntityAlias) {
		this.jpaEntityAlias = jpaEntityAlias;
	}

	/**
	 * gets the JPA entity alias name set into the context
	 */
	public final String getJPAEntityAlias() {
		return this.jpaEntityAlias;
	}

	/**
	 * sets the JPQL context type into the context
	 * 
	 * @param type
	 *            is JPQLContextType
	 */
	protected final void setType(JPQLContextType type) {
		this.type = type;
	}

	/**
	 * gets the JPA entity name set into the context
	 */
	public final String getJPAEntityName() {
		return this.jpaEntityName;
	}

	/**
	 * gets the JPQL context type set into the context
	 */
	public final JPQLContextType getType() {
		return this.type;
	}

	/**
	 * the method returns an instance of type
	 * {@link com.sap.core.odata.processor.api.jpa.jpql.JPQLContext.JPQLContextBuilder}
	 * based on the JPQLContextType. The context builder can be used for
	 * building different JPQL contexts.
	 * 
	 * @param contextType
	 *            is the JPQLContextType
	 * @param resultsView
	 *            is the OData request view
	 * @return an instance of type
	 *         {@link com.sap.core.odata.processor.api.jpa.jpql.JPQLContext.JPQLContextBuilder}
	 * @throws ODataJPARuntimeException
	 */
	public final static JPQLContextBuilder createBuilder(
			JPQLContextType contextType, Object resultsView)
			throws ODataJPARuntimeException {
		return JPQLContextBuilder.create(contextType, resultsView);
	}

	/**
	 * The abstract class is extended by specific JPQLContext builder for
	 * building JPQLContexts.
	 * 
	 * @author SAP AG
	 * 
	 */
	public static abstract class JPQLContextBuilder {
		/**
		 * alias counter is an integer counter that is incremented by "1" for
		 * every new alias name generation. The value of counter is used in the
		 * generation of JPA entity alias names.
		 */
		protected int aliasCounter = 0;

		protected JPQLContextBuilder() {
		}

		/**
		 * the method instantiates an instance of type JPQLContextBuilder.
		 * 
		 * @param contextType
		 *            indicates the type of JPQLContextBuilder to instantiate.
		 * @param resultsView
		 *            is the OData request view
		 * @return an instance of type
		 *         {@link com.sap.core.odata.processor.api.jpa.jpql.JPQLContext.JPQLContextBuilder}
		 * @throws ODataJPARuntimeException
		 */
		private static JPQLContextBuilder create(JPQLContextType contextType,
				Object resultsView) throws ODataJPARuntimeException {
			JPQLContextBuilder contextBuilder = ODataJPAFactory.createFactory()
					.getJPQLBuilderFactory().getContextBuilder(contextType);
			if (contextBuilder == null) {
				throw ODataJPARuntimeException
						.throwException(
								ODataJPARuntimeException.ERROR_JPQLCTXBLDR_CREATE,
								null);
			}
			contextBuilder.setResultsView(resultsView);
			return contextBuilder;
		}

		/**
		 * The abstract method is implemented by specific JPQL context builders
		 * to build JPQL Contexts. The build method makes use of information set
		 * into the context to built JPQL Context Types.
		 * 
		 * @return an instance of
		 *         {@link com.sap.core.odata.processor.api.jpa.jpql.JPQLContext}
		 * @throws ODataJPAModelException
		 * @throws ODataJPARuntimeException
		 */
		public abstract JPQLContext build() throws ODataJPAModelException,
				ODataJPARuntimeException;

		/**
		 * The abstract method is implemented by specific JPQL context builder.
		 * The method sets the OData request view into the JPQL context.
		 * 
		 * @param resultsView
		 *            is an instance representing OData request.
		 */
		protected abstract void setResultsView(Object resultsView);

		/**
		 * The method resets the alias counter value to "0".
		 */
		protected void resetAliasCounter() {
			aliasCounter = 0;
		}

		/**
		 * The method returns a system generated alias name starting with prefix
		 * "E" and ending with suffix "aliasCounter".
		 * 
		 * @return a String representing JPA entity alias name
		 */
		protected String generateJPAEntityAlias() {
			return new String("E" + ++aliasCounter);
		}
	}
}
