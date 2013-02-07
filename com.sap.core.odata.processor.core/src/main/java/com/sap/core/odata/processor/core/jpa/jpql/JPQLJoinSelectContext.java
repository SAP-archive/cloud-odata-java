package com.sap.core.odata.processor.core.jpa.jpql;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.processor.api.jpa.access.JPAJoinClause;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContext;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLJoinContextView;
import com.sap.core.odata.processor.core.jpa.access.data.ODataExpressionParser;

public class JPQLJoinSelectContext extends JPQLSelectContext implements
		JPQLJoinContextView {

	private List<JPAJoinClause> jpaJoinClauses = null;

	protected void setJPAOuterJoinClause(List<JPAJoinClause> jpaOuterJoinClauses) {
		this.jpaJoinClauses = jpaOuterJoinClauses;
	}

	public class JPQLJoinContextBuilder extends JPQLSelectContextBuilder {

		protected int relationShipAliasCounter = 0;

		@Override
		public JPQLContext build() throws ODataJPAModelException,
				ODataJPARuntimeException {
			try {
				JPQLJoinSelectContext.this.setType(JPQLContextType.JOIN);

				JPQLJoinSelectContext.this
						.setJPAOuterJoinClause(generateJoinClauses());

				if (!jpaJoinClauses.isEmpty()) {
					JPAJoinClause joinClause = jpaJoinClauses
							.get(jpaJoinClauses.size() - 1);
					JPQLJoinSelectContext.this.setJPAEntityName(joinClause
							.getEntityName());
					JPQLJoinSelectContext.this.setJPAEntityAlias(joinClause
							.getEntityRelationShipAlias());
				}

				JPQLJoinSelectContext.this
						.setOrderByCollection(generateOrderByFileds());

				JPQLJoinSelectContext.this
						.setSelectExpression(generateSelectExpression());

				JPQLJoinSelectContext.this
						.setWhereExpression(generateWhereExpression());

			} catch (ODataException e) {
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.GENERAL, e);
			}

			return JPQLJoinSelectContext.this;
		}

		protected List<JPAJoinClause> generateJoinClauses()
				throws ODataJPARuntimeException, EdmException {

			List<JPAJoinClause> jpaOuterJoinClauses = new ArrayList<JPAJoinClause>();
			JPAJoinClause jpaOuterJoinClause = null;

			boolean firstPass = true;
			for (NavigationSegment navigationSegment : entitySetView
					.getNavigationSegments()) {

				EdmNavigationProperty navigationProperty = navigationSegment
						.getNavigationProperty();

				String joinCondition = null;
				String relationShipAlias = generateRelationShipAlias();
				String entityAlias = generateJPAEntityAlias();

				if (!firstPass) {

					joinCondition = ODataExpressionParser.parseKeyPredicates(
							navigationSegment.getKeyPredicates(),
							relationShipAlias);
				} else {

					joinCondition = ODataExpressionParser.parseKeyPredicates(
							entitySetView.getKeyPredicates(),
							entityAlias);
					firstPass = false;
				}

				jpaOuterJoinClause = new JPAJoinClause(
						getFromEntityName(navigationProperty),
						entityAlias,
						getRelationShipName(navigationProperty),
						relationShipAlias, joinCondition,
						JPAJoinClause.JOIN.INNER);

				jpaOuterJoinClauses.add(jpaOuterJoinClause);

			}

			return jpaOuterJoinClauses;
		}

		private String getFromEntityName(
				EdmNavigationProperty navigationProperty) throws EdmException {

			String fromRole = navigationProperty.getFromRole();

			EdmEntityType toEntityType = navigationProperty.getRelationship()
					.getEnd(fromRole).getEntityType();

			EdmMapping mapping = toEntityType.getMapping();

			String entityName = null;
			if (mapping != null)
				entityName = mapping.getInternalName();
			else
				entityName = toEntityType.getName();

			return entityName;

		}

		private String getRelationShipName(
				EdmNavigationProperty navigationProperty) throws EdmException {

			EdmMapping mapping = navigationProperty.getMapping();

			String relationShipName = null;
			if (mapping != null)
				relationShipName = mapping.getInternalName();
			else
				relationShipName = navigationProperty.getName();

			return relationShipName;
		}

		private String generateRelationShipAlias() {
			return new String("R" + ++this.relationShipAliasCounter);
		}
	}

	@Override
	public List<JPAJoinClause> getJPAJoinClauses() {
		return this.jpaJoinClauses;
	}

}
