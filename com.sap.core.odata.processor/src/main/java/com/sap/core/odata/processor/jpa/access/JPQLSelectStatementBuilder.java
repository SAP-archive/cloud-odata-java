package com.sap.core.odata.processor.jpa.access;

import java.util.Iterator;
import java.util.Map.Entry;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.processor.jpa.access.api.JPQLContext;
import com.sap.core.odata.processor.jpa.access.api.JPQLSelectContext;
import com.sap.core.odata.processor.jpa.access.api.JPQLStatement;
import com.sap.core.odata.processor.jpa.access.api.JPQLStatementBuilder;

public class JPQLSelectStatementBuilder extends JPQLStatementBuilder{
	
	JPQLStatement jpqlStatement;
	private JPQLSelectContext context;
	public JPQLSelectStatementBuilder(JPQLContext context, JPQLStatement jpqlStatement) {
		this.jpqlStatement = jpqlStatement;
		this.context = (JPQLSelectContext) context;
	}

	@Override
	public JPQLStatement build() {
		this.jpqlStatement.setStatement(createJPQLQuery());
		return this.jpqlStatement;
		
	}

	private String createJPQLQuery() {

		StringBuilder jpqlQuery = new StringBuilder();
		String tableAlias = "gwt1";
		String fromClause = context.getJPAEntityName() + " " + tableAlias;
		String query = "SELECT %s FROM %s";
		StringBuilder orderByBuilder = new StringBuilder();
		
		jpqlQuery.append(String.format(query, tableAlias, fromClause));
		
		try {
			jpqlQuery.append(" WHERE ").append(ExpressionParsingUtility.parseWhereExpression(context.getWhereExpression()));
		} catch (ODataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
		if(context.getOrderByCollection() != null && context.getOrderByCollection().size()>0){
			Iterator<Entry<String,String>> orderItr = context.getOrderByCollection().entrySet().iterator();
			int i = 0;
			while(orderItr.hasNext()){				
				if(i != 0){
					orderByBuilder.append(", ");
				}
				Entry<String, String> entry = orderItr.next();
				orderByBuilder.append(tableAlias+"."+entry.getKey()).append(entry.getValue());				
				i++;
			}
			jpqlQuery.append(" ORDER BY ").append(orderByBuilder);
		}
		
		return jpqlQuery.toString();
	
	}


}
