package com.sap.core.odata.processor.api.jpa.jpql;

import java.util.List;

import com.sap.core.odata.api.uri.KeyPredicate;

/**
 * The interface provide a view on JPQL select single context.The interface
 * provides methods for accessing the clause of a JPQL SELECT statement like
 * "SELECT". The view can be used for building JPQL statements without any
 * WHERE,JOIN,ORDERBY clauses. The clauses are built from OData read entity
 * request views.
 * 
 * @author SAP AG
 * @see com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement
 * 
 */
public interface JPQLSelectSingleContextView extends JPQLContextView {
  /**
   * The method returns a JPQL SELECT clause. The SELECT clause is built from
   * $select OData system Query option.
   * 
   * @return a String representing a SELECT clause in JPQL
   */
  public String getSelectExpression();

  /**
   * The method returns the list of key predicates that can be used for
   * constructing the WHERE clause in JPQL statements. The OData entity key
   * predicates are thus converted into JPA entity keys.
   * 
   * @return a list of key predicates
   */
  public List<KeyPredicate> getKeyPredicates();
}
