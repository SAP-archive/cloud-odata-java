package com.sap.core.odata.processor.api.jpa.access;

/**
 * A container for Java Persistence Join Clause that can be used for building
 * JPQL statements. The container has two main elements <b>
 * <ol>
 * <li>Java Persistence Entity -</li> is the source entity participating in the
 * join. <br>
 * <li>Java Persistence Entity Relationship -</li> is the entity relationship of
 * the source entity participating in the join.
 * </ol>
 * </b>
 * 
 * @author SAP AG
 * 
 */
public class JPAJoinClause {

  /**
   * Enumerated list of possible Joins in JPQL
   * <ol>
   * <li>LEFT - left outer join</li>
   * <li>FETCH - enable fetching of an association as a side effect of the
   * execution of a query</li>
   * <li>INNER - inner join
   * </ol>
   * 
   * @author SAP AG
   * 
   */
  public enum JOIN {
    LEFT, FETCH, INNER
  }

  private String entityName;
  private String entityAlias;
  private String entityRelationShip;
  private String entityRelationShipAlias;
  private JOIN joinType;
  private String joinCondition;

  /**
   * The method returns Java Persistence Entity participating in the join.
   * 
   * @return an entity name
   */
  public String getEntityName() {
    return entityName;
  }

  /**
   * The method returns Java Persistence Entity alias name participating in
   * the join.
   * 
   * @return a entity alias name
   */
  public String getEntityAlias() {
    return entityAlias;
  }

  /**
   * The method returns Java Persistence Entity Relationship name
   * participating in the join.
   * 
   * @return entity alias relationship
   */
  public String getEntityRelationShip() {
    return entityRelationShip;
  }

  /**
   * The method returns Java Persistence Entity Relationship Alias name
   * participating in the join.
   * 
   * @return entity entity relationship alias
   */
  public String getEntityRelationShipAlias() {
    return entityRelationShipAlias;
  }

  /**
   * Constructor for creating elements of JPA Join Clause container.
   * 
   * @param entityName
   *            is the name of the JPA entity participating in the join
   * @param entityAlias
   *            is the alias for the JPA entity participating in the join
   * @param entityRelationShip
   *            is the name of the JPA entity relationship participating in
   *            the join
   * @param entityRelationShipAlias
   *            is the alias name of the JPA entity relationship participating
   *            in the join
   * @param joinCondition
   *            is the condition on which the joins should occur
   * @param joinType
   *            is the type of join
   *            {@link com.sap.core.odata.processor.api.jpa.access.JPAJoinClause.JOIN}
   *            to execute
   */
  public JPAJoinClause(final String entityName, final String entityAlias,
      final String entityRelationShip, final String entityRelationShipAlias,
      final String joinCondition, final JOIN joinType) {

    this.entityName = entityName;
    this.entityAlias = entityAlias;
    this.entityRelationShip = entityRelationShip;
    this.entityRelationShipAlias = entityRelationShipAlias;
    this.joinCondition = joinCondition;
    this.joinType = joinType;
  }

  /**
   * The method returns a join condition that can be used for building JPQL
   * join statements.
   * 
   * @return a join condition
   */
  public String getJoinCondition() {
    return joinCondition;
  }

  /**
   * The method returns the type of
   * {@link com.sap.core.odata.processor.api.jpa.access.JPAJoinClause.JOIN}
   * that can be used for building JPQL join statements.
   * 
   * @return join type
   */
  public JOIN getJoinType() {
    return joinType;
  }

}
