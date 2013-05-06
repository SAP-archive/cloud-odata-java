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

import java.util.Iterator;
import java.util.Map.Entry;

import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextView;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLSelectContextView;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement.JPQLStatementBuilder;

public class JPQLSelectStatementBuilder extends JPQLStatementBuilder {

	JPQLStatement jpqlStatement;
	private JPQLSelectContextView context;

	public JPQLSelectStatementBuilder(JPQLContextView context) {
		this.context = (JPQLSelectContextView) context;
	}

	@Override
	public JPQLStatement build() throws ODataJPARuntimeException {
		this.jpqlStatement = createStatement(createJPQLQuery());
		return this.jpqlStatement;

	}

	private String createJPQLQuery() throws ODataJPARuntimeException {

		StringBuilder jpqlQuery = new StringBuilder();
		String tableAlias = context.getJPAEntityAlias();
		String fromClause = context.getJPAEntityName() + JPQLStatement.DELIMITER.SPACE + tableAlias;
		
		jpqlQuery.append(JPQLStatement.KEYWORD.SELECT).append(JPQLStatement.DELIMITER.SPACE);
		if(this.context.getType().equals(JPQLContextType.SELECT_COUNT)){ //$COUNT
			jpqlQuery.append(JPQLStatement.KEYWORD.COUNT).append(JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(JPQLStatement.DELIMITER.PARENTHESIS_LEFT).append(JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(context.getSelectExpression()).append(JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(JPQLStatement.DELIMITER.PARENTHESIS_RIGHT).append(JPQLStatement.DELIMITER.SPACE);
		}else {//Normal
			jpqlQuery.append(context.getSelectExpression()).append(JPQLStatement.DELIMITER.SPACE);
		}
		
		jpqlQuery.append(JPQLStatement.KEYWORD.FROM).append(JPQLStatement.DELIMITER.SPACE);
		jpqlQuery.append(fromClause);

		if (context.getWhereExpression() != null){
			jpqlQuery.append(JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(JPQLStatement.KEYWORD.WHERE).append(JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(context.getWhereExpression());
		}

		if (context.getOrderByCollection() != null
				&& context.getOrderByCollection().size() > 0) {
			
			StringBuilder orderByBuilder = new StringBuilder();
			Iterator<Entry<String, String>> orderItr = context
					.getOrderByCollection().entrySet().iterator();
			
			int i = 0;
			
			while (orderItr.hasNext()) {
				if (i != 0) {
					orderByBuilder.append(JPQLStatement.DELIMITER.SPACE).append(JPQLStatement.DELIMITER.COMMA).append(JPQLStatement.DELIMITER.SPACE);
				}
				Entry<String, String> entry = orderItr.next();
				orderByBuilder.append(entry.getKey()).append(JPQLStatement.DELIMITER.SPACE);
				orderByBuilder.append(entry.getValue());
				i++;
			}
			jpqlQuery.append(JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(JPQLStatement.KEYWORD.ORDERBY).append(JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(orderByBuilder);
		}

		return jpqlQuery.toString();

	}

}
