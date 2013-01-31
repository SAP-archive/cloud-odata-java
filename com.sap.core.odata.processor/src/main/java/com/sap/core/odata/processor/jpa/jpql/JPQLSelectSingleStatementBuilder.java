package com.sap.core.odata.processor.jpa.jpql;

import com.sap.core.odata.processor.jpa.access.data.ODataExpressionParser;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLSelectSingleContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement.JPQLStatementBuilder;

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
		jpqlQuery.append(tableAlias).append(JPQLStatement.DELIMITER.SPACE);
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
