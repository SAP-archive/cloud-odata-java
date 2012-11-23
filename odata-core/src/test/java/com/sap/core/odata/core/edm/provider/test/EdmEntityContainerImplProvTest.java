package com.sap.core.odata.core.edm.provider.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.core.edm.provider.EdmEntityContainerImplProv;
import com.sap.core.odata.core.edm.provider.EdmImplProv;

public class EdmEntityContainerImplProvTest {

  private static EdmEntityContainerImplProv edmEntityContainer;

  @BeforeClass
  public static void getEdmEntityContainerImpl() throws Exception {
    EdmProvider edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);
    when(edmProvider.getEntityContainer("Container")).thenReturn(new EntityContainerInfo().setName("Container"));

    EntityContainerInfo entityContainer = new EntityContainerInfo().setName("Container1").setExtendz("Container");

    EntitySet entitySetFooFromParent = new EntitySet().setName("fooFromParent");
    when(edmProvider.getEntitySet("Container", "fooFromParent")).thenReturn(entitySetFooFromParent);

    EntitySet entitySetFoo = new EntitySet().setName("foo");
    when(edmProvider.getEntitySet("Container1", "foo")).thenReturn(entitySetFoo);

    EntitySet entitySetBar = new EntitySet().setName("foo");
    when(edmProvider.getEntitySet("Container1", "foo")).thenReturn(entitySetBar);

    AssociationSet associationSet = new AssociationSet().setName("4711");
    FullQualifiedName assocFQName = new FullQualifiedName("AssocNs", "AssocName");
    when(edmProvider.getAssociationSet("Container1", assocFQName, "foo", "fromRole")).thenReturn(associationSet);

    FunctionImport functionImportFoo = new FunctionImport().setName("foo");
    when(edmProvider.getFunctionImport("Container1", "foo")).thenReturn(functionImportFoo);

    FunctionImport functionImportBar = new FunctionImport().setName("foo");
    when(edmProvider.getFunctionImport("Container1", "foo")).thenReturn(functionImportBar);

    edmEntityContainer = new EdmEntityContainerImplProv(edmImplProv, entityContainer);
  }

  @Test
  public void testEntityContainerName() throws EdmException {
    assertEquals("Container1", edmEntityContainer.getName());
  }

  @Test
  public void testEntityContainerInheritance() throws EdmException {
    assertEquals("fooFromParent", edmEntityContainer.getEntitySet("fooFromParent").getName());

    EdmEntitySet sourceEntitySet = mock(EdmEntitySet.class);
    when(sourceEntitySet.getName()).thenReturn("foo");

    EdmAssociation edmAssociation = mock(EdmAssociation.class);
    when(edmAssociation.getNamespace()).thenReturn("AssocNs");
    when(edmAssociation.getName()).thenReturn("AssocName");

    EdmNavigationProperty edmNavigationProperty = mock(EdmNavigationProperty.class);
    when(edmNavigationProperty.getRelationship()).thenReturn(edmAssociation);
    when(edmNavigationProperty.getFromRole()).thenReturn("wrongRole");
    
    assertNull(edmEntityContainer.getAssociationSet(sourceEntitySet, edmNavigationProperty));
  }

  @Test
  public void testEntitySetCache() throws EdmException {
    assertEquals(edmEntityContainer.getEntitySet("foo"), edmEntityContainer.getEntitySet("foo"));
    assertNotSame(edmEntityContainer.getEntitySet("foo"), edmEntityContainer.getEntitySet("bar"));
  }

  @Test
  public void testAssociationSetCache() throws EdmException {
    EdmEntitySet sourceEntitySet = mock(EdmEntitySet.class);
    when(sourceEntitySet.getName()).thenReturn("foo");

    EdmAssociation edmAssociation = mock(EdmAssociation.class);
    when(edmAssociation.getNamespace()).thenReturn("AssocNs");
    when(edmAssociation.getName()).thenReturn("AssocName");

    EdmNavigationProperty edmNavigationProperty = mock(EdmNavigationProperty.class);
    when(edmNavigationProperty.getRelationship()).thenReturn(edmAssociation);
    when(edmNavigationProperty.getFromRole()).thenReturn("fromRole");
    
    assertNotNull(edmEntityContainer.getAssociationSet(sourceEntitySet, edmNavigationProperty));
    assertEquals(edmEntityContainer.getAssociationSet(sourceEntitySet, edmNavigationProperty), edmEntityContainer.getAssociationSet(sourceEntitySet, edmNavigationProperty));
  }

  @Test
  public void testFunctionImportCache() throws EdmException {
    assertEquals(edmEntityContainer.getFunctionImport("foo"), edmEntityContainer.getFunctionImport("foo"));
    assertNotSame(edmEntityContainer.getFunctionImport("foo"), edmEntityContainer.getFunctionImport("bar"));
  }
}