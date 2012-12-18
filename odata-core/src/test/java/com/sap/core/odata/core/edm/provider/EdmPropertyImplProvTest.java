package com.sap.core.odata.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmAnnotatable;
import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.CustomizableFeedMappings;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.Facets;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;
import com.sap.core.odata.core.edm.provider.EdmComplexPropertyImplProv;
import com.sap.core.odata.core.edm.provider.EdmImplProv;
import com.sap.core.odata.core.edm.provider.EdmPropertyImplProv;
import com.sap.core.odata.core.edm.provider.EdmSimplePropertyImplProv;

public class EdmPropertyImplProvTest {
  private static EdmProvider edmProvider;
  private static EdmPropertyImplProv propertySimpleProvider;
  private static EdmPropertyImplProv propertySimpleWithFacetsProvider;
  private static EdmPropertyImplProv propertySimpleWithFacetsProvider2;
  private static EdmPropertyImplProv propertyComplexProvider;

  @BeforeClass
  public static void setup() throws Exception {

    edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    Mapping propertySimpleMapping = new Mapping().setMimeType("mimeType2").setInternalName("value");
    CustomizableFeedMappings propertySimpleFeedMappings = new CustomizableFeedMappings().setFcKeepInContent(true);
    SimpleProperty propertySimple = new SimpleProperty().setName("PropertyName").setType(EdmSimpleTypeKind.String)
        .setMimeType("mimeType").setMapping(propertySimpleMapping).setCustomizableFeedMappings(propertySimpleFeedMappings);
    propertySimpleProvider = new EdmSimplePropertyImplProv(edmImplProv, propertySimple);

    Facets facets = new Facets().setNullable(false);
    SimpleProperty propertySimpleWithFacets = new SimpleProperty().setName("PropertyName").setType(EdmSimpleTypeKind.String).setFacets(facets);
    propertySimpleWithFacetsProvider = new EdmSimplePropertyImplProv(edmImplProv, propertySimpleWithFacets);

    Facets facets2 = new Facets().setNullable(true);
    SimpleProperty propertySimpleWithFacets2 = new SimpleProperty().setName("PropertyName").setType(EdmSimpleTypeKind.String).setFacets(facets2);
    propertySimpleWithFacetsProvider2 = new EdmSimplePropertyImplProv(edmImplProv, propertySimpleWithFacets2);

    ComplexType complexType = new ComplexType().setName("complexType");
    FullQualifiedName complexName = new FullQualifiedName("namespace", "complexType");
    when(edmProvider.getComplexType(complexName)).thenReturn(complexType);

    ComplexProperty propertyComplex = new ComplexProperty().setName("complexProperty").setType(complexName);
    propertyComplexProvider = new EdmComplexPropertyImplProv(edmImplProv, propertyComplex);

  }

  @Test
  public void testPropertySimple() throws Exception {
    assertNotNull(propertySimpleProvider);
    assertEquals("PropertyName", propertySimpleProvider.getName());
    assertNotNull(propertySimpleProvider.getType());
    assertEquals(EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.String), propertySimpleProvider.getType());
    assertEquals("mimeType", propertySimpleProvider.getMimeType());
    assertNotNull(propertySimpleProvider.getMapping());
    assertEquals("mimeType2", propertySimpleProvider.getMapping().getMimeType());
    assertNotNull(propertySimpleProvider.getCustomizableFeedMappings());
    assertEquals("value", propertySimpleProvider.getMapping().getInternalName());
    assertNull(propertySimpleProvider.getFacets());
    assertNotNull(propertySimpleProvider.getMultiplicity());
    assertEquals(EdmMultiplicity.ZERO_TO_ONE, propertySimpleProvider.getMultiplicity());
  }

  @Test
  public void testPropertySimpleWithFacets() throws Exception {
    assertNotNull(propertySimpleWithFacetsProvider.getFacets());
    assertNotNull(propertySimpleWithFacetsProvider.getMultiplicity());
    assertEquals(EdmMultiplicity.ONE, propertySimpleWithFacetsProvider.getMultiplicity());

    assertNotNull(propertySimpleWithFacetsProvider2.getFacets());
    assertNotNull(propertySimpleWithFacetsProvider2.getMultiplicity());
    assertEquals(EdmMultiplicity.ZERO_TO_ONE, propertySimpleWithFacetsProvider2.getMultiplicity());
  }

  @Test
  public void testPropertyComplex() throws Exception {
    assertNotNull(propertyComplexProvider);
    assertEquals("complexProperty", propertyComplexProvider.getName());
    assertEquals(EdmTypeKind.COMPLEX, propertyComplexProvider.getType().getKind());
    assertEquals("complexType", propertyComplexProvider.getType().getName());
  }

  @Test
  public void getAnnotations() throws Exception {
    EdmAnnotatable annotatable = (EdmAnnotatable) propertySimpleProvider;
    EdmAnnotations annotations = annotatable.getAnnotations();
    assertNull(annotations.getAnnotationAttributes());
    assertNull(annotations.getAnnotationElements());

  }
}
