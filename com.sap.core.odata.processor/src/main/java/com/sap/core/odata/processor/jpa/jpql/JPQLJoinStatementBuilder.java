package com.sap.core.odata.processor.jpa.jpql;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.sap.core.odata.processor.jpa.api.access.JPAJoinClause;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLJoinContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement.JPQLStatementBuilder;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;

public class JPQLJoinStatementBuilder extends JPQLStatementBuilder {

	JPQLStatement jpqlStatement;
	private JPQLJoinContextView context;

	public JPQLJoinStatementBuilder(JPQLContextView context) {
		this.context = (JPQLJoinContextView) context;
	}

	@Override
	public JPQLStatement build() throws ODataJPARuntimeException {
		this.jpqlStatement = createStatement(createJPQLQuery());
		return this.jpqlStatement;

	}

	private String createJPQLQuery() throws ODataJPARuntimeException {

		StringBuilder jpqlQuery = new StringBuilder();
		StringBuilder joinWhereCondition = null;
		String tableAlias = context.getJPAEntityAlias();

		jpqlQuery.append(JPQLStatement.KEYWORD.SELECT).append(
				JPQLStatement.DELIMITER.SPACE);
		jpqlQuery.append(tableAlias).append(JPQLStatement.DELIMITER.SPACE);
		jpqlQuery.append(JPQLStatement.KEYWORD.FROM).append(
				JPQLStatement.DELIMITER.SPACE);

		if (context.getJPAJoinClauses() != null
				&& context.getJPAJoinClauses().size() > 0) {
			List<JPAJoinClause> joinClauseList = context.getJPAJoinClauses();
			JPAJoinClause joinClause = joinClauseList.get(0);
			String relationShipAlias = null;

			jpqlQuery.append(joinClause.getEntityName()).append(
					JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(joinClause.getEntityAlias()).append(
					JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(JPQLStatement.KEYWORD.JOIN).append(
					JPQLStatement.DELIMITER.SPACE);

			relationShipAlias = joinClause.getEntityRelationShipAlias();
			jpqlQuery.append(joinClause.getEntityAlias()).append(
					JPQLStatement.DELIMITER.PERIOD);
			jpqlQuery.append(joinClause.getEntityRelationShip()).append(
					JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(joinClause.getEntityRelationShipAlias()).append(
					JPQLStatement.DELIMITER.SPACE);

			joinWhereCondition = new StringBuilder();
			String joinCondition = joinClause.getJoinCondition();
			if (joinCondition != null)
				joinWhereCondition.append(joinCondition).append(
						JPQLStatement.DELIMITER.SPACE);

			int i = 1;
			int limit = joinClauseList.size();
			while (i < limit) {
				jpqlQuery.append(JPQLStatement.KEYWORD.JOIN).append(
						JPQLStatement.DELIMITER.SPACE);

				joinClause = joinClauseList.get(i);
				jpqlQuery.append(relationShipAlias).append(
						JPQLStatement.DELIMITER.PERIOD);
				jpqlQuery.append(joinClause.getEntityRelationShip()).append(
						JPQLStatement.DELIMITER.SPACE);
				jpqlQuery.append(joinClause.getEntityRelationShipAlias()).append(
						JPQLStatement.DELIMITER.SPACE);

				relationShipAlias = joinClause.getEntityRelationShipAlias();
				i++;

				joinCondition = joinClause.getJoinCondition();
				if (joinCondition != null) {
					joinWhereCondition.append(JPQLStatement.DELIMITER.SPACE
							+ JPQLStatement.Operator.AND
							+ JPQLStatement.DELIMITER.SPACE);

					joinWhereCondition.append(joinCondition).append(
							JPQLStatement.DELIMITER.SPACE);
				}

			}
		} else {
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.JOIN_CLAUSE_EXPECTED, null);
		}
		String whereExpression = context.getWhereExpression();
		if ( whereExpression != null || joinWhereCondition.length() > 0) {
			jpqlQuery.append(JPQLStatement.KEYWORD.WHERE).append(
					JPQLStatement.DELIMITER.SPACE);
			if (whereExpression != null) {
				jpqlQuery.append(whereExpression);
				if (joinWhereCondition != null) {
					jpqlQuery.append(JPQLStatement.DELIMITER.SPACE
							+ JPQLStatement.Operator.AND
							+ JPQLStatement.DELIMITER.SPACE);
				}
			}
			if (joinWhereCondition != null) {
				jpqlQuery.append(joinWhereCondition.toString());
			}

		}

		if (context.getOrderByCollection() != null
				&& context.getOrderByCollection().size() > 0) {

			StringBuilder orderByBuilder = new StringBuilder();
			Iterator<Entry<String, String>> orderItr = context
					.getOrderByCollection().entrySet().iterator();

			int i = 0;

			while (orderItr.hasNext()) {
				if (i != 0) {
					orderByBuilder.append(JPQLStatement.DELIMITER.COMMA)
							.append(JPQLStatement.DELIMITER.SPACE);
				}
				Entry<String, String> entry = orderItr.next();
				orderByBuilder.append(tableAlias)
						.append(JPQLStatement.DELIMITER.PERIOD)
						.append(entry.getKey())
						.append(JPQLStatement.DELIMITER.SPACE);
				orderByBuilder.append(entry.getValue()).append(
						JPQLStatement.DELIMITER.SPACE);
				i++;
			}
			jpqlQuery.append(JPQLStatement.KEYWORD.ORDERBY).append(
					JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(orderByBuilder);
		}

		return jpqlQuery.toString();

	}
}
