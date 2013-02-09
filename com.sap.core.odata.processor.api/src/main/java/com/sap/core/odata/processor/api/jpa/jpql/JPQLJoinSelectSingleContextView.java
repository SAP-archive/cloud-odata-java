package com.sap.core.odata.processor.api.jpa.jpql;

import java.util.List;

import com.sap.core.odata.processor.api.jpa.access.JPAJoinClause;

/**
 * The interface provide a view on JPQL Join Clauses.The interface is an
 * extension to JPQL select single context and provides methods for accessing
 * JPQL Join clauses. The view can be used for building JPQL statements without any
 * WHERE,ORDERBY clauses. The clauses are built from OData read entity
 * request views.
 * 
 * @author SAP AG
 * @see JPQLSelectSingleContextView
 * 
 */
public interface JPQLJoinSelectSingleContextView extends
		JPQLSelectSingleContextView {

	/**
	 * The method returns a list of JPA Join Clauses. The returned list of
	 * values can be used for building JPQL Statements with Join clauses.
	 * 
	 * @return a list of {@link JPAJoinClause}
	 */
	public abstract List<JPAJoinClause> getJPAJoinClauses();
}
