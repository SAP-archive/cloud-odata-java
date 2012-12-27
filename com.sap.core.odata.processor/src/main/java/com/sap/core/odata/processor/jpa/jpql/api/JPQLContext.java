package com.sap.core.odata.processor.jpa.jpql.api;

public interface JPQLContext {
	public void setJPAEntityName(String jpaEntityName);
	public String getJPAEntityName();
	public JPQLContextType getContextType();
	
}
