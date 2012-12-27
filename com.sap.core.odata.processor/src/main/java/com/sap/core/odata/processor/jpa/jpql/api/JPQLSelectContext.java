package com.sap.core.odata.processor.jpa.jpql.api;

import java.util.HashMap;

import com.sap.core.odata.api.uri.expression.FilterExpression;

public interface JPQLSelectContext extends JPQLContext {
	
	public void setSelectedFields(String[] selectedFields);
	public void setOrderByCollection(HashMap<String, String> orderByCollection);
	public void setWhereExpression(FilterExpression filterExpression);
	
	public String[] getSelectedFields();
	public HashMap<String, String> getOrderByCollection();
	public FilterExpression getWhereExpression();

}
