package com.sap.core.odata.processor.jpa.api.jpql;

import java.util.ArrayList;
import java.util.HashMap;

import com.sap.core.odata.api.uri.expression.FilterExpression;

public interface JPQLSelectContextView {
	public ArrayList<String> getSelectedFields();
	public HashMap<String, String> getOrderByCollection();
	public FilterExpression getWhereExpression();
}
