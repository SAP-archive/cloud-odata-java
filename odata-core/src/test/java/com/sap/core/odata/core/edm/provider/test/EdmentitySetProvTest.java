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
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.core.edm.provider.EdmEntityContainerImplProv;
import com.sap.core.odata.core.edm.provider.EdmImplProv;

public class EdmEntitySetProvTest {

  private static EdmEntityContainerImplProv edmEntityContainer;
  private static EdmEntityContainerImplProv edmEntityContainerParent;
  private static EdmProvider edmProvider;
  @BeforeClass
  public static void getEdmEntityContainerImpl() throws Exception {

    edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    EntityContainer entityContainerParent = new EntityContainer().setName("ContainerParent");
    when(edmProvider.getEntityContainer("ContainerParent")).thenReturn(entityContainerParent);

    EntitySet entitySetFooParent = new EntitySet().setName("fooParent");
    when(edmProvider.getEntitySet("ContainerParent", "fooParent")).thenReturn(entitySetFooParent);

    EntityContainer entityContainer = new EntityContainer().setName("Container").setExtendz("ContainerParent");
    when(edmProvider.getEntityContainer("Container")).thenReturn(entityContainer);

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

    
    edmEntityContainer = new EdmEntityContainerImplProv(edmImplProv, entityContainer);
    edmEntityContainerParent = new EdmEntityContainerImplProv(edmImplProv, entityContainerParent);

  }

  @Test
  public void testEntitySet1() throws Exception {
    assertEquals("Container", edmEntityContainer.getName());
    EntitySet entitySet1 = edmProvider.getEntitySet("Container", "foo");
    assertEquals("foo", entitySet1.getName());

    EdmEntitySet entitySet2 = edmEntityContainer.getEntitySet("foo");
    assertEquals("foo", entitySet2.getName());

    assertEquals(entitySet1.getName(), entitySet2.getName());
  }

  @Test
  public void testEntitySet2() throws Exception {
    assertEquals("Container", edmEntityContainer.getName());
    EntitySet entitySet1 = edmProvider.getEntitySet("Container", "bar");
    assertEquals("bar", entitySet1.getName());

    EdmEntitySet entitySet2 = edmEntityContainer.getEntitySet("bar");
    assertEquals("bar", entitySet2.getName());

    assertEquals(entitySet1.getName(), entitySet2.getName());
  }

  @Test
  public void testEntitySetNavigation() throws Exception {
    EdmEntitySet entitySetStart = edmEntityContainer.getEntitySet("foo");
    assertEquals("foo", entitySetStart.getName());
    
    EdmEntitySet entitySetEnd = edmEntityContainer.getEntitySet("bar");
    assertEquals("bar", entitySetEnd.getName());

    Collection<String> navPropertyyNames = entitySetStart.getEntityType().getNavigationPropertyNames();
    assertTrue(navPropertyyNames.contains("fooBarNav"));
    EdmTyped navProperty = entitySetStart.getEntityType().getProperty("fooBarNav");
    assertNotNull(navProperty);
    
    EdmEntitySet relatedEntitySet = entitySetStart.getRelatedEntitySet((EdmNavigationProperty) navProperty); 
    
    assertEquals(entitySetEnd.getName(), relatedEntitySet.getName());
  }
  
  @Test
  public void testEntitySetContainer() throws Exception {
    EdmEntitySet entitySet = edmEntityContainer.getEntitySet("foo");
    assertEquals("foo", entitySet.getName());
    assertEquals("Container", entitySet.getEntityContainer().getName());
  }

  @Test
  public void testEntitySetParent() throws Exception {
    assertEquals("ContainerParent", edmEntityContainerParent.getName());
    EdmEntitySet entitySet1 = edmEntityContainer.getEntitySet("fooParent");
    assertEquals("fooParent", entitySet1.getName());

    EdmEntitySet entitySet2 = edmEntityContainer.getEntitySet("fooParent");
    assertEquals("fooParent", entitySet2.getName());

    assertEquals(entitySet1, entitySet2);
  }

  @Test
  public void testEntitySetType() throws Exception {
    assertEquals("fooEntityType", edmProvider.getEntityType(new FullQualifiedName("namespace", "fooEntityType")).getName());
    EdmEntityType entityType1 = edmEntityContainer.getEntitySet("foo").getEntityType();

    assertEquals(entityType1.getName(), edmProvider.getEntityType(new FullQualifiedName("namespace", "fooEntityType")).getName());
  }

}
