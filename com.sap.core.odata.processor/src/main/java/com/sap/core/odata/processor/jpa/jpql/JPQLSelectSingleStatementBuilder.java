package com.sap.core.odata.processor.jpa.jpql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.processor.jpa.access.data.ODataExpressionParser;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLSelectSingleContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement.JPQLStatementBuilder;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPQLSelectSingleStatementBuilder extends JPQLStatementBuilder {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JPQLSelectSingleStatementBuilder.class);

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
		jpqlQuery.append(fromClause).append(JPQLStatement.DELIMITER.SPACE);

		if (context.getKeyPredicates() != null
				&& context.getKeyPredicates().size() > 0) {
			jpqlQuery.append(JPQLStatement.KEYWORD.WHERE).append(
					JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(
					ODataExpressionParser.parseKeyPredicates(
							context.getKeyPredicates(),
							context.getJPAEntityAlias())).append(
					JPQLStatement.DELIMITER.SPACE);
		}

		LOGGER.info("JPQL Select Statement formed : " + jpqlQuery.toString());

		return jpqlQuery.toString();

	}

}
