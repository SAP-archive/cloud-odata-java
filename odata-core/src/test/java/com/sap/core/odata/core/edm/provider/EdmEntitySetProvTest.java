/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 *
 */
public class EdmEntitySetProvTest extends BaseTest {

  private static EdmEntitySetImplProv edmEntitySetFoo;
  private static EdmEntitySetImplProv edmEnitiySetBar;
  private static EdmProvider edmProvider;

  @BeforeClass
  public static void getEdmEntityContainerImpl() throws Exception {

    edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    EntityContainerInfo entityContainer = new EntityContainerInfo().setName("Container");
    when(edmProvider.getEntityContainerInfo("Container")).thenReturn(entityContainer);
    EdmEntityContainerImplProv edmEntityContainer = new EdmEntityContainerImplProv(edmImplProv, entityContainer);

    EntitySet entitySetFoo = new EntitySet().setName("foo");
    when(edmProvider.getEntitySet("Container", "foo")).thenReturn(entitySetFoo);

    List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
    FullQualifiedName fooBarAssocName = new FullQualifiedName("namespace", "fooBarAssoc");
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

    edmEntitySetFoo = new EdmEntitySetImplProv(edmImplProv, entitySetFoo, edmEntityContainer);
    edmEnitiySetBar = new EdmEntitySetImplProv(edmImplProv, entitySetBar, edmEntityContainer);
  }

  @Test
  public void testEntitySet1() throws Exception {
    assertEquals("foo", edmEntitySetFoo.getName());
    assertEquals("Container", edmEntitySetFoo.getEntityContainer().getName());
  }

  @Test
  public void testEntitySet2() throws Exception {
    assertEquals("bar", edmEnitiySetBar.getName());
    assertEquals("Container", edmEnitiySetBar.getEntityContainer().getName());
  }

  @Test
  public void testEntitySetNavigation() throws Exception {
    List<String> navPropertyyNames = edmEntitySetFoo.getEntityType().getNavigationPropertyNames();
    assertTrue(navPropertyyNames.contains("fooBarNav"));
    EdmTyped navProperty = edmEntitySetFoo.getEntityType().getProperty("fooBarNav");
    assertNotNull(navProperty);

    EdmEntitySet relatedEntitySet = edmEntitySetFoo.getRelatedEntitySet((EdmNavigationProperty) navProperty);

    assertEquals(edmEnitiySetBar.getName(), relatedEntitySet.getName());
  }

  @Test
  public void testEntitySetType() throws Exception {
    assertEquals("fooEntityType", edmEntitySetFoo.getEntityType().getName());
    assertEquals(edmEntitySetFoo.getEntityType().getName(), edmProvider.getEntityType(new FullQualifiedName("namespace", "fooEntityType")).getName());
  }

  @Test
  public void getAnnotations() throws Exception {
    EdmAnnotatable annotatable = edmEntitySetFoo;
    EdmAnnotations annotations = annotatable.getAnnotations();
    assertNull(annotations.getAnnotationAttributes());
    assertNull(annotations.getAnnotationElements());
  }

}
