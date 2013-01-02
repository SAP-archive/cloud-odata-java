package com.sap.core.odata.processor.jpa.jpql.api;

import java.util.ArrayList;
import java.util.HashMap;

import com.sap.core.odata.api.uri.expression.FilterExpression;

public abstract class JPQLSelectContext extends JPQLContext {
	
	protected abstract void setSelectedFields(ArrayList<String> selectedFields);
	protected abstract void setOrderByCollection(HashMap<String, String> orderByCollection);
	protected abstract void setWhereExpression(FilterExpression filterExpression);
	
	public abstract ArrayList<String> getSelectedFields();
	public abstract HashMap<String, String> getOrderByCollection();
	public abstract FilterExpression getWhereExpression();

}
