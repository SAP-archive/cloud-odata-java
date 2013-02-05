package com.sap.core.odata.processor.core.jpa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.metamodel.Attribute;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.processor.core.jpa.model.mock.JPAAttributeMock;
import com.sap.core.odata.processor.core.jpa.testdata.JPAEdmMockData.SimpleType;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmReferentialConstraintView;

public class JPAEdmAssociationTest extends JPAEdmTestModelView {


	private static JPAEdmAssociation objAssociation = null;
	private static String ASSOCIATION_NAME = "Association_SalesOrderHeader_String";
	private static JPAEdmAssociationTest localView = null;
	@BeforeClass
	public static void setup() throws ODataJPAModelException{
		localView = new JPAEdmAssociationTest();
		objAssociation = new JPAEdmAssociation(localView,localView,localView);
		objAssociation.getBuilder().build();
	}

	@Override
	public AssociationEnd getEdmAssociationEnd1() {
		AssociationEnd associationEnd = new AssociationEnd();
		associationEnd.setType(new FullQualifiedName("salesorderprocessing", "SalesOrderHeader"));
		associationEnd.setRole("SalesOrderHeader");
		associationEnd.setMultiplicity(EdmMultiplicity.ONE);
		return associationEnd;
	}

	@Override
	public AssociationEnd getEdmAssociationEnd2() {
		AssociationEnd associationEnd = new AssociationEnd();
		associationEnd.setType(new FullQualifiedName("salesorderprocessing", "String"));
		associationEnd.setRole("String");
		associationEnd.setMultiplicity(EdmMultiplicity.MANY);
		return associationEnd;
	}

	@Override
	public Association getEdmAssociation() {
		Association association = new Association();
		association.setEnd1(new AssociationEnd().setType(new FullQualifiedName("salesorderprocessing", "SalesOrderHeader")));
		association.setEnd2(new AssociationEnd().setType(new FullQualifiedName("salesorderprocessing","String")));
		
		return association;
	}
	@Override
	public boolean isExists() {
		return true;
	}
	@Override
	public JPAEdmReferentialConstraintView getJPAEdmReferentialConstraintView() {
		JPAEdmReferentialConstraint refConstraintView = new JPAEdmReferentialConstraint(localView, localView, localView);
		return refConstraintView;
	}


	@Test
	public void testGetBuilder() throws ODataJPAModelException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
		/*JPAEdmReferentialConstraint refConstraintView = new JPAEdmReferentialConstraint(localView, localView, localView);
		Class<?> clazz = Class.forName("com.sap.core.odata.processor.jpa.model.JPAEdmReferentialConstraint");
		Field f = clazz.getField("exists");
		f.setAccessible(true);
		f.set(refConstraintView, true);
		objAssociation.addJPAEdmRefConstraintView(refConstraintView);
		objAssociation.getBuilder().build();*/
		assertNotNull(objAssociation.getBuilder());
	}

	@Test
	public void testGetEdmAssociation() {
		assertNotNull(objAssociation.getEdmAssociation());
		assertEquals(objAssociation.getEdmAssociation().getName(), ASSOCIATION_NAME);
	}

	@Test
	public void testGetConsistentEdmAssociationList() {
		assertTrue(objAssociation.getConsistentEdmAssociationList().size()>0);
	}

	
	@Override
	public String getEdmRelationShipName() {
		return "Association_SalesOrderHeader_String";
	}

	

	@Test
	public void testSearchAssociation1() throws ODataJPAModelException {
		class TestAssociationEndView extends JPAEdmTestModelView
		{
			private Attribute<?,?> getJPAAttributeLocal() {
				AttributeMock<Object, String> attr = new AttributeMock<Object,String>();
				return attr;
			}

			@Override
			public Attribute<?, ?> getJPAAttribute() {
				return getJPAAttributeLocal();
			}

			@Override
			public String getpUnitName() {
				return "salesorderprocessing";
			}

			@Override
			public EntityType getEdmEntityType() {
				EntityType entityType = new EntityType();
				entityType.setName("SalesOrderHeader");
				return entityType;
			}
			@SuppressWarnings("hiding")
			class AttributeMock<Object, String> extends JPAAttributeMock<Object, String> {

				@SuppressWarnings("unchecked")
				@Override
				public Class<String> getJavaType() {
					return (Class<String>) SimpleType.SimpleTypeA.clazz;
				}

				@Override
				public PersistentAttributeType getPersistentAttributeType() {
					
						return PersistentAttributeType.ONE_TO_MANY;
					
				
				}

				
			}
		}
		TestAssociationEndView objJPAEdmAssociationEndTest = new TestAssociationEndView();
		JPAEdmAssociationEnd objJPAEdmAssociationEnd = new JPAEdmAssociationEnd(objJPAEdmAssociationEndTest,objJPAEdmAssociationEndTest);
		objJPAEdmAssociationEnd.getBuilder().build();
		assertNotNull(objAssociation.searchAssociation(objJPAEdmAssociationEnd));
		
	}
	

	@Test
	public void testAddJPAEdmAssociationView() throws ODataJPAModelException {
		
		class LocalJPAAssociationView extends JPAEdmTestModelView
		{
			@Override
			public AssociationEnd getEdmAssociationEnd1() {
				AssociationEnd associationEnd = new AssociationEnd();
				associationEnd.setType(new FullQualifiedName("salesorderprocessing", "SalesOrderHeader"));
				associationEnd.setRole("SalesOrderHeader");
				associationEnd.setMultiplicity(EdmMultiplicity.ONE);
				return associationEnd;
			}

			@Override
			public AssociationEnd getEdmAssociationEnd2() {
				AssociationEnd associationEnd = new AssociationEnd();
				associationEnd.setType(new FullQualifiedName("salesorderprocessing", "SalesOrderItem"));
				associationEnd.setRole("SalesOrderItem");
				associationEnd.setMultiplicity(EdmMultiplicity.MANY);
				return associationEnd;
			}

			@Override
			public Association getEdmAssociation() {
				Association association = new Association();
				association.setEnd1(new AssociationEnd().setType(new FullQualifiedName("salesorderprocessing", "SalesOrderHeader")));
				association.setEnd2(new AssociationEnd().setType(new FullQualifiedName("salesorderprocessing","SalesOrderItem")));
				
				return association;
			}
		}
		LocalJPAAssociationView assocViewObj = new LocalJPAAssociationView();
		JPAEdmAssociation objLocalAssociation = new JPAEdmAssociation(assocViewObj,assocViewObj,assocViewObj);
		objLocalAssociation.getBuilder().build();
		objAssociation.addJPAEdmAssociationView(objLocalAssociation);
		
		
	}

	
	@Test
	public void testAddJPAEdmRefConstraintView() {
		
	}

	@Test
	public void testGetJPAEdmReferentialConstraintView() {
		
	}
}
