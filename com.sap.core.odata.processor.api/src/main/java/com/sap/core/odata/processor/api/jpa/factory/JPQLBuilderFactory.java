package com.sap.core.odata.processor.api.jpa.factory;

import com.sap.core.odata.processor.api.jpa.access.JPAMethodContext.JPAMethodContextBuilder;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextView;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement.JPQLStatementBuilder;

/**
 * Factory interface for creating following instances
 * 
 * <p>
 * <ul>
 * <li>JPQL statement builders of type
 * {@link com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement.JPQLStatementBuilder}
 * </li>
 * <li>JPQL context builder of type
 * {@link com.sap.core.odata.processor.api.jpa.jpql.JPQLContext.JPQLContextBuilder}
 * </li>
 * </ul>
 * </p>
 * 
 * @author SAP AG
 * @see com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory
 */
public interface JPQLBuilderFactory {
	/**
	 * The method returns JPQL statement builder for building JPQL statements.
	 * 
	 * @param context
	 *            is
	 *            {@link com.sap.core.odata.processor.api.jpa.jpql.JPQLContext}
	 *            that determines the type of JPQL statement builder. The
	 *            parameter cannot be null.
	 * @return an instance of JPQLStatementBuilder
	 */
	public JPQLStatementBuilder getStatementBuilder(JPQLContextView context);

	/**
	 * The method returns a JPQL context builder for building JPQL Context
	 * object.
	 * 
	 * @param contextType
	 *            is
	 *            {@link com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType}
	 *            that determines the type of JPQL context builder. The
	 *            parameter cannot be null.
	 * @return an instance of JPQLContextBuilder
	 */
	public JPQLContextBuilder getContextBuilder(JPQLContextType contextType);

	/**
	 * The method returns a JPA method context builder for building JPA Method
	 * context object.
	 * 
	 * @param contextType
	 *            is
	 *            {@link com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType}
	 *            that determines the type of JPQL context builder. The
	 *            parameter cannot be null.
	 * @return an instance of JPAMethodContextBuilder
	 */
	public JPAMethodContextBuilder getJPAMethodContextBuilder(
			JPQLContextType contextType);
}
