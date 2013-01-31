package com.sap.core.odata.processor.jpa.api.jpql;

import java.util.HashMap;

public interface JPQLSelectContextView extends JPQLContextView {
	public String getSelectExpression();
	public HashMap<String, String> getOrderByCollection();
	public String getWhereExpression();
}
