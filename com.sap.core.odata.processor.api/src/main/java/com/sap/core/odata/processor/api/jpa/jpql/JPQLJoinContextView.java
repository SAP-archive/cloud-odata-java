package com.sap.core.odata.processor.api.jpa.jpql;

import java.util.List;

import com.sap.core.odata.processor.api.jpa.access.JPAJoinClause;

/**
 * The interface provide a view on JPQL Join context.The interface provides
 * methods for accessing the Join Clause which can be part of JPQL Select
 * statement. The interface extends the JPQL Select Context to add JQPL Join
 * clauses to the Select statement. The JPQL Join context view is built from
 * OData read entity set with navigation request.
 * 
 * @author SAP AG
 * @see com.sap.core.odata.processor.api.jpa.jpql.JPQLSelectContextView
 * 
 */
public interface JPQLJoinContextView extends JPQLSelectContextView {
  /**
   * The method returns a list of JPA Join Clauses. The returned list of
   * values can be used for building JPQL Statements with Join clauses.
   * 
   * @return a list of
   *         {@link com.sap.core.odata.processor.api.jpa.access.JPAJoinClause}
   */
  public List<JPAJoinClause> getJPAJoinClauses();
}
