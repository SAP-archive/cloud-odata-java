package com.sap.core.odata.processor.jpa.api.jpql;

import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.api.factory.ODataJPAFactory;

public class JPQLStatement {

	protected String statement;

	public static JPQLStatementBuilder createBuilder(JPQLContextView context) {
		return JPQLStatementBuilder.create(context);
	}

	private JPQLStatement(String statement) {
		this.statement = statement;
	}

	@Override
	public String toString() {
		return statement;
	}

	public static abstract class JPQLStatementBuilder {

		protected JPQLStatementBuilder() {
		}

		private static final JPQLStatementBuilder create(JPQLContextView context) {
			return ODataJPAFactory.createFactory().getJPQLBuilderFactory()
					.getStatementBuilder(context);
		}

		protected final JPQLStatement createStatement(String statement) {
			return new JPQLStatement(statement);
		}

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
	
	public static final class DELIMITER{
		public static final char SPACE = ' ';
		public static final char COMMA = ',';
		public static final char PERIOD = '.';
	}

}
