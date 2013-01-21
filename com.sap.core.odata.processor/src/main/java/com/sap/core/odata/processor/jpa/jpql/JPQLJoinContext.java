package com.sap.core.odata.processor.jpa.jpql;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.processor.jpa.access.data.ODataExpressionParser;
import com.sap.core.odata.processor.jpa.api.access.JPAOuterJoinClause;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLJoinContextView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPQLJoinContext extends JPQLSelectContext implements
		JPQLJoinContextView {

	private List<JPAOuterJoinClause> jpaOuterJoinClauses = null;

	protected void setJPAOuterJoinClause(
			List<JPAOuterJoinClause> jpaOuterJoinClauses) {
		this.jpaOuterJoinClauses = jpaOuterJoinClauses;
	}

	public class JPQLJoinContextBuilder extends JPQLSelectContextBuilder {

		protected int relationShipAliasCounter = 0;

		@Override
		public JPQLContext build() throws ODataJPAModelException,
				ODataJPARuntimeException {
			try {
				super.build();
				JPQLJoinContext.this.setJPAOuterJoinClause(generateJoinClauses());
			} catch (EdmException e) {
				throw ODataJPARuntimeException.throwException(ODataJPARuntimeException.GENERAL, e);
			}
			
			return JPQLJoinContext.this;
		}

		protected List<JPAOuterJoinClause> generateJoinClauses()
				throws ODataJPARuntimeException, EdmException {

			List<JPAOuterJoinClause> jpaOuterJoinClauses = new ArrayList<JPAOuterJoinClause>();
			JPAOuterJoinClause jpaOuterJoinClause = null;

			for (NavigationSegment navigationSegment : entitySetView
					.getNavigationSegments()) {

				EdmNavigationProperty navigationProperty = navigationSegment
						.getNavigationProperty();

				String joinCondition = ODataExpressionParser
						.parseKeyPredicates(navigationSegment
								.getKeyPredicates(),getJPAEntityAlias());

				jpaOuterJoinClause = new JPAOuterJoinClause(
						getFromEntityName(navigationProperty),
						getJPAEntityAlias(),
						getRelationShipName(navigationProperty),
						generateRelationShipAlias(), joinCondition,
						JPAOuterJoinClause.JOIN.LEFT);

				jpaOuterJoinClauses.add(jpaOuterJoinClause);

			}
			
			return jpaOuterJoinClauses;
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
	public List<JPAOuterJoinClause> getJPAOuterJoinClauses() {
		return this.jpaOuterJoinClauses;
	}

}
