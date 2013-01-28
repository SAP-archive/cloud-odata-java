package com.sap.core.odata.processor.jpa.model;

import static org.junit.Assert.*;

import java.lang.reflect.Member;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public class JPAEdmAssociationEndTest extends JPAEdmTestModelView{
	
	private final static int VARIANT1 = 1;
	private final static int VARIANT2 = 2;
	private final static int VARIANT3 = 3;
	
	private static int variant;
	
	private static final String PUNIT_NAME = "salesorderprocessing";
	//private static JPAEdmBuilderV2 builder;
	//private static Metamodel metaModel;
	
	static JPAEdmAssociationEnd objJPAEdmAssociationEnd = null;
	static JPAEdmAssociationEndTest objJPAEdmAssociationEndTest = null;

	@BeforeClass
	public static void setup() throws Exception {
		objJPAEdmAssociationEndTest = new JPAEdmAssociationEndTest();
		EntityManagerFactory emf = null;
		try {
			emf = Persistence.createEntityManagerFactory(PUNIT_NAME);
			//metaModel = emf.getMetamodel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (emf != null) {
			//builder = new JPAEdmBuilderV2(PUNIT_NAME, emf);
			objJPAEdmAssociationEnd = new JPAEdmAssociationEnd(objJPAEdmAssociationEndTest, objJPAEdmAssociationEndTest);
			objJPAEdmAssociationEnd.getBuilder().build();
		}
	}


	@Test
	public void testGetBuilder() {
		JPAEdmBuilder builder = objJPAEdmAssociationEnd.getBuilder();
		assertNotNull(builder);

	}

	@Test
	public void testGetAssociationEnd1() {
		AssociationEnd associationEnd = objJPAEdmAssociationEnd.getAssociationEnd1();
		assertEquals(associationEnd.getType().getName(), "Object");
	}

	@Test
	public void testGetAssociationEnd2() {
		AssociationEnd associationEnd = objJPAEdmAssociationEnd.getAssociationEnd2();
		assertEquals(associationEnd.getType().getName(), "Object");
	}

	@Test
	public void testCompare() throws ODataJPAModelException {
		assertTrue(objJPAEdmAssociationEnd.compare(getAssociationEnd("Object", 1), getAssociationEnd("Object", 1)));
		assertFalse(objJPAEdmAssociationEnd.compare(getAssociationEnd("Object", 1), getAssociationEnd("Object", 2)));
	}

	private AssociationEnd getAssociationEnd(String typeName, int variant) {
		AssociationEnd associationEnd = new AssociationEnd();
		associationEnd.setType(getFullQualifiedName(typeName));
		if(variant == VARIANT1)
			associationEnd.setMultiplicity(EdmMultiplicity.MANY);
		else if(variant == VARIANT2)
			associationEnd.setMultiplicity(EdmMultiplicity.ONE);
		else if(variant == VARIANT3)
			associationEnd.setMultiplicity(EdmMultiplicity.ZERO_TO_ONE);
		else
			associationEnd.setMultiplicity(EdmMultiplicity.MANY);//
		return associationEnd;
	}


	private FullQualifiedName getFullQualifiedName(String typeName) {
		FullQualifiedName fullQualifiedName = new FullQualifiedName(PUNIT_NAME, typeName);
		return fullQualifiedName;
	}
		
	private Attribute<?,?> getJPAAttributeLocal() {
		AttributeMock<?, ?> attr = new AttributeMock<>();
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
		entityType.setName("Object");
		return entityType;
	}

	private static class AttributeMock<X,Y> implements Attribute<X, Y>{
		
		@Override
		public ManagedType<X> getDeclaringType() {
			return null;
		}
		
		@Override
		public Member getJavaMember() {
			return null;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Class<Y> getJavaType() {
			return (Class<Y>) Object.class;
		}
		
		@Override
		public String getName() {
			return null;
		}
		
		@Override
		public javax.persistence.metamodel.Attribute.PersistentAttributeType getPersistentAttributeType() {
			if(variant == VARIANT1)
				return PersistentAttributeType.ONE_TO_MANY;
			else if (variant == VARIANT2)
				return PersistentAttributeType.ONE_TO_ONE;
			else if (variant == VARIANT3)
				return PersistentAttributeType.MANY_TO_ONE;
			else
				return PersistentAttributeType.MANY_TO_MANY;
		}
		
		@Override
		public boolean isAssociation() {
			return false;
		}
		
		@Override
		public boolean isCollection() {
			return false;
		}
		
	}
	
	
}
