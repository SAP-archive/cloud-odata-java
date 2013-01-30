package com.sap.core.odata.processor.jpa.jpql;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.processor.jpa.access.data.ODataExpressionParser;
import com.sap.core.odata.processor.jpa.api.access.JPAJoinClause;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLJoinContextView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPQLJoinContext extends JPQLSelectContext implements
		JPQLJoinContextView {

	private List<JPAJoinClause> jpaJoinClauses = null;

	protected void setJPAOuterJoinClause(
			List<JPAJoinClause> jpaOuterJoinClauses) {
		this.jpaJoinClauses = jpaOuterJoinClauses;
	}

	public class JPQLJoinContextBuilder extends JPQLSelectContextBuilder {

		protected int relationShipAliasCounter = 0;

		@Override
		public JPQLContext build() throws ODataJPAModelException,
				ODataJPARuntimeException {
			try {
				JPQLJoinContext.this.setType(JPQLContextType.JOIN);
				
				JPQLJoinContext.this
						.setJPAOuterJoinClause(generateJoinClauses());
				
				if(!jpaJoinClauses.isEmpty()){
					JPAJoinClause joinClause = jpaJoinClauses.get(jpaJoinClauses.size() - 1);
					JPQLJoinContext.this.setJPAEntityName(joinClause.getEntityName());
					JPQLJoinContext.this.setJPAEntityAlias(joinClause.getEntityRelationShipAlias());
				}
				
				JPQLJoinContext.this
						.setOrderByCollection(generateOrderByFileds());

				JPQLJoinContext.this.setSelectedFields(generateSelectFields());


				JPQLJoinContext.this
						.setWhereExpression(generateWhereExpression());
				
			} catch (ODataException e) {
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.GENERAL, e);
			}

			return JPQLJoinContext.this;
		}

		protected List<JPAJoinClause> generateJoinClauses()
				throws ODataJPARuntimeException, EdmException {

			List<JPAJoinClause> jpaOuterJoinClauses = new ArrayList<JPAJoinClause>();
			JPAJoinClause jpaOuterJoinClause = null;

			for (NavigationSegment navigationSegment : entitySetView
					.getNavigationSegments()) {

				EdmNavigationProperty navigationProperty = navigationSegment
						.getNavigationProperty();

				String joinCondition = ODataExpressionParser
						.parseKeyPredicates(
								navigationSegment.getKeyPredicates(),
								getJPAEntityAlias());

				jpaOuterJoinClause = new JPAJoinClause(
						getFromEntityName(navigationProperty),
						generateJPAEntityAlias( ),
						getRelationShipName(navigationProperty),
						generateRelationShipAlias(), joinCondition,
						JPAJoinClause.JOIN.INNER);

				jpaOuterJoinClauses.add(jpaOuterJoinClause);

			}

			return jpaOuterJoinClauses;
		}

		private String getFromEntityName(EdmNavigationProperty navigationProperty)
				throws EdmException {

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
