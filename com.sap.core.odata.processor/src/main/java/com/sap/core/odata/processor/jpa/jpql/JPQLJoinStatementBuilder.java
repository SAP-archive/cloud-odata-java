package com.sap.core.odata.processor.jpa.jpql;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.sap.core.odata.processor.jpa.api.access.JPAOuterJoinClause;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLJoinContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement.JPQLStatementBuilder;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPQLJoinStatementBuilder extends JPQLStatementBuilder{

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
		String fromClause = context.getJPAEntityName() + JPQLStatement.DELIMITER.SPACE + tableAlias;
		

		jpqlQuery.append(JPQLStatement.KEYWORD.SELECT).append(JPQLStatement.DELIMITER.SPACE);
		jpqlQuery.append(tableAlias).append(JPQLStatement.DELIMITER.SPACE);
		jpqlQuery.append(JPQLStatement.KEYWORD.FROM).append(JPQLStatement.DELIMITER.SPACE);
		jpqlQuery.append(fromClause);
		
		if(context.getJPAOuterJoinClauses() != null && context.getJPAOuterJoinClauses().size() > 0){
			int i = 0;
			String entityRelationshipAlias = null;
			joinWhereCondition = new StringBuilder();
			List<JPAOuterJoinClause> joinClauseList = context.getJPAOuterJoinClauses();
			for(JPAOuterJoinClause joinClause : joinClauseList){
				jpqlQuery.append(JPQLStatement.DELIMITER.SPACE);
				jpqlQuery.append(JPQLStatement.KEYWORD.JOIN).append(JPQLStatement.DELIMITER.SPACE);
				if(entityRelationshipAlias == null){
					entityRelationshipAlias = joinClause.getEntityAlias();
				}
				jpqlQuery.append(entityRelationshipAlias).append(JPQLStatement.DELIMITER.PERIOD).append(joinClause.getEntityRelationShip()).append(JPQLStatement.DELIMITER.SPACE);
				entityRelationshipAlias = joinClause.getEntityRelationShipAlias();
				jpqlQuery.append(joinClause.getEntityRelationShipAlias());
				if(joinClause.getJoinCondition() != null){
					if(i > 0){
						joinWhereCondition.append(JPQLStatement.DELIMITER.SPACE + JPQLStatement.Operator.AND + JPQLStatement.DELIMITER.SPACE);
					}
					i++;
					joinWhereCondition.append(joinClause.getJoinCondition()).append(JPQLStatement.DELIMITER.SPACE);
				}
			}
		}

		if (context.getWhereExpression() != null || joinWhereCondition != null){
			jpqlQuery.append(JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(JPQLStatement.KEYWORD.WHERE).append(JPQLStatement.DELIMITER.SPACE);
			if(context.getWhereExpression() != null){
				jpqlQuery.append(context.getWhereExpression());
				if(joinWhereCondition != null){
					jpqlQuery.append(JPQLStatement.DELIMITER.SPACE + JPQLStatement.Operator.AND + JPQLStatement.DELIMITER.SPACE);
				}
			}
			if(joinWhereCondition != null){
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
					orderByBuilder.append(JPQLStatement.DELIMITER.COMMA).append(JPQLStatement.DELIMITER.SPACE);
				}
				Entry<String, String> entry = orderItr.next();
				orderByBuilder.append(tableAlias).append(JPQLStatement.DELIMITER.PERIOD).append(entry.getKey()).append(JPQLStatement.DELIMITER.SPACE);
				orderByBuilder.append(entry.getValue()).append(JPQLStatement.DELIMITER.SPACE);
				i++;
			}
			jpqlQuery.append(JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(JPQLStatement.KEYWORD.ORDERBY).append(JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(orderByBuilder);
		}

		return jpqlQuery.toString();

	}

}
