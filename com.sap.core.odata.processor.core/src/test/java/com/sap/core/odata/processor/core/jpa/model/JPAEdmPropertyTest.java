package com.sap.core.odata.processor.core.jpa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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

import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmProperty;
import com.sap.core.odata.processor.core.jpa.model.mock.JPAEmbeddableTypeMock;
import com.sap.core.odata.processor.core.jpa.model.mock.JPAEntityTypeMock;
import com.sap.core.odata.processor.core.jpa.model.mock.JPAMetaModelMock;
import com.sap.core.odata.processor.core.jpa.model.mock.JPASingularAttributeMock;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityTypeView;

public class JPAEdmPropertyTest extends JPAEdmTestModelView {

	private JPAEdmPropertyTest objJPAEdmPropertyTest;
	private JPAEdmProperty objJPAEdmProperty;

	@Before
	public void setUp() throws Exception {
		objJPAEdmPropertyTest = new JPAEdmPropertyTest();
		objJPAEdmProperty = new JPAEdmProperty(objJPAEdmPropertyTest);
		objJPAEdmProperty.getBuilder().build();

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
			fail("ODataJPAModelException not expected");
		} catch (ODataJPARuntimeException e) {
			fail("ODataJPARuntimeException not expected");
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
		// assertNotNull(objJPAEdmProperty.getEdmComplexProperty());
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
	public Schema getEdmSchema() {
		Schema schema = new Schema();
		schema.setNamespace("salesordereprocessing");
		return schema;
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
	public JPAEdmBuilder getBuilder() {
		return new JPAEdmBuilder() {

			@Override
			public void build() throws ODataJPAModelException {
				// Nothing to do?
			}
		};
	}

	private class JPAEdmMetaModel extends JPAMetaModelMock {
		Set<EntityType<?>> entities;

		public JPAEdmMetaModel() {
			entities = new HashSet<EntityType<?>>();
		}

		@Override
		public Set<EntityType<?>> getEntities() {
			entities.add(new JPAEdmEntityType());
			return entities;
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
				return PersistentAttributeType.BASIC;
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

		/*
		 * @Override public Attribute<? super Object, ?>
		 * getAttribute(java.lang.String arg0) { return
		 * super.getAttribute(arg0); }
		 */

		@Override
		public Set<Attribute<? super String, ?>> getAttributes() {
			setValuesToSet();
			return attributeSet;
		}

		private class JPAEdmAttribute<Object, String> extends
				JPASingularAttributeMock<Object, String> {

			@Override
			public PersistentAttributeType getPersistentAttributeType() {
				return PersistentAttributeType.BASIC;
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
