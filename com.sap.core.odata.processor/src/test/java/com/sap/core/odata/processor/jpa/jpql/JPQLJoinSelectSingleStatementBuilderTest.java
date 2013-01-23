package com.sap.core.odata.processor.jpa.jpql;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.processor.jpa.api.access.JPAOuterJoinClause;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLJoinContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLJoinSelectSingleContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPQLJoinSelectSingleStatementBuilderTest {
	JPQLJoinSelectSingleContextView context = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		context = EasyMock.createMock(JPQLJoinSelectSingleContextView.class);
		EasyMock.expect(context.getJPAEntityAlias()).andStubReturn("gt1");
		EasyMock.expect(context.getJPAEntityName()).andStubReturn("SOHeader");
		EasyMock.expect(context.getType()).andStubReturn(JPQLContextType.SELECT);
		EasyMock.expect(context.getKeyPredicates()).andStubReturn(createKeyPredicates());
		EasyMock.expect(context.getSelectedFields()).andStubReturn(null);
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
		JPQLJoinSelectSingleStatementBuilder jpqlJoinSelectsingleStatementBuilder = new JPQLJoinSelectSingleStatementBuilder(context);
		try {
			JPQLStatement jpqlStatement = jpqlJoinSelectsingleStatementBuilder.build();
			assertEquals("SELECT gt1 SOHeader gt1 JOIN soh.soItem soi JOIN soi.material mat WHERE gt1.soid = 1 AND soi.shId = soh.soId AND mat.id = 'abc' ", jpqlStatement.toString());
		} catch (ODataJPARuntimeException e) {
			fail("Should not have come here");
		}
		
	}
	
	private List<KeyPredicate> createKeyPredicates() throws EdmException {
		KeyPredicate keyPredicate = EasyMock.createMock(KeyPredicate.class);
		EasyMock.expect(keyPredicate.getLiteral()).andStubReturn("1");
		EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);
		EasyMock.expect(edmProperty.getName()).andStubReturn("soid");
		EdmSimpleType edmType = EasyMock.createMock(EdmSimpleType.class);
		EasyMock.expect(edmProperty.getType()).andStubReturn(edmType );
		EasyMock.expect(keyPredicate.getProperty()).andStubReturn(edmProperty );
		
		EasyMock.replay(edmType, edmProperty, keyPredicate);
		List<KeyPredicate> keyPredicates = new ArrayList<KeyPredicate>();
		keyPredicates.add(keyPredicate);
		return keyPredicates;
	}

}
