package com.sap.core.odata.processor.api.jpql;

import java.util.List;

import com.sap.core.odata.processor.api.access.JPAJoinClause;

public interface JPQLJoinContextView extends JPQLSelectContextView{
	public List<JPAJoinClause> getJPAJoinClauses();
}
