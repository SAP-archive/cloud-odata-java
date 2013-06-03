/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.processor.core.jpa.jpql;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.processor.api.jpa.access.JPAJoinClause;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContext;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLJoinSelectSingleContextView;
import com.sap.core.odata.processor.core.jpa.ODataExpressionParser;

public class JPQLJoinSelectSingleContext extends JPQLSelectSingleContext
    implements JPQLJoinSelectSingleContextView {

  private List<JPAJoinClause> jpaJoinClauses = null;

  protected void setJPAJoinClause(final List<JPAJoinClause> jpaJoinClauses) {
    this.jpaJoinClauses = jpaJoinClauses;
  }

  public class JPQLJoinSelectSingleContextBuilder extends
      JPQLSelectSingleContextBuilder {

    protected int relationShipAliasCounter = 0;

    @Override
    public JPQLContext build() throws ODataJPAModelException,
        ODataJPARuntimeException {
      try {
        setType(JPQLContextType.JOIN_SINGLE);
        setJPAJoinClause(generateJoinClauses());

        if (!jpaJoinClauses.isEmpty()) {
          JPAJoinClause joinClause = jpaJoinClauses
              .get(jpaJoinClauses.size() - 1);
          setJPAEntityName(joinClause.getEntityName());
          setJPAEntityAlias(joinClause
              .getEntityRelationShipAlias());
        }

        setKeyPredicates(entityView
            .getKeyPredicates());

        setSelectExpression(generateSelectExpression());

      } catch (EdmException e) {
        throw ODataJPARuntimeException.throwException(
            ODataJPARuntimeException.GENERAL, e);
      }

      return JPQLJoinSelectSingleContext.this;
    }

    protected List<JPAJoinClause> generateJoinClauses()
        throws ODataJPARuntimeException, EdmException {

      List<JPAJoinClause> jpaOuterJoinClauses = new ArrayList<JPAJoinClause>();
      JPAJoinClause jpaOuterJoinClause = null;
      String joinCondition = null;
      String entityAlias = generateJPAEntityAlias();
      joinCondition = ODataExpressionParser.parseKeyPredicates(
          entityView.getKeyPredicates(), entityAlias);

      EdmEntityType entityType = entityView.getStartEntitySet()
          .getEntityType();
      Mapping mapping = (Mapping) entityType.getMapping();
      String entityTypeName = null;
      if (mapping != null) {
        entityTypeName = mapping.getInternalName();
      } else {
        entityTypeName = entityType.getName();
      }

      jpaOuterJoinClause = new JPAJoinClause(entityTypeName, entityAlias,
          null, null, joinCondition, JPAJoinClause.JOIN.INNER);

      jpaOuterJoinClauses.add(jpaOuterJoinClause);

      for (NavigationSegment navigationSegment : entityView
          .getNavigationSegments()) {

        EdmNavigationProperty navigationProperty = navigationSegment
            .getNavigationProperty();

        String relationShipAlias = generateRelationShipAlias();

        joinCondition = ODataExpressionParser
            .parseKeyPredicates(
                navigationSegment.getKeyPredicates(),
                relationShipAlias);

        jpaOuterJoinClause = new JPAJoinClause(
            getFromEntityName(navigationProperty), entityAlias,
            getRelationShipName(navigationProperty),
            relationShipAlias, joinCondition,
            JPAJoinClause.JOIN.INNER);

        jpaOuterJoinClauses.add(jpaOuterJoinClause);

      }

      return jpaOuterJoinClauses;
    }

    private String getFromEntityName(
        final EdmNavigationProperty navigationProperty) throws EdmException {

      String fromRole = navigationProperty.getFromRole();

      EdmEntityType fromEntityType = navigationProperty.getRelationship()
          .getEnd(fromRole).getEntityType();

      EdmMapping mapping = fromEntityType.getMapping();

      String entityName = null;
      if (mapping != null) {
        entityName = mapping.getInternalName();
      } else {
        entityName = fromEntityType.getName();
      }

      return entityName;

    }

    private String getRelationShipName(
        final EdmNavigationProperty navigationProperty) throws EdmException {

      EdmMapping mapping = navigationProperty.getMapping();

      String relationShipName = null;
      if (mapping != null) {
        relationShipName = mapping.getInternalName();
      } else {
        relationShipName = navigationProperty.getName();
      }

      return relationShipName;
    }

    private String generateRelationShipAlias() {
      return new String("R" + ++relationShipAliasCounter);
    }
  }

  @Override
  public List<JPAJoinClause> getJPAJoinClauses() {
    return jpaJoinClauses;
  }

}
