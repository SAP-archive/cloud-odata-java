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

import java.util.List;

import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContext;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLSelectSingleContextView;

public class JPQLSelectSingleContext extends JPQLContext implements JPQLSelectSingleContextView {

  private String selectExpression;
  private List<KeyPredicate> keyPredicates;

  protected void setKeyPredicates(final List<KeyPredicate> keyPredicates) {
    this.keyPredicates = keyPredicates;
  }

  @Override
  public List<KeyPredicate> getKeyPredicates() {
    return keyPredicates;
  }

  protected final void setSelectExpression(final String selectExpression) {
    this.selectExpression = selectExpression;
  }

  @Override
  public String getSelectExpression() {
    return selectExpression;
  }

  public class JPQLSelectSingleContextBuilder
      extends
      com.sap.core.odata.processor.api.jpa.jpql.JPQLContext.JPQLContextBuilder {

    protected GetEntityUriInfo entityView;

    @Override
    public JPQLContext build() throws ODataJPAModelException, ODataJPARuntimeException {
      if (entityView != null) {

        try {

          setType(JPQLContextType.SELECT_SINGLE);

          EdmEntityType entityType = entityView
              .getTargetEntitySet().getEntityType();
          EdmMapping mapping = entityType.getMapping();
          if (mapping != null) {
            setJPAEntityName(mapping.getInternalName());
          } else {
            setJPAEntityName(entityType.getName());
          }

          setJPAEntityAlias(generateJPAEntityAlias());

          setKeyPredicates(entityView.getKeyPredicates());

          setSelectExpression(generateSelectExpression());

        } catch (EdmException e) {
          throw ODataJPARuntimeException.throwException(
              ODataJPARuntimeException.GENERAL.addContent(e
                  .getMessage()), e);
        }

      }

      return JPQLSelectSingleContext.this;

    }

    @Override
    protected void setResultsView(final Object resultsView) {
      if (resultsView instanceof GetEntityUriInfo) {
        entityView = (GetEntityUriInfo) resultsView;
      }

    }

    /*
     * Generate Select Clause 
     */
    protected String generateSelectExpression() throws EdmException {
      return getJPAEntityAlias();
    }
  }
}
