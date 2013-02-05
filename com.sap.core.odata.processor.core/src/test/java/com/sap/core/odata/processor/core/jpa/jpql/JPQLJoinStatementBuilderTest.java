package com.sap.core.odata.processor.core.jpa.jpql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.processor.core.jpa.jpql.JPQLJoinStatementBuilder;
import com.sap.core.odata.processor.jpa.api.access.JPAJoinClause;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLJoinContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement;

public class JPQLJoinStatementBuilderTest {
	JPQLJoinContextView context = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	public void setUp(List<JPAJoinClause> joinClauseList) throws Exception {
		context = EasyMock.createMock(JPQLJoinContextView.class);
		EasyMock.expect(context.getJPAEntityAlias()).andStubReturn("mat");
		EasyMock.expect(context.getJPAEntityName()).andStubReturn("SOHeader");
		EasyMock.expect(context.getType()).andStubReturn(JPQLContextType.SELECT);
		EasyMock.expect(context.getSelectExpression()).andStubReturn("mat");
		EasyMock.expect(context.getWhereExpression()).andStubReturn("soh.buyerId = 2 AND soh.createdBy = 'Peter'");
		HashMap<String, String> orderByMap = new HashMap<String, String>();
		orderByMap.put("buyerId", "asc");
		orderByMap.put("city", "desc");
		EasyMock.expect(context.getOrderByCollection()).andStubReturn(orderByMap);
		EasyMock.expect(context.getJPAJoinClauses()).andStubReturn(joinClauseList);
		EasyMock.replay(context);		
	}

	private List<JPAJoinClause> getJoinClauseList() {
		List<JPAJoinClause> joinClauseList = new ArrayList<JPAJoinClause>();
		JPAJoinClause jpaOuterJoinClause = new JPAJoinClause("SOHeader", "soh", "soItem", "soi", "soi.shId = soh.soId", JPAJoinClause.JOIN.LEFT);
		joinClauseList.add(jpaOuterJoinClause);
		jpaOuterJoinClause = new JPAJoinClause("SOItem", "si", "material", "mat", "mat.id = 'abc'", JPAJoinClause.JOIN.LEFT);
		joinClauseList.add(jpaOuterJoinClause);
		return joinClauseList;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuild() throws Exception {
		setUp(getJoinClauseList());
		JPQLJoinStatementBuilder jpqlJoinStatementBuilder = new JPQLJoinStatementBuilder(context);
		try {
			JPQLStatement jpqlStatement = jpqlJoinStatementBuilder.build();
			assertEquals("SELECT mat FROM SOHeader soh JOIN soh.soItem soi JOIN soi.material mat WHERE soh.buyerId = 2 AND soh.createdBy = 'Peter' AND soi.shId = soh.soId  AND mat.id = 'abc' ORDER BY mat.buyerId asc , mat.city desc", jpqlStatement.toString());
		} catch (ODataJPARuntimeException e) {
			fail("Should not have come here");
		}
		
	}
	
	@Test
	public void testJoinClauseAsNull() throws Exception {
		setUp(null);
		JPQLJoinStatementBuilder jpqlJoinStatementBuilder = new JPQLJoinStatementBuilder(context);
		try {
			jpqlJoinStatementBuilder.build();
			fail("Should not have come here");
			} catch (ODataJPARuntimeException e) {
			assertTrue(true);
		}		
	}
	
	@Test
	public void testJoinClauseListAsEmpty() throws Exception {
		setUp(new ArrayList<JPAJoinClause>());
		JPQLJoinStatementBuilder jpqlJoinStatementBuilder = new JPQLJoinStatementBuilder(context);
		try {
			jpqlJoinStatementBuilder.build();
			fail("Should not have come here");
			} catch (ODataJPARuntimeException e) {
			assertTrue(true);
		}
	}

	
}
