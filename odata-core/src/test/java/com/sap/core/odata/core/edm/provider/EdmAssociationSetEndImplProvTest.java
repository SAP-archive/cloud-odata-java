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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmAnnotatable;
import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.EdmAssociationSetEnd;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.testutil.fit.BaseTest;

public class EdmAssociationSetEndImplProvTest extends BaseTest {
  private static EdmAssociationSetEnd edmAssociationSetEnd;
  private static EdmProvider edmProvider;

  @BeforeClass
  public static void getEdmEntityContainerImpl() throws Exception {

    edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    EntityContainerInfo entityContainer = new EntityContainerInfo().setName("Container");
    EdmEntityContainer edmEntityContainer = new EdmEntityContainerImplProv(edmImplProv, entityContainer);

    //    AssociationEnd end1 = new AssociationEnd().setRole("end1Role").setMultiplicity(EdmMultiplicity.ONE).setType(EdmSimpleTypeKind.String.getFullQualifiedName());
    //    AssociationEnd end2 = new AssociationEnd().setRole("end2Role").setMultiplicity(EdmMultiplicity.ONE).setType(EdmSimpleTypeKind.String.getFullQualifiedName());
    //    Association association = new Association().setName("association").setEnd1(end1).setEnd2(end2);
    //    FullQualifiedName assocName = new FullQualifiedName("namespace", "association");
    //    when(edmProvider.getAssociation(assocName)).thenReturn(association);

    AssociationSetEnd associationSetEnd = new AssociationSetEnd().setRole("end1Role").setEntitySet("entitySetRole1");
    EntitySet entitySet = new EntitySet().setName("entitySetRole1");
    when(edmProvider.getEntitySet("Container", "entitySetRole1")).thenReturn(entitySet);

    edmAssociationSetEnd = new EdmAssociationSetEndImplProv(associationSetEnd, edmEntityContainer.getEntitySet("entitySetRole1"));
  }

  @Test
  public void testAssociationSetEnd() throws Exception {
    EdmAssociationSetEnd setEnd = edmAssociationSetEnd;

    assertEquals("end1Role", setEnd.getRole());
    assertEquals("entitySetRole1", setEnd.getEntitySet().getName());
  }

  @Test
  public void getAnnotations() throws Exception {
    EdmAnnotatable annotatable = (EdmAnnotatable) edmAssociationSetEnd;
    EdmAnnotations annotations = annotatable.getAnnotations();
    assertNull(annotations.getAnnotationAttributes());
    assertNull(annotations.getAnnotationElements());
  }
}
