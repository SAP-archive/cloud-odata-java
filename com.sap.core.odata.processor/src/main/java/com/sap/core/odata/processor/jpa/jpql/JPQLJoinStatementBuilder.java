package com.sap.core.odata.processor.jpa.jpql;

import java.util.Iterator;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLJoinContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement.JPQLStatementBuilder;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPQLJoinStatementBuilder extends JPQLStatementBuilder{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JPQLSelectStatementBuilder.class);

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
		String tableAlias = context.getJPAEntityAlias();
		String fromClause = context.getJPAEntityName() + JPQLStatement.DELIMITER.SPACE + tableAlias;
		

		jpqlQuery.append(JPQLStatement.KEYWORD.SELECT).append(JPQLStatement.DELIMITER.SPACE);
		jpqlQuery.append(tableAlias).append(JPQLStatement.DELIMITER.SPACE);
		jpqlQuery.append(fromClause).append(JPQLStatement.DELIMITER.SPACE);

		if (context.getWhereExpression() != null){
			jpqlQuery.append(JPQLStatement.KEYWORD.WHERE).append(JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(context.getWhereExpression()).append(JPQLStatement.DELIMITER.SPACE);
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
			
			jpqlQuery.append(JPQLStatement.KEYWORD.ORDERBY).append(JPQLStatement.DELIMITER.SPACE);
			jpqlQuery.append(orderByBuilder);
		}

		LOGGER.info("JPQL Select Statement formed : " + jpqlQuery.toString());

		return jpqlQuery.toString();

	}

}
