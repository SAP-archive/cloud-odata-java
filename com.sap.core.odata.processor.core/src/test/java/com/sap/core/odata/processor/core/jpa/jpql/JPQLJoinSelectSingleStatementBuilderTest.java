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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.processor.api.jpa.access.JPAJoinClause;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLJoinSelectSingleContextView;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement;

public class JPQLJoinSelectSingleStatementBuilderTest {
  JPQLJoinSelectSingleContextView context = null;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  public void setUp(final List<JPAJoinClause> joinClauseList) throws Exception {
    context = EasyMock.createMock(JPQLJoinSelectSingleContextView.class);
    EasyMock.expect(context.getJPAEntityAlias()).andStubReturn("gt1");
    EasyMock.expect(context.getJPAEntityName()).andStubReturn("SOHeader");
    EasyMock.expect(context.getType()).andStubReturn(JPQLContextType.SELECT);
    EasyMock.expect(context.getKeyPredicates()).andStubReturn(createKeyPredicates());
    EasyMock.expect(context.getSelectExpression()).andStubReturn("gt1");
    EasyMock.expect(context.getJPAJoinClauses()).andStubReturn(joinClauseList);
    EasyMock.replay(context);
  }

  private List<JPAJoinClause> getJoinClauseList() {
    List<JPAJoinClause> joinClauseList = new ArrayList<JPAJoinClause>();
    JPAJoinClause jpaOuterJoinClause = new JPAJoinClause("SOHeader", "soh", null, null, "soh.soId = 1", JPAJoinClause.JOIN.LEFT);
    joinClauseList.add(jpaOuterJoinClause);
    jpaOuterJoinClause = new JPAJoinClause("SOHeader", "soh", "soItem", "soi", "soi.shId = soh.soId", JPAJoinClause.JOIN.LEFT);
    joinClauseList.add(jpaOuterJoinClause);
    jpaOuterJoinClause = new JPAJoinClause("SOItem", "si", "material", "mat", "mat.id = 'abc'", JPAJoinClause.JOIN.LEFT);
    joinClauseList.add(jpaOuterJoinClause);
    return joinClauseList;
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testBuild() throws Exception {
    setUp(getJoinClauseList());
    JPQLJoinSelectSingleStatementBuilder jpqlJoinSelectsingleStatementBuilder = new JPQLJoinSelectSingleStatementBuilder(context);
    try {
      JPQLStatement jpqlStatement = jpqlJoinSelectsingleStatementBuilder.build();
      assertEquals("SELECT gt1 FROM SOHeader soh JOIN soh.soItem soi JOIN soi.material mat WHERE soh.soId = 1 AND soi.shId = soh.soId AND mat.id = 'abc'", jpqlStatement.toString());
    } catch (ODataJPARuntimeException e) {
      fail("Should not have come here");
    }

  }

  private List<KeyPredicate> createKeyPredicates() throws EdmException {
    KeyPredicate keyPredicate = EasyMock.createMock(KeyPredicate.class);
    EasyMock.expect(keyPredicate.getLiteral()).andStubReturn("1");
    EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);
    EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
    EasyMock.expect(edmMapping.getInternalName()).andStubReturn("soid");
    EasyMock.expect(edmProperty.getMapping()).andStubReturn(edmMapping);
    EdmSimpleType edmType = EasyMock.createMock(EdmSimpleType.class);
    EasyMock.expect(edmProperty.getType()).andStubReturn(edmType);
    EasyMock.expect(keyPredicate.getProperty()).andStubReturn(edmProperty);

    EasyMock.replay(edmType, edmMapping, edmProperty, keyPredicate);
    List<KeyPredicate> keyPredicates = new ArrayList<KeyPredicate>();
    keyPredicates.add(keyPredicate);
    return keyPredicates;
  }

  @Test
  public void testJoinClauseAsNull() throws Exception {
    setUp(null);
    JPQLJoinSelectSingleStatementBuilder jpqlJoinSelectsingleStatementBuilder = new JPQLJoinSelectSingleStatementBuilder(context);
    try {
      jpqlJoinSelectsingleStatementBuilder.build();
      fail("Should not have come here");
    } catch (ODataJPARuntimeException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testJoinClauseListAsEmpty() throws Exception {
    List<JPAJoinClause> joinClauseList = new ArrayList<JPAJoinClause>();
    setUp(joinClauseList);
    JPQLJoinSelectSingleStatementBuilder jpqlJoinSelectsingleStatementBuilder = new JPQLJoinSelectSingleStatementBuilder(context);
    try {
      jpqlJoinSelectsingleStatementBuilder.build();
      fail("Should not have come here");
    } catch (ODataJPARuntimeException e) {
      assertTrue(true);
    }
  }

}
