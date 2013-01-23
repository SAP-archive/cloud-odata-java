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

import com.sap.core.odata.processor.jpa.api.access.JPAOuterJoinClause;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLJoinContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

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
		EasyMock.expect(context.getJPAEntityAlias()).andStubReturn("gt1");
		EasyMock.expect(context.getJPAEntityName()).andStubReturn("SOHeader");
		EasyMock.expect(context.getType()).andStubReturn(JPQLContextType.SELECT);
		EasyMock.expect(context.getWhereExpression()).andStubReturn("gt1.buyerId = 2 AND gt1.createdBy = 'Peter'");
		HashMap<String, String> orderByMap = new HashMap<String, String>();
		orderByMap.put("buyerId", "asc");
		orderByMap.put("city", "desc");
		EasyMock.expect(context.getOrderByCollection()).andStubReturn(orderByMap);
		List<JPAOuterJoinClause> joinClauseList = new ArrayList<JPAOuterJoinClause>();
		JPAOuterJoinClause jpaOuterJoinClause = new JPAOuterJoinClause("SOHeader", "soh", "soItem", "soi", "soi.shId = soh.soId", JPAOuterJoinClause.JOIN.LEFT);
		joinClauseList.add(jpaOuterJoinClause);
		jpaOuterJoinClause = new JPAOuterJoinClause("SOItem", "si", "material", "mat", "mat.id = 'abc'", JPAOuterJoinClause.JOIN.LEFT);
		joinClauseList.add(jpaOuterJoinClause);
		EasyMock.expect(context.getJPAOuterJoinClauses()).andStubReturn(joinClauseList);
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
			assertEquals("SELECT gt1 SOHeader gt1 JOIN soh.soItem soi JOIN soi.material mat WHERE gt1.buyerId = 2 AND gt1.createdBy = 'Peter' AND soi.shId = soh.soId  AND mat.id = 'abc'  ORDER BY gt1.buyerId asc , gt1.city desc ", jpqlStatement.toString());
		} catch (ODataJPARuntimeException e) {
			fail("Should not have come here");
		}
		
	}

	
}
