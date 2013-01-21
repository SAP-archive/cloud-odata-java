package com.sap.core.odata.processor.jpa.jpql;

import java.util.List;

import com.sap.core.odata.processor.jpa.access.data.ODataExpressionParser;
import com.sap.core.odata.processor.jpa.api.access.JPAOuterJoinClause;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLJoinSelectSingleContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement.JPQLStatementBuilder;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPQLJoinSelectSingleStatementBuilder extends JPQLStatementBuilder{

	JPQLStatement jpqlStatement;
	private JPQLJoinSelectSingleContextView context;

	public JPQLJoinSelectSingleStatementBuilder(JPQLContextView context) {
		this.context = (JPQLJoinSelectSingleContextView) context;
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
		jpqlQuery.append(fromClause).append(JPQLStatement.DELIMITER.SPACE);
		
		if(context.getJPAOuterJoinClauses() != null && context.getJPAOuterJoinClauses().size() > 0){
			int i = 0;
			String entityRelationshipAlias = null;
			joinWhereCondition = new StringBuilder();
			List<JPAOuterJoinClause> joinClauseList = context.getJPAOuterJoinClauses();
			for(JPAOuterJoinClause joinClause : joinClauseList){
				jpqlQuery.append(JPQLStatement.KEYWORD.JOIN).append(JPQLStatement.DELIMITER.SPACE);
				if(entityRelationshipAlias == null){
					entityRelationshipAlias = joinClause.getEntityAlias();
				}
				jpqlQuery.append(entityRelationshipAlias).append(JPQLStatement.DELIMITER.PERIOD).append(joinClause.getEntityRelationShip()).append(JPQLStatement.DELIMITER.SPACE);
				entityRelationshipAlias = joinClause.getEntityRelationShipAlias();
				jpqlQuery.append(joinClause.getEntityRelationShipAlias()).append(JPQLStatement.DELIMITER.SPACE);
				if(joinClause.getJoinCondition() != null){
					if(i > 0){
						joinWhereCondition.append(JPQLStatement.DELIMITER.SPACE + JPQLStatement.Operator.AND + JPQLStatement.DELIMITER.SPACE);
					}
					i++;
					joinWhereCondition.append(joinClause.getJoinCondition());
				}
			}
		}

		
		if (context.getKeyPredicates() != null || joinWhereCondition != null){
			jpqlQuery.append(JPQLStatement.KEYWORD.WHERE).append(JPQLStatement.DELIMITER.SPACE);
			if(context.getKeyPredicates() != null){
				jpqlQuery.append(
						ODataExpressionParser.parseKeyPredicates(
								context.getKeyPredicates(),
								context.getJPAEntityAlias())).append(JPQLStatement.DELIMITER.SPACE);
				if(joinWhereCondition != null){
					jpqlQuery.append(JPQLStatement.Operator.AND + JPQLStatement.DELIMITER.SPACE);
				}
			}
			if(joinWhereCondition != null){
				jpqlQuery.append(joinWhereCondition.toString()).append(JPQLStatement.DELIMITER.SPACE);
			}
			
		}

		return jpqlQuery.toString();

	}

}
