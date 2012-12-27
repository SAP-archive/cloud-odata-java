package com.sap.core.odata.processor.jpa.jpql.api;

import java.util.HashMap;

import com.sap.core.odata.api.uri.expression.FilterExpression;

public abstract class JPQLSelectContext extends JPQLContext {
	
	protected abstract void setSelectedFields(String[] selectedFields);
	protected abstract void setOrderByCollection(HashMap<String, String> orderByCollection);
	protected abstract void setWhereExpression(FilterExpression filterExpression);
	
	public abstract String[] getSelectedFields();
	public abstract HashMap<String, String> getOrderByCollection();
	public abstract FilterExpression getWhereExpression();

}
