package com.sap.core.odata.processor.api.jpa.jpql;

import java.util.List;

import com.sap.core.odata.processor.api.jpa.access.JPAJoinClause;

public interface JPQLJoinSelectSingleContextView extends JPQLSelectSingleContextView{
	public abstract List<JPAJoinClause> getJPAJoinClauses();
}
