package com.sap.core.odata.core.edm.provider.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.CustomizableFeedMappings;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Facets;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;
import com.sap.core.odata.core.edm.provider.EdmImplProv;
import com.sap.core.odata.core.edm.provider.EdmPropertyImplProv;

public class EdmPropertyImplProvTest {
  private static EdmProvider edmProvider;
  private static EdmPropertyImplProv propertySimpleProvider;
  private static EdmPropertyImplProv propertySimpleWithFacetsProvider;
  private static EdmPropertyImplProv propertySimpleWithFacetsProvider2;
  private static EdmPropertyImplProv propertyComplexProvider;
  private static EdmPropertyImplProv propertyEntityProvider;

  @BeforeClass
  public static void setup() throws Exception {

    edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    Mapping propertySimpleMapping = new Mapping().setMimeType("mimeType2").setValue("value");
    CustomizableFeedMappings propertySimpleFeedMappings = new CustomizableFeedMappings().setFcKeepInContent(true);
    Property propertySimple = new Property().setName("PropertyName").setType(EdmSimpleTypeKind.String.getFullQualifiedName())
        .setMimeType("mimeType").setMapping(propertySimpleMapping).setCustomizableFeedMappings(propertySimpleFeedMappings);
    propertySimpleProvider = new EdmPropertyImplProv(edmImplProv, propertySimple);
    
    
    Facets facets = new Facets().setNullable(false);
    Property propertySimpleWithFacets = new Property().setName("PropertyName").setType(EdmSimpleTypeKind.String.getFullQualifiedName()).setFacets(facets);  
    propertySimpleWithFacetsProvider = new EdmPropertyImplProv(edmImplProv, propertySimpleWithFacets);
    
    Facets facets2 = new Facets().setNullable(true);
    Property propertySimpleWithFacets2 = new Property().setName("PropertyName").setType(EdmSimpleTypeKind.String.getFullQualifiedName()).setFacets(facets2);  
    propertySimpleWithFacetsProvider2 = new EdmPropertyImplProv(edmImplProv, propertySimpleWithFacets2);
    
    ComplexType complexType = new ComplexType().setName("complexType");
    FullQualifiedName complexName = new FullQualifiedName("namespace", "complexType");
    when(edmProvider.getComplexType(complexName)).thenReturn(complexType);
    
    Property propertyComplex = new Property().setName("complexProperty").setType(complexName);
    propertyComplexProvider = new EdmPropertyImplProv(edmImplProv, propertyComplex);

    EntityType entityType = new EntityType().setName("entityType");
    FullQualifiedName entityName = new FullQualifiedName("namespace", "entityName");
    when(edmProvider.getEntityType(entityName)).thenReturn(entityType);
    
    Property propertyEntity = new Property().setName("entityProperty").setType(entityName);
    propertyEntityProvider = new EdmPropertyImplProv(edmImplProv, propertyEntity);
    
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
    assertEquals("value", propertySimpleProvider.getMapping().getValue());
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
  public void testPropertyEntity() throws Exception {
    assertNotNull(propertyEntityProvider);
    assertEquals("entityProperty", propertyEntityProvider.getName());
    assertEquals(EdmTypeKind.ENTITY, propertyEntityProvider.getType().getKind());
    assertEquals("entityType", propertyEntityProvider.getType().getName());
  }
}
