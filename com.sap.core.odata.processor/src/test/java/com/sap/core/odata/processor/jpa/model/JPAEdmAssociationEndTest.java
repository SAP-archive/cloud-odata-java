package com.sap.core.odata.processor.jpa.model;

import static org.junit.Assert.*;

import javax.persistence.metamodel.Attribute;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.model.mock.JPAAttributeMock;
import com.sap.core.odata.processor.jpa.testdata.JPAEdmMockData.SimpleType;
import com.sap.core.odata.processor.jpa.testdata.JPAEdmMockData.SimpleType.SimpleTypeA;

public class JPAEdmAssociationEndTest extends JPAEdmTestModelView {

	private final static int VARIANT1 = 1;
	private final static int VARIANT2 = 2;
	private final static int VARIANT3 = 3;

	private static int variant;

	private static final String PUNIT_NAME = "salesorderprocessing";
	private static JPAEdmAssociationEnd objJPAEdmAssociationEnd = null;
	private static JPAEdmAssociationEndTest objJPAEdmAssociationEndTest = null;

	

	@BeforeClass
	public static void setup() throws ODataJPAModelException{
		objJPAEdmAssociationEndTest = new JPAEdmAssociationEndTest();
		objJPAEdmAssociationEnd = new JPAEdmAssociationEnd(
				objJPAEdmAssociationEndTest, objJPAEdmAssociationEndTest);
		objJPAEdmAssociationEnd.getBuilder().build();
	}

	@Test
	public void testGetBuilder() {
		JPAEdmBuilder builder = objJPAEdmAssociationEnd.getBuilder();
		assertNotNull(builder);

	}

	@Test
	public void testGetAssociationEnd1() {
		AssociationEnd associationEnd = objJPAEdmAssociationEnd
				.getAssociationEnd1();
		assertEquals(associationEnd.getType().getName(), "SOID");
	}

	@Test
	public void testGetAssociationEnd2() {
		AssociationEnd associationEnd = objJPAEdmAssociationEnd
				.getAssociationEnd2();
		assertEquals(associationEnd.getType().getName(), "String");
	}

	@Test
	public void testCompare() throws ODataJPAModelException {
		assertTrue(objJPAEdmAssociationEnd.compare(
				getAssociationEnd("SOID", 1), getAssociationEnd("String", 1)));
		assertFalse(objJPAEdmAssociationEnd.compare(
				getAssociationEnd("String", 2), getAssociationEnd("SOID", 1)));
	}
	
	@Test
	public void testBuildAssociationEnd() 
	{
		assertEquals("SOID",objJPAEdmAssociationEnd.getAssociationEnd1().getType().getName());
		assertEquals(new FullQualifiedName("salesorderprocessing", "SOID"), objJPAEdmAssociationEnd.getAssociationEnd1().getType());
		assertTrue(objJPAEdmAssociationEnd.isConsistent());
		
	}

	private AssociationEnd getAssociationEnd(String typeName, int variant) {
		AssociationEnd associationEnd = new AssociationEnd();
		associationEnd.setType(getFullQualifiedName(typeName));
		if (variant == VARIANT1)
			associationEnd.setMultiplicity(EdmMultiplicity.MANY);
		else if (variant == VARIANT2)
			associationEnd.setMultiplicity(EdmMultiplicity.ONE);
		else if (variant == VARIANT3)
			associationEnd.setMultiplicity(EdmMultiplicity.ZERO_TO_ONE);
		else
			associationEnd.setMultiplicity(EdmMultiplicity.MANY);//
		return associationEnd;
	}

	private FullQualifiedName getFullQualifiedName(String typeName) {
		FullQualifiedName fullQualifiedName = new FullQualifiedName(PUNIT_NAME,
				typeName);
		return fullQualifiedName;
	}

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
		return PUNIT_NAME;
	}

	@Override
	public EntityType getEdmEntityType() {
		EntityType entityType = new EntityType();
		entityType.setName(SimpleTypeA.NAME);
		return entityType;
	}

	// The inner class which gives us an replica of the jpa attribute
	@SuppressWarnings("hiding")
	private class AttributeMock<Object, String> extends JPAAttributeMock<Object, String> {

		@SuppressWarnings("unchecked")
		@Override
		public Class<String> getJavaType() {
			return (Class<String>) SimpleType.SimpleTypeA.clazz;
		}

		/*@Override
		public java.lang.String getName() {
			return SimpleType.SimpleTypeA.NAME;
		}*/

		@Override
		public PersistentAttributeType getPersistentAttributeType() {
			if (variant == VARIANT1)
				return PersistentAttributeType.ONE_TO_MANY;
			else if (variant == VARIANT2)
				return PersistentAttributeType.ONE_TO_ONE;
			else if (variant == VARIANT3)
				return PersistentAttributeType.MANY_TO_ONE;
			else
				return PersistentAttributeType.MANY_TO_MANY;
		
		}

		
	}

}
