package com.sap.core.odata.processor.jpa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.Attribute;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.Key;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmKeyView;
import com.sap.core.odata.processor.jpa.model.mock.JPAAttributeMock;
import com.sap.core.odata.processor.jpa.testdata.JPAEdmMockData;
import com.sap.core.odata.processor.jpa.testdata.JPAEdmMockData.ComplexType.ComplexTypeA;
import com.sap.core.odata.processor.jpa.testdata.JPAEdmTestModelView;
import com.sap.core.odata.processor.jpa.util.MockData;

public class JPAEdmKeyTest extends JPAEdmTestModelView {

	@SuppressWarnings("hiding")
	private class JPAAttributeA<Object, ComplexTypeA> extends
			JPAAttributeMock<Object, ComplexTypeA> {
		@SuppressWarnings("unchecked")
		@Override
		public Class<ComplexTypeA> getJavaType() {
			return (Class<ComplexTypeA>) JPAEdmMockData.ComplexType.ComplexTypeA.class;
		}
	}

	@Test
	public void testBuildComplexKey() {
		JPAEdmKeyView keyView = new JPAEdmKey(this, this);

		try {
			keyView.getBuilder().build();
		} catch (ODataJPAModelException e) {
			fail("Exception Not Expected");
		}

		Key key = keyView.getEdmKey();

		assertEquals(
				JPAEdmMockData.ComplexType.ComplexTypeA.Property.PROPERTY_A,
				key.getKeys().get(0).getName());
		assertEquals(
				JPAEdmMockData.ComplexType.ComplexTypeA.Property.PROPERTY_B,
				key.getKeys().get(1).getName());
		assertEquals(
				JPAEdmMockData.ComplexType.ComplexTypeB.Property.PROPERTY_D,
				key.getKeys().get(2).getName());
		assertEquals(
				JPAEdmMockData.ComplexType.ComplexTypeB.Property.PROPERTY_E,
				key.getKeys().get(3).getName());
		
	}

	@Override
	public Attribute<?, ?> getJPAAttribute() {
		return new JPAAttributeA<Object, ComplexTypeA>();

	}

	@Override
	public ComplexType searchEdmComplexType(FullQualifiedName arg0) {
		return searchEdmComplexType(arg0.getName());
	}

	@Override
	public ComplexType searchEdmComplexType(String arg0) {
		if (arg0.equals(JPAEdmMockData.ComplexType.ComplexTypeA.class.getName()))
			return buildComplexTypeA();
		else if (arg0.equals(JPAEdmMockData.ComplexType.ComplexTypeB.class
				.getSimpleName()))
			return buildComplexTypeB();

		return null;

	}

	private ComplexType buildComplexTypeB() {
		ComplexType complexType = new ComplexType();
		complexType.setProperties(buildPropertiesB());

		return complexType;
	}

	private List<Property> buildPropertiesB() {
		List<Property> propertyList = new ArrayList<Property>();

		SimpleProperty property = new SimpleProperty();
		property.setName(JPAEdmMockData.ComplexType.ComplexTypeB.Property.PROPERTY_D);
		property.setType(EdmSimpleTypeKind.Int16);

		propertyList.add(property);

		property = new SimpleProperty();
		property.setName(JPAEdmMockData.ComplexType.ComplexTypeB.Property.PROPERTY_E);
		property.setType(EdmSimpleTypeKind.Int16);
		
		propertyList.add(property);

		return propertyList;
	}

	private ComplexType buildComplexTypeA() {
		ComplexType complexType = new ComplexType();
		complexType.setProperties(buildPropertiesA());

		return complexType;
	}

	private List<Property> buildPropertiesA() {

		List<Property> propertyList = new ArrayList<Property>();

		SimpleProperty property = new SimpleProperty();
		property.setName(JPAEdmMockData.ComplexType.ComplexTypeA.Property.PROPERTY_A);
		property.setType(EdmSimpleTypeKind.Int16);

		propertyList.add(property);

		property = new SimpleProperty();
		property.setName(JPAEdmMockData.ComplexType.ComplexTypeA.Property.PROPERTY_B);
		property.setType(EdmSimpleTypeKind.Int16);
		
		propertyList.add(property);

		ComplexProperty complexProperty = new ComplexProperty();
		complexProperty
				.setName(JPAEdmMockData.ComplexType.ComplexTypeA.Property.PROPERTY_C);
		complexProperty.setType(new FullQualifiedName(MockData.NAME_SPACE,
				JPAEdmMockData.ComplexType.ComplexTypeB.name));

		propertyList.add(complexProperty);
		return propertyList;

	}

}
