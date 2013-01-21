package com.sap.core.odata.processor.jpa.api.jpql;

import java.util.List;

import com.sap.core.odata.processor.jpa.api.access.JPAOuterJoinClause;

public interface JPQLJoinSelectSingleContextView extends JPQLSelectSingleContextView{
	public abstract List<JPAOuterJoinClause> getJPAOuterJoinClauses();
}
