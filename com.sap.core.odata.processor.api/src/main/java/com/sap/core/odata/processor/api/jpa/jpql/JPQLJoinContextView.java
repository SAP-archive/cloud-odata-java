package com.sap.core.odata.processor.api.jpa.jpql;

import java.util.List;

import com.sap.core.odata.processor.api.jpa.access.JPAJoinClause;

public interface JPQLJoinContextView extends JPQLSelectContextView{
	public List<JPAJoinClause> getJPAJoinClauses();
}
