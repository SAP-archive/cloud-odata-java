package com.sap.core.odata.processor.core.jpa.jpql;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.processor.core.jpa.access.data.ODataExpressionParser;
import com.sap.core.odata.processor.jpa.api.access.JPAJoinClause;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLJoinSelectSingleContextView;

public class JPQLJoinSelectSingleContext extends JPQLSelectSingleContext
		implements JPQLJoinSelectSingleContextView {

	private List<JPAJoinClause> jpaJoinClauses = null;

	protected void setJPAJoinClause(List<JPAJoinClause> jpaJoinClauses) {
		this.jpaJoinClauses = jpaJoinClauses;
	}

	public class JPQLJoinSelectSingleContextBuilder extends
			JPQLSelectSingleContextBuilder {

		protected int relationShipAliasCounter = 0;

		@Override
		public JPQLContext build() throws ODataJPAModelException,
				ODataJPARuntimeException {
			try {
				JPQLJoinSelectSingleContext.this
						.setType(JPQLContextType.JOIN_SINGLE);
				JPQLJoinSelectSingleContext.this
						.setJPAJoinClause(generateJoinClauses());

				if (!jpaJoinClauses.isEmpty()) {
					JPAJoinClause joinClause = jpaJoinClauses
							.get(jpaJoinClauses.size() - 1);
					JPQLJoinSelectSingleContext.this
							.setJPAEntityName(joinClause.getEntityName());
					JPQLJoinSelectSingleContext.this
							.setJPAEntityAlias(joinClause
									.getEntityRelationShipAlias());
				}

				JPQLJoinSelectSingleContext.this.setKeyPredicates(entityView
						.getKeyPredicates());

				JPQLJoinSelectSingleContext.this
						.setSelectExpression(generateSelectExpression());

			} catch (EdmException e) {
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.GENERAL, e);
			}

			return JPQLJoinSelectSingleContext.this;
		}

		protected List<JPAJoinClause> generateJoinClauses()
				throws ODataJPARuntimeException, EdmException {

			List<JPAJoinClause> jpaJoinClauses = new ArrayList<JPAJoinClause>();
			JPAJoinClause jpaJoinClause = null;

			boolean firstPass = true;
			for (NavigationSegment navigationSegment : entityView
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
							entityView.getKeyPredicates(),
							entityAlias);
					firstPass = false;
				}

				jpaJoinClause = new JPAJoinClause(
						getFromEntityName(navigationProperty),
						entityAlias,
						getRelationShipName(navigationProperty),
						relationShipAlias, joinCondition,
						JPAJoinClause.JOIN.INNER);

				jpaJoinClauses.add(jpaJoinClause);

			}

			return jpaJoinClauses;
		}

		private String getFromEntityName(
				EdmNavigationProperty navigationProperty) throws EdmException {

			String fromRole = navigationProperty.getFromRole();

			EdmEntityType fromEntityType = navigationProperty.getRelationship()
					.getEnd(fromRole).getEntityType();

			EdmMapping mapping = fromEntityType.getMapping();

			String entityName = null;
			if (mapping != null)
				entityName = mapping.getInternalName();
			else
				entityName = fromEntityType.getName();

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
