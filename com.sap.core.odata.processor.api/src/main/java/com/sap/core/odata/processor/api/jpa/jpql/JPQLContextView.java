package com.sap.core.odata.processor.api.jpa.jpql;

public interface JPQLContextView {
	public String getJPAEntityName();
	public String getJPAEntityAlias( );
	public JPQLContextType getType();
}
