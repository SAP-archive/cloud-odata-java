package com.sap.core.odata.processor.core.jpa.jpql;

import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextView;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLSelectSingleContextView;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement.JPQLStatementBuilder;
import com.sap.core.odata.processor.core.jpa.access.data.ODataExpressionParser;

public class JPQLSelectSingleStatementBuilder extends JPQLStatementBuilder {

	JPQLStatement jpqlStatement;
	private JPQLSelectSingleContextView context;

	public JPQLSelectSingleStatementBuilder(JPQLContextView context) {
		this.context = (JPQLSelectSingleContextView) context;
	}

	@Override
	public JPQLStatement build() throws ODataJPARuntimeException {
		this.jpqlStatement = createStatement(createJPQLQuery());
		return this.jpqlStatement;

	}

	private String createJPQLQuery() throws ODataJPARuntimeException {

		StringBuilder jpqlQuery = new StringBuilder();
		String tableAlias = context.getJPAEntityAlias();
		String fromClause = context.getJPAEntityName()
				+ JPQLStatement.DELIMITER.SPACE + tableAlias;

		jpqlQuery.append(JPQLStatement.KEYWORD.SELECT).append(
				JPQLStatement.DELIMITER.SPACE);
		jpqlQuery.append(context.getSelectExpression()).append(JPQLStatement.DELIMITER.SPACE);
		jpqlQuery.append(JPQLStatement.KEYWORD.FROM).append(JPQLStatement.DELIMITER.SPACE);
		jpqlQuery.append(fromClause);

		if (context.getKeyPredicates() != null
				&& context.getKeyPredicates().size() > 0) {
			jpqlQuery.append(JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(JPQLStatement.KEYWORD.WHERE).append(JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(ODataExpressionParser.parseKeyPredicates(context.getKeyPredicates(),
							context.getJPAEntityAlias()));
		}

		return jpqlQuery.toString();

	}

}
