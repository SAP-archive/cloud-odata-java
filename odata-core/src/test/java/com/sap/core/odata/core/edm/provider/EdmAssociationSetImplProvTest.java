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
import com.sap.core.odata.api.edm.EdmAssociationSet;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.core.edm.provider.EdmAssociationSetImplProv;
import com.sap.core.odata.core.edm.provider.EdmEntityContainerImplProv;
import com.sap.core.odata.core.edm.provider.EdmImplProv;

public class EdmAssociationSetImplProvTest {

  private static EdmAssociationSet edmAssociationSet;
  private static EdmProvider edmProvider;

  @BeforeClass
  public static void getEdmEntityContainerImpl() throws Exception {

    edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    EntityContainerInfo entityContainer = new EntityContainerInfo().setName("Container");
    EdmEntityContainer edmEntityContainer = new EdmEntityContainerImplProv(edmImplProv, entityContainer);

    AssociationEnd end1 = new AssociationEnd().setRole("end1Role").setMultiplicity(EdmMultiplicity.ONE).setType(EdmSimpleTypeKind.String.getFullQualifiedName());
    AssociationEnd end2 = new AssociationEnd().setRole("end2Role").setMultiplicity(EdmMultiplicity.ONE).setType(EdmSimpleTypeKind.String.getFullQualifiedName());
    Association association = new Association().setName("association").setEnd1(end1).setEnd2(end2);
    FullQualifiedName assocName = new FullQualifiedName("namespace", "association");
    when(edmProvider.getAssociation(assocName)).thenReturn(association);

    AssociationSetEnd endSet1 = new AssociationSetEnd().setRole("end1Role").setEntitySet("entitySetRole1");
    when(edmProvider.getEntitySet("Container", "entitySetRole1")).thenReturn(new EntitySet().setName("entitySetRole1"));
    AssociationSetEnd endSet2 = new AssociationSetEnd().setRole("end2Role");

    AssociationSet associationSet = new AssociationSet().setName("associationSetName").setAssociation(assocName).setEnd1(endSet1).setEnd2(endSet2);

    edmAssociationSet = new EdmAssociationSetImplProv(edmImplProv, associationSet, edmEntityContainer);
  }

  @Test
  public void testAssociationSet() throws Exception {
    EdmAssociationSet associationSet = edmAssociationSet;

    assertEquals("associationSetName", associationSet.getName());

    assertEquals("end1Role", associationSet.getEnd("end1Role").getRole());
    assertEquals(null, associationSet.getEnd("endWrongRole"));
  }

  @Test(expected = EdmException.class)
  public void testAssociationSetNoEntity() throws Exception {
    EdmAssociationSet associationSet = edmAssociationSet;
    associationSet.getEnd("end2Role");
  }

  @Test
  public void testAssociationExists() throws Exception {
    EdmAssociationSet associationSet = edmAssociationSet;
    assertNotNull(associationSet.getAssociation());
  }

  @Test
  public void testEntityContainer() throws Exception {
    EdmAssociationSet associationSet = edmAssociationSet;
    assertNotNull(associationSet.getEntityContainer());
  }

  @Test
  public void getAnnotations() throws Exception {
    EdmAnnotatable annotatable = (EdmAnnotatable) edmAssociationSet;
    EdmAnnotations annotations = annotatable.getAnnotations();
    assertNull(annotations.getAnnotationAttributes());
    assertNull(annotations.getAnnotationElements());
  }
}
