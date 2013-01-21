package com.sap.core.odata.processor.jpa.api.jpql;

import java.util.ArrayList;
import java.util.HashMap;

public interface JPQLSelectContextView extends JPQLContextView {
	public ArrayList<String> getSelectedFields();
	public HashMap<String, String> getOrderByCollection();
	public String getWhereExpression();
}
