package com.sap.core.odata.processor.jpa.access;

import java.util.HashMap;

import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.processor.jpa.access.api.JPQLContext;
import com.sap.core.odata.processor.jpa.access.api.JPQLContextType;
import com.sap.core.odata.processor.jpa.access.api.JPQLDeleteContext;
import com.sap.core.odata.processor.jpa.access.api.JPQLModifyContext;
import com.sap.core.odata.processor.jpa.access.api.JPQLSelectContext;

public class JPQLContextImpl implements JPQLContext , JPQLSelectContext, JPQLModifyContext, JPQLDeleteContext {
	private String jpaEntityName;
	private String[] selectedFields;
	private HashMap<String, String> orderByCollection;
	private FilterExpression whereCondition;
	private JPQLContextType contextType;
	
	public JPQLContextImpl (JPQLContextType contextType){
		this.contextType = contextType;
	}
	@Override
	public void setJPAEntityName(String jpaEntityName) {
		this.jpaEntityName = jpaEntityName;		
	}

	

	@Override
	public void setOrderByCollection(HashMap<String, String> orderByCollection) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public HashMap<String, String> getOrderByCollection() {
		return orderByCollection;
	}

	

	@Override
	public FilterExpression getWhereExpression() {
		return whereCondition;
	}

	@Override
	public String getJPAEntityName() {
		return jpaEntityName;
	}

	@Override
	public void setSelectedFields(String[] selectedFields) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWhereExpression(FilterExpression filterExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getSelectedFields() {
		return selectedFields;
	}
	
	@Override
	public JPQLContextType getContextType() {
		return contextType;
	}


}
