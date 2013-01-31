package com.sap.core.odata.processor.jpa.api.jpql;

import java.util.List;

import com.sap.core.odata.processor.jpa.api.access.JPAJoinClause;

public interface JPQLJoinSelectSingleContextView extends JPQLSelectSingleContextView{
	public abstract List<JPAJoinClause> getJPAJoinClauses();
}
