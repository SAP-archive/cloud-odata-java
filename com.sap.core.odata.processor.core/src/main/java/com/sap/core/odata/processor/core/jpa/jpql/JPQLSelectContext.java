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

import java.util.HashMap;

import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContext;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLSelectContextView;
import com.sap.core.odata.processor.core.jpa.access.data.ODataExpressionParser;

public class JPQLSelectContext extends JPQLContext implements
    JPQLSelectContextView {

  protected String selectExpression;
  protected HashMap<String, String> orderByCollection;
  protected String whereCondition;

  protected boolean isCountOnly = false;//Support for $count

  public JPQLSelectContext(final boolean isCountOnly) {
    this.isCountOnly = isCountOnly;
  }

  protected final void setOrderByCollection(
      final HashMap<String, String> orderByCollection) {
    this.orderByCollection = orderByCollection;
  }

  protected final void setWhereExpression(final String filterExpression) {
    whereCondition = filterExpression;
  }

  protected final void setSelectExpression(final String selectExpression) {
    this.selectExpression = selectExpression;
  }

  @Override
  public String getSelectExpression() {
    return selectExpression;
  }

  @Override
  public HashMap<String, String> getOrderByCollection() {
    return orderByCollection;
  }

  @Override
  public String getWhereExpression() {
    return whereCondition;
  }

  public class JPQLSelectContextBuilder
      extends
      com.sap.core.odata.processor.api.jpa.jpql.JPQLContext.JPQLContextBuilder {

    protected GetEntitySetUriInfo entitySetView;

    @Override
    public JPQLContext build() throws ODataJPAModelException,
        ODataJPARuntimeException {
      if (entitySetView != null) {

        try {

          if (isCountOnly) {
            setType(JPQLContextType.SELECT_COUNT);
          } else {
            setType(JPQLContextType.SELECT);
          }
          EdmEntityType entityType = entitySetView
              .getTargetEntitySet().getEntityType();
          EdmMapping mapping = entityType.getMapping();
          if (mapping != null) {
            setJPAEntityName(mapping.getInternalName());
          } else {
            setJPAEntityName(entityType.getName());
          }

          setJPAEntityAlias(generateJPAEntityAlias());

          setOrderByCollection(generateOrderByFileds());

          setSelectExpression(generateSelectExpression());

          setWhereExpression(generateWhereExpression());
        } catch (ODataException e) {
          throw ODataJPARuntimeException.throwException(ODataJPARuntimeException.INNER_EXCEPTION, e);
        }
      }

      return JPQLSelectContext.this;

    }

    @Override
    protected void setResultsView(final Object resultsView) {
      if (resultsView instanceof GetEntitySetUriInfo) {
        entitySetView = (GetEntitySetUriInfo) resultsView;
      }

    }

    /*
     * Generate Select Clause 
     */
    protected String generateSelectExpression() throws EdmException {
      return getJPAEntityAlias();
    }

    /*
     * Generate Order By Clause Fields
     */
    protected HashMap<String, String> generateOrderByFileds()
        throws ODataJPARuntimeException, EdmException {

      if (entitySetView.getOrderBy() != null) {

        return ODataExpressionParser
            .parseToJPAOrderByExpression(entitySetView.getOrderBy(), getJPAEntityAlias());

      } else if (entitySetView.getTop() != null
          || entitySetView.getSkip() != null) {

        return ODataExpressionParser
            .parseKeyPropertiesToJPAOrderByExpression(entitySetView.getTargetEntitySet()
                .getEntityType().getKeyProperties(), getJPAEntityAlias());
      } else {
        return null;
      }

    }

    /*
     * Generate Where Clause Expression
     */
    protected String generateWhereExpression() throws ODataException {
      if (entitySetView.getFilter() != null) {
        return ODataExpressionParser
            .parseToJPAWhereExpression(entitySetView.getFilter(), getJPAEntityAlias());
      }
      return null;
    }
  }

}
