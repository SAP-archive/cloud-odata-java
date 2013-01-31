package com.sap.core.odata.processor.jpa.jpql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

	@Before
	public void setUp() throws Exception {
		context = EasyMock.createMock(JPQLJoinContextView.class);
		EasyMock.expect(context.getJPAEntityAlias()).andStubReturn("mat");
		EasyMock.expect(context.getJPAEntityName()).andStubReturn("SOHeader");
		EasyMock.expect(context.getType()).andStubReturn(JPQLContextType.SELECT);
		EasyMock.expect(context.getWhereExpression()).andStubReturn("soh.buyerId = 2 AND soh.createdBy = 'Peter'");
		HashMap<String, String> orderByMap = new HashMap<String, String>();
		orderByMap.put("buyerId", "asc");
		orderByMap.put("city", "desc");
		EasyMock.expect(context.getOrderByCollection()).andStubReturn(orderByMap);
		List<JPAJoinClause> joinClauseList = new ArrayList<JPAJoinClause>();
		JPAJoinClause jpaOuterJoinClause = new JPAJoinClause("SOHeader", "soh", "soItem", "soi", "soi.shId = soh.soId", JPAJoinClause.JOIN.LEFT);
		joinClauseList.add(jpaOuterJoinClause);
		jpaOuterJoinClause = new JPAJoinClause("SOItem", "si", "material", "mat", "mat.id = 'abc'", JPAJoinClause.JOIN.LEFT);
		joinClauseList.add(jpaOuterJoinClause);
		EasyMock.expect(context.getJPAJoinClauses()).andStubReturn(joinClauseList);
		EasyMock.replay(context);		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuild() {
		JPQLJoinStatementBuilder jpqlJoinStatementBuilder = new JPQLJoinStatementBuilder(context);
		try {
			JPQLStatement jpqlStatement = jpqlJoinStatementBuilder.build();
			assertEquals("SELECT mat FROM SOHeader soh JOIN soh.soItem soi JOIN soi.material mat WHERE soh.buyerId = 2 AND soh.createdBy = 'Peter' AND soi.shId = soh.soId  AND mat.id = 'abc' ORDER BY mat.buyerId asc , mat.city desc ", jpqlStatement.toString());
		} catch (ODataJPARuntimeException e) {
			fail("Should not have come here");
		}
		
	}

	
}
