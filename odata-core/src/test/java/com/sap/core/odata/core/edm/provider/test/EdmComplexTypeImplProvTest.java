package com.sap.core.odata.core.edm.provider.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmAnnotatable;
import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.core.edm.provider.EdmComplexTypeImplProv;
import com.sap.core.odata.core.edm.provider.EdmImplProv;

public class EdmComplexTypeImplProvTest {
  private static EdmComplexTypeImplProv edmComplexType;
  private static EdmComplexTypeImplProv edmComplexTypeWithBaseType;
  private static EdmProvider edmProvider;

  @BeforeClass
  public static void getEdmEntityContainerImpl() throws Exception {

    edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    ComplexType fooComplexType = new ComplexType().setName("fooComplexType");

    Collection<Property> keyPropertysFoo = new ArrayList<Property>();
    keyPropertysFoo.add(new Property().setName("Name").setType(EdmSimpleTypeKind.String.getFullQualifiedName()));
    keyPropertysFoo.add(new Property().setName("Address").setType(EdmSimpleTypeKind.String.getFullQualifiedName()));
    fooComplexType.setProperties(keyPropertysFoo);

    edmComplexType = new EdmComplexTypeImplProv(edmImplProv, fooComplexType, "namespace");

    FullQualifiedName barBaseTypeName = new FullQualifiedName("namespace", "barBase");
    ComplexType barBase = new ComplexType().setName("barBase");
    when(edmProvider.getComplexType(barBaseTypeName)).thenReturn(barBase);

    Collection<Property> propertysBarBase = new ArrayList<Property>();
    propertysBarBase.add(new Property().setName("Name").setType(EdmSimpleTypeKind.String.getFullQualifiedName()));
    propertysBarBase.add(new Property().setName("Address").setType(EdmSimpleTypeKind.String.getFullQualifiedName()));
    barBase.setProperties(propertysBarBase);

    ComplexType barComplexType = new ComplexType().setName("barComplexType").setBaseType(barBaseTypeName);
    edmComplexTypeWithBaseType = new EdmComplexTypeImplProv(edmImplProv, barComplexType, "namespace");

  }

  @Test
  public void getPropertiesNames() throws Exception {
    Collection<String> properties = edmComplexType.getPropertyNames();
    assertNotNull(properties);
    assertTrue(properties.contains("Name"));
    assertTrue(properties.contains("Address"));
  }

  @Test
  public void getPropertiesWithBaseType() throws Exception {
    Collection<String> properties = edmComplexTypeWithBaseType.getPropertyNames();
    assertNotNull(properties);
    assertTrue(properties.contains("Name"));
    assertTrue(properties.contains("Address"));
  }

  @Test
  public void getBaseType() throws Exception {
    EdmComplexType baseType = edmComplexTypeWithBaseType.getBaseType();
    assertNotNull(baseType);
    assertEquals("barBase", baseType.getName());
    assertEquals("namespace", baseType.getNamespace());
  }

  @Test
  public void getProperty() throws Exception {
    EdmTyped property = edmComplexType.getProperty("Name");
    assertNotNull(property);
    assertEquals("Name", property.getName());
  }

  @Test
  public void getAnnotations() throws Exception {
    EdmAnnotatable annotatable = (EdmAnnotatable) edmComplexType;
    EdmAnnotations annotations = annotatable.getAnnotations();
    assertNull(annotations.getAnnotationAttributes());
    assertNull(annotations.getAnnotationElements());

  }
}
