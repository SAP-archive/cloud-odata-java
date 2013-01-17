package com.sap.core.odata.processor.jpa.jpql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.processor.jpa.access.ODataExpressionParser;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLSelectSingleContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement.JPQLStatementBuilder;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPQLSelectSingleStatementBuilder extends JPQLStatementBuilder{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JPQLSelectSingleStatementBuilder.class);
	
	JPQLStatement jpqlStatement;
	private JPQLSelectSingleContext context;
	
	public JPQLSelectSingleStatementBuilder(JPQLContext context) {
		this.context = (JPQLSelectSingleContext) context;
	}

	/**
	 * Builds the jpql statement for read of an entity
	 * 
	 * @return jpqlStatement
	 * @throws ODataJPARuntimeException
	 * 
	 */
	@Override
	public JPQLStatement build() throws ODataJPARuntimeException {
		this.jpqlStatement = createStatement(createJPQLQuery());
		return this.jpqlStatement;
		
	}

	/**
	 * Creates the jpql statement for read of an entity
	 * 
	 * @return jpqlStatement
	 * @throws ODataJPARuntimeException
	 * 
	 */
	private String createJPQLQuery() throws ODataJPARuntimeException {

		StringBuilder jpqlQuery = new StringBuilder();
		String tableAlias = ODataExpressionParser.TABLE_ALIAS;
		String fromClause = ((JPQLContext) context).getJPAEntityName() + " " + tableAlias;
		String query = "SELECT %s FROM %s";
		
		jpqlQuery.append(String.format(query, tableAlias, fromClause));
		
		if(context.getKeyPredicates() != null  && context.getKeyPredicates().size() > 0){
			jpqlQuery.append(" WHERE ").append(ODataExpressionParser.parseKeyPredicates(context.getKeyPredicates()));
		}
				
		LOGGER.info("JPQL Select Statement formed : "+ jpqlQuery.toString());
		
		return jpqlQuery.toString();
	
	}

	


}

