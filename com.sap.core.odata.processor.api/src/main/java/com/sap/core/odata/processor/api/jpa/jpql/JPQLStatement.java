package com.sap.core.odata.processor.api.jpa.jpql;

import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.factory.JPQLBuilderFactory;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;

/**
 * The class represents a JPQL Statement. The JPQL statement is built using a
 * builder namely {@link JPQLStatementBuilder}. Based upon the JPQL Context
 * types ({@link JPQLContextType} different kinds of JPQL statements are built.
 * 
 * @author SAP AG
 * @see {@link JPQLBuilderFactory}, {@link JPQLContextView}
 */
public class JPQLStatement {

	protected String statement;

	/**
	 * The method is used for creating an instance of JPQL Statement Builder for
	 * building JPQL statements. The JPQL Statement builder is created based
	 * upon the JPQL Context.
	 * 
	 * @param context
	 *            a non null value of {@link JPQLContextView}. The context is
	 *            expected to be set to be built with no errors.
	 * @return
	 * @throws ODataJPARuntimeException
	 */
	public static JPQLStatementBuilder createBuilder(JPQLContextView context)
			throws ODataJPARuntimeException {
		return JPQLStatementBuilder.create(context);
	}

	private JPQLStatement(String statement) {
		this.statement = statement;
	}

	/**
	 * The method provides a String representation of JPQLStatement.
	 */
	@Override
	public String toString() {
		return statement;
	}

	/**
	 * The abstract class is extended by specific JPQL statement builders for
	 * building JPQL statements like
	 * <ol>
	 * <li>Select statements</li>
	 * <li>Select single statements</li>
	 * <li>Select statements with Join</li>
	 * <li>Insert/Modify/Delete statements</li>
	 * </ol>
	 * 
	 * A default statement builder for building each kind of JPQL statements is
	 * provided by the library.
	 * 
	 * @author SAP AG
	 * 
	 */
	public static abstract class JPQLStatementBuilder {

		protected JPQLStatementBuilder() {
		}

		private static final JPQLStatementBuilder create(JPQLContextView context)
				throws ODataJPARuntimeException {
			return ODataJPAFactory.createFactory().getJPQLBuilderFactory()
					.getStatementBuilder(context);
		}

		protected final JPQLStatement createStatement(String statement) {
			return new JPQLStatement(statement);
		}

		/**
		 * The abstract method is implemented by specific statement builder for
		 * building JPQL Statement.
		 * 
		 * @return an instance of {@link JPQLStatement}
		 * @throws ODataJPARuntimeException
		 *             in case there are errors building the statements
		 */
		public abstract JPQLStatement build() throws ODataJPARuntimeException;

	}

	public static final class Operator {
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

	public static final class KEYWORD {
		public static final String SELECT = "SELECT";
		public static final String FROM = "FROM";
		public static final String WHERE = "WHERE";
		public static final String LEFT_OUTER_JOIN = "LEFT OUTER JOIN";
		public static final String OUTER = "OUTER";
		public static final String JOIN = "JOIN";
		public static final String ORDERBY = "ORDER BY";

	}

	public static final class DELIMITER {
		public static final char SPACE = ' ';
		public static final char COMMA = ',';
		public static final char PERIOD = '.';
	}

}
