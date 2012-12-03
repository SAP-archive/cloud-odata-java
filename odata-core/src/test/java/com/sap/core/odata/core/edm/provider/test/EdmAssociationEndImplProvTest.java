package com.sap.core.odata.core.edm.provider.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmAnnotatable;
import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.EdmAssociationEnd;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.core.edm.provider.EdmAssociationEndImplProv;
import com.sap.core.odata.core.edm.provider.EdmImplProv;

public class EdmAssociationEndImplProvTest {
  private static EdmAssociationEndImplProv associationEndProv;
  private static EdmProvider edmProvider;

  @BeforeClass
  public static void getEdmEntityContainerImpl() throws Exception {

    edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    AssociationEnd end1 = new AssociationEnd().setRole("end1Role").setMultiplicity(EdmMultiplicity.ONE).setType(EdmSimpleTypeKind.String.getFullQualifiedName());

    associationEndProv = new EdmAssociationEndImplProv(edmImplProv, end1);
  }

  @Test
  public void testAssociationEnd() throws Exception {
    EdmAssociationEnd associationEnd = associationEndProv;

    assertEquals("end1Role", associationEnd.getRole());
    assertEquals(EdmMultiplicity.ONE, associationEnd.getMultiplicity());
  }

  @Test(expected = EdmException.class)
  public void testAssociationEntityType() throws Exception {
    EdmAssociationEnd associationEnd = associationEndProv;
    associationEnd.getEntityType();

  }

  @Test
  public void getAnnotations() throws Exception {
    EdmAnnotatable annotatable = (EdmAnnotatable) associationEndProv;
    EdmAnnotations annotations = annotatable.getAnnotations();
    assertNull(annotations.getAnnotationAttributes());
    assertNull(annotations.getAnnotationElements());
  }
}
