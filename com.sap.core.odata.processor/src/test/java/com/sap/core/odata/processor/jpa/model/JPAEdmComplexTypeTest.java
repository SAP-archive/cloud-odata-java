package com.sap.core.odata.processor.jpa.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.Metamodel;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.model.mock.JPAEmbeddableMock;
import com.sap.core.odata.processor.jpa.model.mock.JPAMetaModelMock;
import com.sap.core.odata.processor.jpa.model.mock.JPASingularAttributeMock;
import com.sap.core.odata.processor.jpa.testdata.JPAEdmMockData.SimpleType;

public class JPAEdmComplexTypeTest extends JPAEdmTestModelView {
	
	private static JPAEdmComplexType objComplexType = null;
	
	@BeforeClass
	public static void setup() throws ODataJPAModelException{
		JPAEdmComplexTypeTest localView = new JPAEdmComplexTypeTest();
		objComplexType = new JPAEdmComplexType(localView);
		objComplexType.getBuilder().build();
	}

	@Override
	public String getpUnitName() {
		return "salesorderprocessing";
	}

	@Override
	public Metamodel getJPAMetaModel() {
		return new JPAEdmMetaModel();
	}

	

	@Test
	public void testGetBuilder() {
		
		assertNotNull(objComplexType.getBuilder());
	}

	@Test
	public void testGetEdmComplexType() {
		assertEquals(objComplexType.getEdmComplexType().getName(), "String");
	}

	@Test
	public void testSearchComplexTypeString() {
		assertNotNull(objComplexType.searchComplexType("java.lang.String"));
		
	}

	@Test
	public void testGetJPAEmbeddableType() {
		assertTrue(objComplexType.getJPAEmbeddableType().getAttributes().size() > 0);
		
	}

	@Test
	public void testGetConsistentEdmComplexTypes() {
		assertEquals(objComplexType.getConsistentEdmComplexTypes().size(), 1);
	}

	@Test
	public void testSearchComplexTypeFullQualifiedName() {
		assertNotNull(objComplexType.searchComplexType(new FullQualifiedName("salesorderprocessing", "String")));
		
	}
	@Test
	public void testSearchComplexTypeFullQualifiedNameNegative()
	{
		assertNull(objComplexType.searchComplexType(new FullQualifiedName("salesorderprocessing", "lang.String")));
	}

	@Test
	public void testAddCompleTypeView() {
		
	}

	@Test
	public void testExpandEdmComplexType() {
		ComplexType complexType = new ComplexType();
		List<Property> properties = new ArrayList<Property>();
		properties.add(new SimpleProperty().setName("LIID"));
		properties.add(new SimpleProperty().setName("LINAME"));
		complexType.setProperties(properties );
		List<Property> expandedList = null;
		try
		{
			objComplexType.expandEdmComplexType(complexType, expandedList);
		}
		catch(ClassCastException e)
		{
			assertTrue(false);
		}
		assertTrue(true);
		
		
	}
	
	@Test
	public void testComplexTypeCreation() throws ODataJPAModelException
	{
		objComplexType.getBuilder().build();
		assertEquals(objComplexType.pUnitName, "salesorderprocessing");
	}
	
	private class JPAEdmMetaModel extends JPAMetaModelMock
	{
		Set<EmbeddableType<?>> embeddableSet;
		
		public JPAEdmMetaModel() {
			embeddableSet = new HashSet<EmbeddableType<?>>();
		}
		
		@Override
		public Set<EmbeddableType<?>> getEmbeddables() {
			embeddableSet.add(new JPAEdmEmbeddable<String>());
			return embeddableSet;
		}
		
	}
	
	private class JPAEdmEmbeddable<String> extends JPAEmbeddableMock<String>
	{
 
		Set<Attribute<? super String, ?>> attributeSet = new HashSet<Attribute<? super String,?>>();
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
		@Override
		public Class<String> getJavaType() {
			return (Class<String>)java.lang.String.class;
		}
		
		
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
			return false;
		}
		
		
	}

}
