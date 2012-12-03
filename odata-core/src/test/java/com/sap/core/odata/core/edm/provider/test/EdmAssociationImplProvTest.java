package com.sap.core.odata.core.edm.provider.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmAnnotatable;
import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.core.edm.provider.EdmAssociationImplProv;
import com.sap.core.odata.core.edm.provider.EdmImplProv;

public class EdmAssociationImplProvTest {

  private static EdmAssociationImplProv associationProv;
  private static EdmProvider edmProvider;

  @BeforeClass
  public static void getEdmEntityContainerImpl() throws Exception {

    edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    AssociationEnd end1 = new AssociationEnd().setRole("end1Role").setMultiplicity(EdmMultiplicity.ONE).setType(EdmSimpleTypeKind.String.getFullQualifiedName());
    AssociationEnd end2 = new AssociationEnd().setRole("end2Role").setMultiplicity(EdmMultiplicity.ONE).setType(EdmSimpleTypeKind.String.getFullQualifiedName());

    Association association = new Association().setName("association").setEnd1(end1).setEnd2(end2);

    associationProv = new EdmAssociationImplProv(edmImplProv, association, "namespace");
  }

  @Test
  public void testAssociation() throws Exception {
    EdmAssociation association = associationProv;

    assertEquals(EdmTypeKind.ASSOCIATION, association.getKind());
    assertEquals("end1Role", association.getEnd("end1Role").getRole());
    assertEquals("end2Role", association.getEnd("end2Role").getRole());
    assertEquals("namespace", association.getNamespace());
    assertEquals(null, association.getEnd("endWrongRole"));
  }

  @Test
  public void getAnnotations() throws Exception {
    EdmAnnotatable annotatable = (EdmAnnotatable) associationProv;
    EdmAnnotations annotations = annotatable.getAnnotations();
    assertNull(annotations.getAnnotationAttributes());
    assertNull(annotations.getAnnotationElements());
  }

}
