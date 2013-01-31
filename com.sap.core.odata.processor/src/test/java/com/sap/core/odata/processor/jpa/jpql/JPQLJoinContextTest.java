package com.sap.core.odata.processor.jpa.jpql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmAssociationEnd;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.processor.jpa.api.access.JPAJoinClause;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.jpql.JPQLJoinSelectContext.JPQLJoinContextBuilder;

public class JPQLJoinContextTest {
	
	GetEntitySetUriInfo entitySetUriInfo;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		entitySetUriInfo = EasyMock.createMock(GetEntitySetUriInfo.class);
		EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
		List<NavigationSegment> navigationSegments = new ArrayList<NavigationSegment>();
		final EdmNavigationProperty navigationProperty = createNavigationProperty();
		final List<KeyPredicate> keyPredicates = createKeyPredicates();
		NavigationSegment navigationSegment = new NavigationSegment() {
			
			@Override
			public EdmNavigationProperty getNavigationProperty() {
				return navigationProperty;
			}
			
			@Override
			public List<KeyPredicate> getKeyPredicates() {
				return keyPredicates;
			}
			
			@Override
			public EdmEntitySet getEntitySet() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		navigationSegments.add(navigationSegment);
		EasyMock.expect(entitySetUriInfo.getNavigationSegments()).andStubReturn(navigationSegments);
		EasyMock.expect(entitySetUriInfo.getOrderBy()).andStubReturn(null);
		EasyMock.expect(entitySetUriInfo.getTop()).andStubReturn(null);
		EasyMock.expect(entitySetUriInfo.getSkip()).andStubReturn(null);
		EasyMock.expect(entitySetUriInfo.getSelect()).andStubReturn(null);
		EasyMock.expect(entitySetUriInfo.getFilter()).andStubReturn(null);
		EasyMock.expect(entitySetUriInfo
							.getTargetEntitySet()).andStubReturn(edmEntitySet);
		EasyMock.expect(edmEntitySet.getEntityType()).andStubReturn(edmEntityType);
		EasyMock.expect(edmEntityType.getName()).andStubReturn("SOHeader");
		EasyMock.replay(edmEntityType,edmEntitySet,entitySetUriInfo);
		
	}

	

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetJPAOuterJoinClauses() {
		JPQLJoinSelectContext joinContext = new JPQLJoinSelectContext();
		JPQLJoinContextBuilder joinContextBuilder = joinContext.new JPQLJoinContextBuilder();
		try { 
			joinContextBuilder.entitySetView = entitySetUriInfo;
			joinContextBuilder.build();
		} catch (ODataJPAModelException e) {
			fail("Should not come here");
		} catch (ODataJPARuntimeException e) {
			fail("Should not come here");
		}
		List<JPAJoinClause> joinClauses = joinContext.getJPAJoinClauses();
		assertNotNull(joinClauses);
		assertTrue(joinClauses.size() > 0);
		assertEquals(joinClauses.get(0).getEntityAlias(), "E1");
		assertEquals(joinClauses.get(0).getEntityName(), "sItem");
		assertEquals(joinClauses.get(0).getEntityRelationShip(), "s_Item");
		assertEquals(joinClauses.get(0).getEntityRelationShipAlias(), "R1");
	}
	
	private EdmNavigationProperty createNavigationProperty() throws EdmException{
		EdmNavigationProperty navigationProperty = EasyMock.createMock(EdmNavigationProperty.class);
		EdmAssociation association = EasyMock.createMock(EdmAssociation.class);
		EdmAssociationEnd associationEnd = EasyMock.createMock(EdmAssociationEnd.class);
		EasyMock.expect(navigationProperty.getFromRole()).andStubReturn("roleA");
		EasyMock.expect(navigationProperty.getToRole()).andStubReturn("roleB");
		EasyMock.expect(navigationProperty.getName()).andStubReturn("navP");
		EasyMock.expect(navigationProperty.getName()).andStubReturn("navP");
		EasyMock.expect(navigationProperty.getMultiplicity()).andStubReturn(EdmMultiplicity.ONE);
		EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(edmMapping.getInternalName()).andStubReturn("sItem");
		EasyMock.expect(edmEntityType.getMapping()).andStubReturn(edmMapping );
		EasyMock.expect(edmEntityType.getName()).andStubReturn("soItem");
		EasyMock.expect(associationEnd.getEntityType()).andStubReturn(edmEntityType );
		EasyMock.expect(association.getEnd("roleA")).andStubReturn(associationEnd);
		EasyMock.expect(navigationProperty.getRelationship()).andStubReturn(association);
		EdmMapping edmMapping1 = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(edmMapping1.getInternalName()).andStubReturn("s_Item");
		EasyMock.expect(navigationProperty.getMapping()).andStubReturn(edmMapping1 );
		EasyMock.replay(edmMapping, edmMapping1, edmEntityType, associationEnd, association, navigationProperty);
		return navigationProperty;
	}
	
	private List<KeyPredicate> createKeyPredicates() throws EdmException {
		KeyPredicate keyPredicate = EasyMock.createMock(KeyPredicate.class);
		EasyMock.expect(keyPredicate.getLiteral()).andStubReturn("1");
		EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(edmMapping.getInternalName()).andStubReturn("soid");
		EasyMock.expect(edmProperty.getMapping()).andStubReturn(edmMapping);
		EasyMock.expect(edmProperty.getName()).andStubReturn("soid");
		EdmSimpleType edmType = EasyMock.createMock(EdmSimpleType.class);
		EasyMock.expect(edmProperty.getType()).andStubReturn(edmType );
		EasyMock.expect(keyPredicate.getProperty()).andStubReturn(edmProperty );
		
		EasyMock.replay(edmType,edmMapping, edmProperty, keyPredicate);
		List<KeyPredicate> keyPredicates = new ArrayList<KeyPredicate>();
		keyPredicates.add(keyPredicate);
		return keyPredicates;
	}

}
