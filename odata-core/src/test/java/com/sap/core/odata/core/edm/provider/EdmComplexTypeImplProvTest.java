/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class EdmComplexTypeImplProvTest extends BaseTest {
  private static EdmComplexTypeImplProv edmComplexType;
  private static EdmComplexTypeImplProv edmComplexTypeWithBaseType;
  private static EdmProvider edmProvider;

  @BeforeClass
  public static void getEdmEntityContainerImpl() throws Exception {

    edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    ComplexType fooComplexType = new ComplexType().setName("fooComplexType");

    List<Property> keyPropertysFoo = new ArrayList<Property>();
    keyPropertysFoo.add(new SimpleProperty().setName("Name").setType(EdmSimpleTypeKind.String));
    keyPropertysFoo.add(new SimpleProperty().setName("Address").setType(EdmSimpleTypeKind.String));
    fooComplexType.setProperties(keyPropertysFoo);

    edmComplexType = new EdmComplexTypeImplProv(edmImplProv, fooComplexType, "namespace");

    FullQualifiedName barBaseTypeName = new FullQualifiedName("namespace", "barBase");
    ComplexType barBase = new ComplexType().setName("barBase");
    when(edmProvider.getComplexType(barBaseTypeName)).thenReturn(barBase);

    List<Property> propertysBarBase = new ArrayList<Property>();
    propertysBarBase.add(new SimpleProperty().setName("Name").setType(EdmSimpleTypeKind.String));
    propertysBarBase.add(new SimpleProperty().setName("Address").setType(EdmSimpleTypeKind.String));
    barBase.setProperties(propertysBarBase);

    ComplexType barComplexType = new ComplexType().setName("barComplexType").setBaseType(barBaseTypeName);
    edmComplexTypeWithBaseType = new EdmComplexTypeImplProv(edmImplProv, barComplexType, "namespace");

  }

  @Test
  public void getPropertiesNames() throws Exception {
    List<String> properties = edmComplexType.getPropertyNames();
    assertNotNull(properties);
    assertTrue(properties.contains("Name"));
    assertTrue(properties.contains("Address"));
  }

  @Test
  public void getPropertiesWithBaseType() throws Exception {
    List<String> properties = edmComplexTypeWithBaseType.getPropertyNames();
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
    EdmAnnotatable annotatable = edmComplexType;
    EdmAnnotations annotations = annotatable.getAnnotations();
    assertNull(annotations.getAnnotationAttributes());
    assertNull(annotations.getAnnotationElements());
  }
}
