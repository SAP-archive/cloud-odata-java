package com.sap.core.odata.processor.core.jpa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmMappingImpl;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmReferentialConstraintRole;
import com.sap.core.odata.processor.core.jpa.model.mock.JPAAttributeMock;
import com.sap.core.odata.processor.core.jpa.model.mock.JPAJavaMemberMock;
import com.sap.core.odata.processor.core.jpa.model.mock.JPAManagedTypeMock;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmReferentialConstraintRoleView.RoleType;

public class JPAEdmReferentialConstraintRoleTest extends JPAEdmTestModelView {

	private static JPAEdmReferentialConstraintRole objJPAEdmReferentialConstraintRole = null;
	private static JPAEdmReferentialConstraintRoleTest objJPAEdmReferentialConstraintRoleTest = null;

	@Before
	public void setUp() throws Exception {
		objJPAEdmReferentialConstraintRoleTest = new JPAEdmReferentialConstraintRoleTest();

		objJPAEdmReferentialConstraintRole = new JPAEdmReferentialConstraintRole(
				RoleType.PRINCIPAL, objJPAEdmReferentialConstraintRoleTest,
				objJPAEdmReferentialConstraintRoleTest,
				objJPAEdmReferentialConstraintRoleTest);

		objJPAEdmReferentialConstraintRole.getBuilder().build();
	}

	@Test
	public void testIsExists() {
		assertTrue(objJPAEdmReferentialConstraintRole.isExists());// Default
	}

	@Test
	public void testGetBuilder() {
		assertNotNull(objJPAEdmReferentialConstraintRole.getBuilder());
	}

	@Test
	public void testGetBuilderIdempotent() {
		JPAEdmBuilder builder1 = objJPAEdmReferentialConstraintRole
				.getBuilder();
		JPAEdmBuilder builder2 = objJPAEdmReferentialConstraintRole
				.getBuilder();

		assertEquals(builder1.hashCode(), builder2.hashCode());
	}

	@Test
	public void testGetRoleTypePrincipal() {
		assertEquals(objJPAEdmReferentialConstraintRole.getRoleType(),
				RoleType.PRINCIPAL);
	}

	@Test
	public void testGetRoleTypeDependent() {
		objJPAEdmReferentialConstraintRoleTest = new JPAEdmReferentialConstraintRoleTest();
		objJPAEdmReferentialConstraintRole = new JPAEdmReferentialConstraintRole(
				RoleType.DEPENDENT, objJPAEdmReferentialConstraintRoleTest,
				objJPAEdmReferentialConstraintRoleTest,
				objJPAEdmReferentialConstraintRoleTest);

		try {
			objJPAEdmReferentialConstraintRole.getBuilder().build();
			objJPAEdmReferentialConstraintRole.getBuilder().build();
		} catch (ODataJPAModelException e) {
			fail("Exception ODataJPAModelException Not Expected");
		} catch (ODataJPARuntimeException e) {
			fail("Exception ODataJPARuntimeException Not Expected");
		}
		assertEquals(objJPAEdmReferentialConstraintRole.getRoleType(),
				RoleType.DEPENDENT);
	}

	@Test
	public void testGetEdmReferentialConstraintRole() {
		try {
			objJPAEdmReferentialConstraintRole.getBuilder().build();
		} catch (ODataJPAModelException e) {
			fail("ODataJPAModelException not expected");
		} catch (ODataJPARuntimeException e) {
			fail("ODataJPARuntimeException not expected");
		}
		assertNotNull(objJPAEdmReferentialConstraintRole
				.getEdmReferentialConstraintRole());
	}

	@Test
	public void testGetJPAColumnName() {
		assertNull(objJPAEdmReferentialConstraintRole.getJPAColumnName());
	}

	@Test
	public void testGetEdmEntityTypeName() {
		assertNull(objJPAEdmReferentialConstraintRole.getEdmEntityTypeName());
	}

	@Test
	public void testGetEdmAssociationName() {
		assertNull(objJPAEdmReferentialConstraintRole.getEdmAssociationName());
	}

	@Override
	public Attribute<?, ?> getJPAAttribute() {
		return getJPAAttributeLocal();
	}

	@Override
	public Association getEdmAssociation() {
		Association association = new Association();
		association.setName("Assoc_SalesOrderHeader_SalesOrderItem");
		association.setEnd1(new AssociationEnd().setType(
				new FullQualifiedName("salesorderprocessing", "String"))
				.setRole("SalesOrderHeader"));
		association.setEnd2(new AssociationEnd()
				.setType(
						new FullQualifiedName("salesorderprocessing",
								"SalesOrderItem")).setRole("SalesOrderItem"));
		return association;
	}

	@Override
	public EntityType searchEdmEntityType(String arg0) {

		EntityType entityType = new EntityType();

		JPAEdmMappingImpl mapping = new JPAEdmMappingImpl();
		mapping.setJPAColumnName("SOID");

		List<Property> propList = new ArrayList<Property>();

		Property property = new Property() {
		};
		property.setMapping((Mapping) mapping);
		property.setName("SOID");
		propList.add(property);

		entityType.setProperties(propList);

		return entityType;
	}

	private Attribute<?, ?> getJPAAttributeLocal() {
		AttributeMock<Object, String> attr = new AttributeMock<Object, String>();
		return attr;
	}

	@SuppressWarnings("hiding")
	private class AttributeMock<Object, String> extends
			JPAAttributeMock<Object, String> {

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
	private class ManagedTypeMock<String> extends JPAManagedTypeMock<String> {

		@SuppressWarnings("unchecked")
		@Override
		public Class<String> getJavaType() {
			return (Class<String>) java.lang.String.class;
		}
	}

	private class JavaMemberMock extends JPAJavaMemberMock {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
			JoinColumn joinColumn = EasyMock.createMock(JoinColumn.class);
			EasyMock.expect(joinColumn.referencedColumnName())
					.andReturn("SOID");
			EasyMock.expect(joinColumn.name()).andReturn("SOID");

			EasyMock.replay(joinColumn);
			return (T) joinColumn;
		}
	}
}
