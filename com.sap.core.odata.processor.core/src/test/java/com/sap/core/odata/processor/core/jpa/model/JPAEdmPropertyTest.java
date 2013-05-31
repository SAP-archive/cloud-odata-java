package com.sap.core.odata.processor.core.jpa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;
import com.sap.core.odata.processor.core.jpa.mock.model.JPAEmbeddableTypeMock;
import com.sap.core.odata.processor.core.jpa.mock.model.JPAEntityTypeMock;
import com.sap.core.odata.processor.core.jpa.mock.model.JPAMetaModelMock;
import com.sap.core.odata.processor.core.jpa.mock.model.JPASingularAttributeMock;
import com.sap.core.odata.processor.core.jpa.mock.model.JPAEdmMockData.ComplexType;
import com.sap.core.odata.processor.core.jpa.mock.model.JPAEdmMockData.SimpleType;

public class JPAEdmPropertyTest extends JPAEdmTestModelView {

	private JPAEdmPropertyTest objJPAEdmPropertyTest;
	private JPAEdmProperty objJPAEdmProperty;
	
	private static int ATTRIBUTE_TYPE = 1; 

	@Before
	public void setUp() {
		objJPAEdmPropertyTest = new JPAEdmPropertyTest();
		objJPAEdmProperty = new JPAEdmProperty(objJPAEdmPropertyTest);
		try {
			objJPAEdmProperty.getBuilder().build();
		} catch (ODataJPAModelException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}

	}

	@Test
	public void testGetBuilder() {
		assertNotNull(objJPAEdmProperty.getBuilder());

		// builder for complex type
		objJPAEdmPropertyTest = new JPAEdmPropertyTest();
		objJPAEdmProperty = new JPAEdmProperty(objJPAEdmPropertyTest,
				objJPAEdmPropertyTest);
		try {
			objJPAEdmProperty.getBuilder().build();
		} catch (ODataJPAModelException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
	}

	@Test
	public void testGetBuilderIdempotent() {
		JPAEdmBuilder builder1 = objJPAEdmProperty.getBuilder();
		JPAEdmBuilder builder2 = objJPAEdmProperty.getBuilder();

		assertEquals(builder1.hashCode(), builder2.hashCode());
	}

	@Test
	public void testGetPropertyList() {
		assertNotNull(objJPAEdmProperty.getEdmPropertyList());
		assertTrue(objJPAEdmProperty.getEdmPropertyList().size() > 0);
	}

	@Test
	public void testGetJPAEdmKeyView() {
		assertNotNull(objJPAEdmProperty.getJPAEdmKeyView());
	}

	@Test
	public void testGetSimpleProperty() {
		assertNotNull(objJPAEdmProperty.getEdmSimpleProperty());
	}

	@Test
	public void testGetJPAAttribute() {
		assertNotNull(objJPAEdmProperty.getJPAAttribute());
	}

	@Test
	public void testGetEdmComplexProperty() {
		assertNull(objJPAEdmProperty.getEdmComplexProperty());
	}

	@Test
	public void testGetJPAEdmNavigationPropertyView() {
		assertNotNull(objJPAEdmProperty.getJPAEdmNavigationPropertyView());
	}

	@Test
	public void testIsConsistent() {
		assertNotNull(objJPAEdmProperty.isConsistent());
	}

	@Test
	public void testClean() {
		objJPAEdmProperty.clean();
		assertFalse(objJPAEdmProperty.isConsistent());
	}
	
	@Test
	public void testBuildManyToOne() {
		ATTRIBUTE_TYPE = 3;
		objJPAEdmPropertyTest = new JPAEdmPropertyTest();
		objJPAEdmProperty = new JPAEdmProperty(objJPAEdmPropertyTest);
		try {
			objJPAEdmProperty.getBuilder().build();
		} catch (ODataJPAModelException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		
		ATTRIBUTE_TYPE = 1;
		objJPAEdmPropertyTest = new JPAEdmPropertyTest();
		objJPAEdmProperty = new JPAEdmProperty(objJPAEdmPropertyTest);
		try {
			objJPAEdmProperty.getBuilder().build();
		} catch (ODataJPAModelException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		
	}

	@Override
	public Metamodel getJPAMetaModel() {
		return new JPAEdmMetaModel();
	}

	@Override
	public JPAEdmEntitySetView getJPAEdmEntitySetView() {
		return this;
	}

	@Override
	public JPAEdmEntityContainerView getJPAEdmEntityContainerView() {
		return this;
	}

	@Override
	public EntityType<?> getJPAEntityType() {
		return new JPAEdmEntityType<String>();
	}

	@Override
	public JPAEdmEntityTypeView getJPAEdmEntityTypeView() {
		return this;
	}
	

	@Override
	public com.sap.core.odata.api.edm.provider.EntityType getEdmEntityType() {
		/*EntityType entityType = new EntityType();
		entityType.setName("SalesOrderHeader");
		return entityType;*/
		com.sap.core.odata.api.edm.provider.EntityType entityType = new com.sap.core.odata.api.edm.provider.EntityType();
		entityType.setName("SalesOrderHeader");
		
		return entityType;
	}
	
	@Override
	public Association getEdmAssociation() {
		Association association = new Association();
		association.setEnd1(new AssociationEnd().setType(new FullQualifiedName(
				"salesorderprocessing", "SalesOrderHeader")));
		association.setEnd2(new AssociationEnd().setType(new FullQualifiedName(
				"salesorderprocessing", "String")));

		return association;
	}

	@Override
	public String getpUnitName() {
		return "salesorderprocessing";
	}

	@Override
	public JPAEdmAssociationView getJPAEdmAssociationView() {
		return this;
	}

	@Override
	public EmbeddableType<?> getJPAEmbeddableType() {
		return new JPAEdmEmbeddable<java.lang.String>();
	}
	
	@Override
	public JPAEdmComplexTypeView getJPAEdmComplexTypeView() {
		return this;
	}


	private class JPAEdmMetaModel extends JPAMetaModelMock {
		Set<EntityType<?>> entities;
		Set<EmbeddableType<?>> embeddableSet;

		public JPAEdmMetaModel() {
			entities = new HashSet<EntityType<?>>();
			embeddableSet = new HashSet<EmbeddableType<?>>();
		}

		@Override
		public Set<EntityType<?>> getEntities() {
			entities.add(new JPAEdmEntityType());
			return entities;
		}

		@Override
		public Set<EmbeddableType<?>> getEmbeddables() {
			embeddableSet.add(new JPAEdmEmbeddable<String>());
			return embeddableSet;
		}

		private class JPAEdmEntityType extends JPAEntityTypeMock<String> {
			@Override
			public String getName() {
				return "SalesOrderHeader";
			}
		}
	}

	@SuppressWarnings("hiding")
	private class JPAEdmEntityType<String> extends JPAEntityTypeMock<String> {
		Set<Attribute<? super String, ?>> attributeSet = new HashSet<Attribute<? super String, ?>>();

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void setValuesToSet() {
			attributeSet
					.add((Attribute<? super String, String>) new JPAEdmAttribute(
							java.lang.String.class, "SOID"));
			attributeSet
					.add((Attribute<? super String, String>) new JPAEdmAttribute(
							java.lang.String.class, "SONAME"));
		}

		@Override
		public Set<Attribute<? super String, ?>> getAttributes() {
			setValuesToSet();
			return attributeSet;
		}

		private class JPAEdmAttribute<Object, String> extends
				JPASingularAttributeMock<Object, String> {

			@Override
			public PersistentAttributeType getPersistentAttributeType() {
				if(ATTRIBUTE_TYPE == 1)
					return PersistentAttributeType.BASIC;
				else if(ATTRIBUTE_TYPE == 2)
					return PersistentAttributeType.EMBEDDED;
				else
					return PersistentAttributeType.MANY_TO_ONE;
			}

			Class<String> clazz;
			java.lang.String attributeName;

			public JPAEdmAttribute(Class<String> javaType, java.lang.String name) {
				this.clazz = javaType;
				this.attributeName = name;

			}

			@Override
			public Class<String> getJavaType() {
				return clazz;
			}

			@Override
			public java.lang.String getName() {
				return this.attributeName;
			}

			@Override
			public boolean isId() {
				return true;
			}
		}
	}

	@SuppressWarnings("hiding")
	private class JPAEdmEmbeddable<String> extends
			JPAEmbeddableTypeMock<String> {

		Set<Attribute<? super String, ?>> attributeSet = new HashSet<Attribute<? super String, ?>>();

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void setValuesToSet() {
			attributeSet
					.add((Attribute<? super String, String>) new JPAEdmAttribute(
							java.lang.String.class, "SOID"));
			attributeSet
					.add((Attribute<? super String, String>) new JPAEdmAttribute(
							java.lang.String.class, "SONAME"));
		}

		@Override
		public Set<Attribute<? super String, ?>> getAttributes() {
			setValuesToSet();
			return attributeSet;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Class<String> getJavaType() {
			Class<?> clazz = null;
			if(ATTRIBUTE_TYPE == 1)
				clazz = (Class<java.lang.String>) SimpleType.SimpleTypeA.clazz;
			else
				clazz = (Class<?>) ComplexType.ComplexTypeA.clazz;
			return (Class<String>) clazz;
		}

		private class JPAEdmAttribute<Object, String> extends
				JPASingularAttributeMock<Object, String> {

			@Override
			public PersistentAttributeType getPersistentAttributeType() {
				if(ATTRIBUTE_TYPE == 1)
					return PersistentAttributeType.BASIC;
				else if(ATTRIBUTE_TYPE == 2)
					return PersistentAttributeType.EMBEDDED;
				else
					return PersistentAttributeType.MANY_TO_ONE;
			}

			Class<String> clazz;
			java.lang.String attributeName;

			public JPAEdmAttribute(Class<String> javaType, java.lang.String name) {
				this.clazz = javaType;
				this.attributeName = name;

			}

			@Override
			public Class<String> getJavaType() {
				return clazz;
			}

			@Override
			public java.lang.String getName() {
				return this.attributeName;
			}

			@Override
			public boolean isId() {
				return true;
			}
		}

	}

}
