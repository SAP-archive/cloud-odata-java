package com.sap.core.odata.processor.jpa.jpql;

import java.util.Iterator;
import java.util.Map.Entry;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.processor.jpa.access.ExpressionParsingUtility;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContext;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLSelectContext;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLStatement;

public class JPQLSelectStatementBuilder extends com.sap.core.odata.processor.jpa.jpql.api.JPQLStatement.JPQLStatementBuilder{
	
	JPQLStatement jpqlStatement;
	private JPQLSelectContext context;
	
	public JPQLSelectStatementBuilder(JPQLContext context) {
		this.context = (JPQLSelectContext) context;
	}

	@Override
	public JPQLStatement build() {
		this.jpqlStatement = createStatement(createJPQLQuery());
		return this.jpqlStatement;
		
	}

	private String createJPQLQuery() {

		StringBuilder jpqlQuery = new StringBuilder();
		String tableAlias = "gwt1";
		String fromClause = ((JPQLContext) context).getJPAEntityName() + " " + tableAlias;
		String query = "SELECT %s FROM %s";
		StringBuilder orderByBuilder = new StringBuilder();
		
		jpqlQuery.append(String.format(query, tableAlias, fromClause));
		
		try {
			if(context.getWhereExpression()!=null)
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
