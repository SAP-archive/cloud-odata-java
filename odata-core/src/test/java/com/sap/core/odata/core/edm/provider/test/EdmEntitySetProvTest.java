package com.sap.core.odata.core.edm.provider.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.core.edm.provider.EdmEntityContainerImplProv;
import com.sap.core.odata.core.edm.provider.EdmEntitySetImplProv;
import com.sap.core.odata.core.edm.provider.EdmImplProv;

/**
 * @author SAP AG
 *
 */
public class EdmEntitySetProvTest {

  
  private static EdmEntitySetImplProv edmEnitiySetFoo;
  private static EdmEntitySetImplProv edmEnitiySetBar;
  private static EdmProvider edmProvider;
  @BeforeClass
  public static void getEdmEntityContainerImpl() throws Exception {

    edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    EntityContainerInfo entityContainer = new EntityContainerInfo().setName("Container");
    when(edmProvider.getEntityContainer("Container")).thenReturn(entityContainer);
    EdmEntityContainerImplProv edmEntityContainer = new EdmEntityContainerImplProv(edmImplProv, entityContainer);

    EntitySet entitySetFoo = new EntitySet().setName("foo");
    when(edmProvider.getEntitySet("Container", "foo")).thenReturn(entitySetFoo);

    Collection<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
    FullQualifiedName fooBarAssocName = new FullQualifiedName("namespace" , "fooBarAssoc");
    navigationProperties.add(new NavigationProperty().setName("fooBarNav").setFromRole("fromFoo").setRelationship(fooBarAssocName).setToRole("toBar"));
    
    EntityType fooEntityType = new EntityType().setName("fooEntityType").setNavigationProperties(navigationProperties);
    FullQualifiedName fooEntityTypeFullName = new FullQualifiedName("namespace", "fooEntityType");
    entitySetFoo.setEntityType(fooEntityTypeFullName);
    when(edmProvider.getEntityType(fooEntityTypeFullName)).thenReturn(fooEntityType);

    EntitySet entitySetBar = new EntitySet().setName("bar");
    when(edmProvider.getEntitySet("Container", "bar")).thenReturn(entitySetBar);

    EntityType barEntityType = new EntityType().setName("barEntityType");
    FullQualifiedName barEntityTypeFullName = new FullQualifiedName("namespace", "barEntityType");
    entitySetBar.setEntityType(barEntityTypeFullName);
    when(edmProvider.getEntityType(barEntityTypeFullName)).thenReturn(barEntityType);
    
    AssociationEnd fooEnd = new AssociationEnd().setRole("fromFoo");
    AssociationEnd barEnd = new AssociationEnd().setRole("toBar");
    
    Association fooBarAssoc = new Association().setName("fooBarAssoc").setEnd1(fooEnd).setEnd2(barEnd);
    when(edmProvider.getAssociation(fooBarAssocName)).thenReturn(fooBarAssoc);

    AssociationSet associationSet = new AssociationSet().setName("fooBarRelation").setEnd1(new AssociationSetEnd().setRole("fromFoo").setEntitySet("foo")).setEnd2(new AssociationSetEnd().setRole("toBar").setEntitySet("bar"));
    FullQualifiedName assocFQName = new FullQualifiedName("namespace", "fooBarAssoc");
    when(edmProvider.getAssociationSet("Container", assocFQName, "foo", "fromFoo")).thenReturn(associationSet);

   edmEnitiySetFoo = new EdmEntitySetImplProv(edmImplProv, entitySetFoo, edmEntityContainer);
   edmEnitiySetBar = new EdmEntitySetImplProv(edmImplProv, entitySetBar, edmEntityContainer);
  }

  @Test
  public void testEntitySet1() throws Exception {
    assertEquals("foo", edmEnitiySetFoo.getName());
    assertEquals("Container", edmEnitiySetFoo.getEntityContainer().getName());
  }

  @Test
  public void testEntitySet2() throws Exception {
    assertEquals("bar", edmEnitiySetBar.getName());
    assertEquals("Container", edmEnitiySetBar.getEntityContainer().getName());
  }

  @Test
  public void testEntitySetNavigation() throws Exception {
    Collection<String> navPropertyyNames = edmEnitiySetFoo.getEntityType().getNavigationPropertyNames();
    assertTrue(navPropertyyNames.contains("fooBarNav"));
    EdmTyped navProperty = edmEnitiySetFoo.getEntityType().getProperty("fooBarNav");
    assertNotNull(navProperty);
    
    EdmEntitySet relatedEntitySet = edmEnitiySetFoo.getRelatedEntitySet((EdmNavigationProperty) navProperty); 
    
    assertEquals(edmEnitiySetBar.getName(), relatedEntitySet.getName());
  }

  @Test
  public void testEntitySetType() throws Exception {
    
    assertEquals("fooEntityType", edmEnitiySetFoo.getEntityType().getName());
    assertEquals(edmEnitiySetFoo.getEntityType().getName(), edmProvider.getEntityType(new FullQualifiedName("namespace", "fooEntityType")).getName());
  }

}
