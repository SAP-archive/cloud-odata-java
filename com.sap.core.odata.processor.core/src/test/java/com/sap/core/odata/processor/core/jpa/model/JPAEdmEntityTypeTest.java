package com.sap.core.odata.processor.core.jpa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmEntityType;
import com.sap.core.odata.processor.core.jpa.model.mock.JPAEntityTypeMock;
import com.sap.core.odata.processor.core.jpa.model.mock.JPAMetaModelMock;
import com.sap.core.odata.processor.core.jpa.model.mock.JPASingularAttributeMock;

public class JPAEdmEntityTypeTest extends JPAEdmTestModelView{

	private static JPAEdmEntityTypeTest objJPAEdmEntityTypeTest;
	private static JPAEdmEntityType objJPAEdmEntityType;
	
	@Before
	public void setUp() throws Exception {
		objJPAEdmEntityTypeTest = new JPAEdmEntityTypeTest();
		objJPAEdmEntityType = new JPAEdmEntityType(objJPAEdmEntityTypeTest);
		objJPAEdmEntityType.getBuilder().build();
	}

	@Test
	public void testGetBuilder() {
		assertNotNull(objJPAEdmEntityType.getBuilder());
	}
	
	@Test
	public void testGetBuilderIdempotent(){
		JPAEdmBuilder builder1 = objJPAEdmEntityType.getBuilder();
		JPAEdmBuilder builder2 = objJPAEdmEntityType.getBuilder();
		
		assertEquals(builder1.hashCode(), builder2.hashCode());
	}

	@Test
	public void testGetEdmEntityType() {
		assertNotNull(objJPAEdmEntityType.getEdmEntityType());
		assertNotNull(objJPAEdmEntityType.getEdmEntityType().getKey());
	}

	@Test
	public void testGetJPAEntityType() {
		assertNotNull(objJPAEdmEntityType.getJPAEntityType());
		
	}

	@Test
	public void testGetConsistentEdmEntityTypes() {
		assertTrue(objJPAEdmEntityType.getConsistentEdmEntityTypes().size() > 0);
	}

	@Test
	public void testSearchEdmEntityType() {
		assertNotNull(objJPAEdmEntityType.searchEdmEntityType("SalesOrderHeader"));
	}

	@Test
	public void testIsConsistent() {
		assertTrue(objJPAEdmEntityType.isConsistent());
	}

	@Override
	public Metamodel getJPAMetaModel() {
		return new JPAEdmMetaModel();
	}
	
	@Override
	public JPAEdmEntitySetView getJPAEdmEntitySetView(){
		return this;
	}
	
	@Override
	public JPAEdmEntityContainerView getJPAEdmEntityContainerView() {
		return this;
	}
	
	@Override
	public EntityType<?> getJPAEntityType() {
		return new JPAEdmEntityTypeLocal<String>();
	}
	
	@Override
	public JPAEdmEntityTypeView getJPAEdmEntityTypeView(){
		return this;
	}
	
	
	private class JPAEdmMetaModel extends JPAMetaModelMock
	{
		Set<EntityType<?>> entities;

		public JPAEdmMetaModel(){
			entities = new HashSet<EntityType<?>>();
		}

		@Override
		public Set<EntityType<?>> getEntities() {
			entities.add(new JPAEdmEntityType());
			return entities;
		}
		
		private class JPAEdmEntityType extends JPAEntityTypeMock<String>{
			@Override
			public String getName() {
				return "SalesOrderHeader";
			}
		}
	}
	
	
	@SuppressWarnings("hiding")
	private class JPAEdmEntityTypeLocal<String> extends JPAEntityTypeMock<String>{
		Set<Attribute<? super String, ?>> attributeSet = new HashSet<Attribute<? super String,?>>();
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void setValuesToSet()
		{
			attributeSet.add((Attribute< ? super String,String>)new JPAEdmAttribute(java.lang.String.class, "SOID"));
			attributeSet.add((Attribute< ? super String,String>)new JPAEdmAttribute(java.lang.String.class, "SONAME"));
		}

		@Override
		public Set<Attribute<? super String, ?>> getAttributes() {
			setValuesToSet();
			return attributeSet;
		}
		
		private class JPAEdmAttribute<Object,String> extends JPASingularAttributeMock<Object, String>
		{

			@Override
			public PersistentAttributeType getPersistentAttributeType() {
				return PersistentAttributeType.BASIC;
			}

			Class<String> clazz;
			java.lang.String attributeName;
			public JPAEdmAttribute(Class<String> javaType,java.lang.String name) {
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
