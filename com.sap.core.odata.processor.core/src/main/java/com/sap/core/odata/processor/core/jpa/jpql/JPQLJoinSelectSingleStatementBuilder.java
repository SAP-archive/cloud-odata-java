/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.processor.core.jpa.jpql;

import java.util.List;

import com.sap.core.odata.processor.api.jpa.access.JPAJoinClause;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextView;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLJoinSelectSingleContextView;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement.JPQLStatementBuilder;

public class JPQLJoinSelectSingleStatementBuilder extends JPQLStatementBuilder {

  JPQLStatement jpqlStatement;
  private JPQLJoinSelectSingleContextView context;

  public JPQLJoinSelectSingleStatementBuilder(final JPQLContextView context) {
    this.context = (JPQLJoinSelectSingleContextView) context;
  }

  @Override
  public JPQLStatement build() throws ODataJPARuntimeException {
    jpqlStatement = createStatement(createJPQLQuery());
    return jpqlStatement;

  }

  private String createJPQLQuery() throws ODataJPARuntimeException {

    StringBuilder jpqlQuery = new StringBuilder();
    StringBuilder joinWhereCondition = null;

    jpqlQuery.append(JPQLStatement.KEYWORD.SELECT).append(
        JPQLStatement.DELIMITER.SPACE);
    jpqlQuery.append(context.getSelectExpression()).append(JPQLStatement.DELIMITER.SPACE);
    jpqlQuery.append(JPQLStatement.KEYWORD.FROM).append(
        JPQLStatement.DELIMITER.SPACE);

    if (context.getJPAJoinClauses() != null
        && context.getJPAJoinClauses().size() > 0) {
      List<JPAJoinClause> joinClauseList = context.getJPAJoinClauses();
      JPAJoinClause joinClause = joinClauseList.get(0);
      String joinCondition = joinClause.getJoinCondition();
      joinWhereCondition = new StringBuilder();
      if (joinCondition != null) {
        joinWhereCondition.append(joinCondition);
      }
      String relationShipAlias = null;
      joinClause = joinClauseList.get(1);
      jpqlQuery.append(joinClause.getEntityName()).append(
          JPQLStatement.DELIMITER.SPACE);
      jpqlQuery.append(joinClause.getEntityAlias());

      int i = 1;
      int limit = joinClauseList.size();
      relationShipAlias = joinClause.getEntityAlias();
      while (i < limit) {
        jpqlQuery.append(JPQLStatement.DELIMITER.SPACE);
        jpqlQuery.append(JPQLStatement.KEYWORD.JOIN).append(
            JPQLStatement.DELIMITER.SPACE);

        joinClause = joinClauseList.get(i);
        jpqlQuery.append(relationShipAlias).append(
            JPQLStatement.DELIMITER.PERIOD);
        jpqlQuery.append(joinClause.getEntityRelationShip()).append(
            JPQLStatement.DELIMITER.SPACE);
        jpqlQuery.append(joinClause.getEntityRelationShipAlias());

        relationShipAlias = joinClause.getEntityRelationShipAlias();
        i++;

        joinCondition = joinClause.getJoinCondition();
        if (joinCondition != null) {
          joinWhereCondition.append(JPQLStatement.DELIMITER.SPACE
              + JPQLStatement.Operator.AND
              + JPQLStatement.DELIMITER.SPACE);

          joinWhereCondition.append(joinCondition);
        }

      }
    } else {
      throw ODataJPARuntimeException.throwException(
          ODataJPARuntimeException.JOIN_CLAUSE_EXPECTED, null);
    }

    if (joinWhereCondition.length() > 0) {
      jpqlQuery.append(JPQLStatement.DELIMITER.SPACE);
      jpqlQuery.append(JPQLStatement.KEYWORD.WHERE).append(JPQLStatement.DELIMITER.SPACE);
      jpqlQuery.append(joinWhereCondition.toString());
    }

    return jpqlQuery.toString();

  }

}
