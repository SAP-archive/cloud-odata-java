package com.sap.core.odata.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

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
import com.sap.core.odata.api.edm.provider.PropertyRef;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class EdmAssociationImplProvTest extends BaseTest {

  private static EdmAssociationImplProv associationProv;
  private static EdmProvider edmProvider;

  @BeforeClass
  public static void getEdmEntityContainerImpl() throws Exception {

    edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    AssociationEnd end1 = new AssociationEnd().setRole("end1Role").setMultiplicity(EdmMultiplicity.ONE).setType(EdmSimpleTypeKind.String.getFullQualifiedName());
    AssociationEnd end2 = new AssociationEnd().setRole("end2Role").setMultiplicity(EdmMultiplicity.ONE).setType(EdmSimpleTypeKind.String.getFullQualifiedName());

    List<PropertyRef> propRef = new ArrayList<PropertyRef>();
    propRef.add(new PropertyRef().setName("prop1"));
    List<PropertyRef> propRef2 = new ArrayList<PropertyRef>();
    propRef2.add(new PropertyRef().setName("prop2"));

    //    TODO: How to test the referential constraint role?
    //    ReferentialConstraintRole principal = new ReferentialConstraintRole().setRole("forRole1").setPropertyRef(propRef);
    //    ReferentialConstraintRole dependent = new ReferentialConstraintRole().setRole("forRole2").setPropertyRef(propRef2);
    //    ReferentialConstraint referentialConstraint = new ReferentialConstraint().setPrincipal(principal).setDependent(dependent);

    Association association = new Association().setName("association").setEnd1(end1).setEnd2(end2);//.setReferentialConstraint(referentialConstraint);

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
    EdmAnnotatable annotatable = associationProv;
    EdmAnnotations annotations = annotatable.getAnnotations();
    assertNull(annotations.getAnnotationAttributes());
    assertNull(annotations.getAnnotationElements());
  }

}
