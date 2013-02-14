package com.sap.core.odata.processor.core.jpa.jpql;

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
import com.sap.core.odata.processor.api.jpa.access.JPAJoinClause;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.jpql.JPQLJoinSelectContext;
import com.sap.core.odata.processor.core.jpa.jpql.JPQLJoinSelectContext.JPQLJoinContextBuilder;

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
		final EdmNavigationProperty navigationProperty = createNavigationProperty("a");
		final EdmNavigationProperty navigationProperty1 = createNavigationProperty("b");
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
		NavigationSegment navigationSegment1 = new NavigationSegment() {
			
			@Override
			public EdmNavigationProperty getNavigationProperty() {
				return navigationProperty1;
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
		navigationSegments.add(navigationSegment1);
		EasyMock.expect(entitySetUriInfo.getNavigationSegments()).andStubReturn(navigationSegments);
		EasyMock.expect(entitySetUriInfo.getOrderBy()).andStubReturn(null);
		EasyMock.expect(entitySetUriInfo.getTop()).andStubReturn(null);
		EasyMock.expect(entitySetUriInfo.getSkip()).andStubReturn(null);
		EasyMock.expect(entitySetUriInfo.getSelect()).andStubReturn(null);
		EasyMock.expect(entitySetUriInfo.getFilter()).andStubReturn(null);
		EasyMock.expect(entitySetUriInfo.getKeyPredicates()).andStubReturn(keyPredicates);
		EasyMock.expect(entitySetUriInfo
							.getTargetEntitySet()).andStubReturn(edmEntitySet);
		EdmEntitySet startEdmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType startEdmEntityType = EasyMock.createMock(EdmEntityType.class);
		EasyMock.expect(startEdmEntityType.getName()).andStubReturn("SOHeader");
		EasyMock.expect(startEdmEntitySet.getEntityType()).andStubReturn(startEdmEntityType);
		EasyMock.expect(entitySetUriInfo.getStartEntitySet()).andStubReturn(
				startEdmEntitySet);
		EasyMock.replay(startEdmEntityType,startEdmEntitySet);
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
		assertEquals("E1",joinClauses.get(0).getEntityAlias());
		assertEquals("SOHeader" , joinClauses.get(0).getEntityName());
		assertEquals("s_Itema", joinClauses.get(1).getEntityRelationShip());
		assertEquals("R1", joinClauses.get(1).getEntityRelationShipAlias());
	}
	
	private EdmNavigationProperty createNavigationProperty(String z) throws EdmException{
		EdmNavigationProperty navigationProperty = EasyMock.createMock(EdmNavigationProperty.class);
		EdmAssociation association = EasyMock.createMock(EdmAssociation.class);
		EdmAssociationEnd associationEnd = EasyMock.createMock(EdmAssociationEnd.class);
		EasyMock.expect(navigationProperty.getFromRole()).andStubReturn("roleA"+z);
		EasyMock.expect(navigationProperty.getToRole()).andStubReturn("roleB"+z);
		EasyMock.expect(navigationProperty.getName()).andStubReturn("navP"+z);
		EasyMock.expect(navigationProperty.getName()).andStubReturn("navP"+z);
		EasyMock.expect(navigationProperty.getMultiplicity()).andStubReturn(EdmMultiplicity.ONE);
		EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(edmMapping.getInternalName()).andStubReturn("sItem"+z);
		EasyMock.expect(edmEntityType.getMapping()).andStubReturn(edmMapping );
		EasyMock.expect(edmEntityType.getName()).andStubReturn("soItem"+z);
		EasyMock.expect(associationEnd.getEntityType()).andStubReturn(edmEntityType );
		EasyMock.expect(association.getEnd("roleA"+z)).andStubReturn(associationEnd);
		EasyMock.expect(navigationProperty.getRelationship()).andStubReturn(association);
		EdmMapping edmMapping1 = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(edmMapping1.getInternalName()).andStubReturn("s_Item"+z);
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
