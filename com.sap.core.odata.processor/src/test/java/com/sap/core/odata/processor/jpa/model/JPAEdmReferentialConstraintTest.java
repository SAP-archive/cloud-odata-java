package com.sap.core.odata.processor.jpa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;

import javax.persistence.JoinColumn;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.model.mock.JPAAttributeMock;
import com.sap.core.odata.processor.jpa.model.mock.JPAJavaMemberMock;
import com.sap.core.odata.processor.jpa.model.mock.JPAManagedTypeMock;

public class JPAEdmReferentialConstraintTest extends JPAEdmTestModelView {

	
	private static JPAEdmReferentialConstraint objJPAEdmReferentialConstraint = null;
	private static JPAEdmReferentialConstraintTest objJPAEdmReferentialConstraintTest = null;
	
	
	@Before
	public void setUp() {
		objJPAEdmReferentialConstraintTest = new JPAEdmReferentialConstraintTest();
		objJPAEdmReferentialConstraint = new JPAEdmReferentialConstraint(objJPAEdmReferentialConstraintTest, 
				objJPAEdmReferentialConstraintTest, objJPAEdmReferentialConstraintTest);
		try {
			objJPAEdmReferentialConstraint.getBuilder().build();
		} catch (ODataJPAModelException e) {
			fail("ODataJPAModelException not expected");
		}
	}

	@Test
	public void testGetBuilder() {
		assertNotNull(objJPAEdmReferentialConstraint.getBuilder());
	}
	
	@Test
	public void testGetBuilderIdempotent(){
		JPAEdmBuilder builder1 = objJPAEdmReferentialConstraint.getBuilder();
		JPAEdmBuilder builder2 = objJPAEdmReferentialConstraint.getBuilder();
		
		assertEquals(builder1.hashCode(), builder2.hashCode());
	}

	@Test
	public void testGetEdmReferentialConstraint() {
		assertNotNull(objJPAEdmReferentialConstraint.getEdmReferentialConstraint());
	}

	
	@Test
	public void testIsExistsTrue() {
		objJPAEdmReferentialConstraintTest = new JPAEdmReferentialConstraintTest();
		objJPAEdmReferentialConstraint = new JPAEdmReferentialConstraint(objJPAEdmReferentialConstraintTest, 
				objJPAEdmReferentialConstraintTest, objJPAEdmReferentialConstraintTest);
		try {
			objJPAEdmReferentialConstraint.getBuilder().build();
			objJPAEdmReferentialConstraint.getBuilder().build();
		} catch (ODataJPAModelException e) {
			fail("ODataJPAModelException not expected");
		}
		assertTrue(objJPAEdmReferentialConstraint.isExists());
	}

	@Test
	public void testGetRelationShipName() {
		assertEquals("Assoc_SalesOrderHeader_SalesOrderItem", objJPAEdmReferentialConstraint.getEdmRelationShipName());
	}
	
	
	@Override
	public Association getEdmAssociation() {
		Association association = new Association();
		association.setName("Assoc_SalesOrderHeader_SalesOrderItem");
		association.setEnd1(new AssociationEnd().setType(new FullQualifiedName("salesorderprocessing", "String")).setRole("SalesOrderHeader"));
		association.setEnd2(new AssociationEnd().setType(new FullQualifiedName("salesorderprocessing", "SalesOrderItem")).setRole("SalesOrderItem"));
		return association;
	}
	
	private Attribute<?,?> getJPAAttributeLocal() {
		AttributeMock<Object, String> attr = new AttributeMock<Object,String>();
		return attr;
	}

	@Override
	public Attribute<?, ?> getJPAAttribute() {
		return getJPAAttributeLocal();
	}
	
	
	@SuppressWarnings("hiding")
	private class AttributeMock<Object, String> extends JPAAttributeMock<Object, String> {

		@Override
		public Member getJavaMember() {
			return new JavaMemberMock();
		}

		@SuppressWarnings("unchecked")
		@Override
		public Class<String> getJavaType() {
			return (Class<String>) java.lang.String.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ManagedType<Object> getDeclaringType() {
			return (ManagedType<Object>) getManagedTypeLocal();
		}

		private ManagedType<?> getManagedTypeLocal() {
			ManagedTypeMock<String> managedTypeMock = new ManagedTypeMock<String>();
			return managedTypeMock;
		}
	}
	
	@SuppressWarnings("hiding")
	private class ManagedTypeMock<String> extends JPAManagedTypeMock<String>{

		@SuppressWarnings("unchecked")
		@Override
		public Class<String> getJavaType() {
			return (Class<String>) java.lang.String.class;
		}
	}
	
	private class JavaMemberMock extends JPAJavaMemberMock{
		@SuppressWarnings("unchecked")
		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
			JoinColumn joinColumn = EasyMock.createMock(JoinColumn.class);
			EasyMock.expect(joinColumn.referencedColumnName()).andReturn("SOID");
			EasyMock.expect(joinColumn.name()).andReturn("SOID");
			
			EasyMock.replay(joinColumn);
			return (T) joinColumn;
		}
	}

}
