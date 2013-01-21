package com.sap.core.odata.processor.jpa.api.access;

public class JPAOuterJoinClause {

	public enum JOIN {
		LEFT, RIGHT
	}

	private String entityName;
	private String entityAlias;
	private String entityRelationShip;
	private String entityRelationShipAlias;
	private JOIN joinType;
	private String joinCondition;

	public String getEntityName() {
		return entityName;
	}

	public String getEntityAlias() {
		return entityAlias;
	}

	public String getEntityRelationShip() {
		return entityRelationShip;
	}

	public String getEntityRelationShipAlias() {
		return entityRelationShipAlias;
	}

	public JPAOuterJoinClause(String entityName, String entityAlias,
			String entityRelationShip, String entityRelationShipAlias,
			String joinCondition, JOIN joinType) {

		this.entityName = entityName;
		this.entityAlias = entityAlias;
		this.entityRelationShip = entityRelationShip;
		this.entityRelationShipAlias = entityRelationShipAlias;
		this.joinCondition = joinCondition;
		this.joinType = joinType;
	}

	public String getJoinCondition() {
		return joinCondition;
	}

	public JOIN getJoinType() {
		return joinType;
	}

}
