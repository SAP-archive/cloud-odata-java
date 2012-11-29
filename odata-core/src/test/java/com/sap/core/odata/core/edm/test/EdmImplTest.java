package com.sap.core.odata.core.edm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.core.edm.EdmImpl;

public class EdmImplTest extends EdmImpl {

  private static EdmImplTest edm;

  public EdmImplTest() {
    super(null);
  }

  @BeforeClass
  public static void getEdmImpl() throws EdmException {
    edm = new EdmImplTest();
  }

  @Test
  public void testEntityContainerCache() throws EdmException {
    assertEquals(edm.getEntityContainer("foo"), edm.getEntityContainer("foo"));
    assertNotSame(edm.getEntityContainer("foo"), edm.getEntityContainer("bar"));
    assertEquals(edm.getDefaultEntityContainer(), edm.getEntityContainer(null));
    assertNotSame(edm.getDefaultEntityContainer(), edm.getEntityContainer(""));
  }

  @Test
  public void testEntityTypeCache() throws EdmException {
    assertEquals(edm.getEntityType("foo", "bar"), edm.getEntityType("foo", "bar"));
    assertNotSame(edm.getEntityType("foo", "bar"), edm.getEntityType("bar", "foo"));
  }

  @Test
  public void testComplexTypeCache() throws EdmException {
    assertEquals(edm.getComplexType("foo", "bar"), edm.getComplexType("foo", "bar"));
    assertNotSame(edm.getComplexType("foo", "bar"), edm.getComplexType("bar", "foo"));
  }

  @Test
  public void testAssociationCache() throws EdmException {
    assertEquals(edm.getAssociation("foo", "bar"), edm.getAssociation("foo", "bar"));
    assertNotSame(edm.getAssociation("foo", "bar"), edm.getAssociation("bar", "foo"));
  }

  @Override
  protected EdmEntityContainer createEntityContainer(String name) throws ODataException {
    EdmEntityContainer edmEntityContainer = mock(EdmEntityContainer.class);
    when(edmEntityContainer.getName()).thenReturn(name);
    return edmEntityContainer;
  }

  @Override
  protected EdmEntityType createEntityType(FullQualifiedName fqName) throws ODataException {
    EdmEntityType edmEntityType = mock(EdmEntityType.class);
    when(edmEntityType.getNamespace()).thenReturn(fqName.getNamespace());
    when(edmEntityType.getName()).thenReturn(fqName.getName());
    return edmEntityType;
  }

  @Override
  protected EdmComplexType createComplexType(FullQualifiedName fqName) throws ODataException {
    EdmComplexType edmComplexType = mock(EdmComplexType.class);
    when(edmComplexType.getNamespace()).thenReturn(fqName.getNamespace());
    when(edmComplexType.getName()).thenReturn(fqName.getName());
    return edmComplexType;
  }

  @Override
  protected EdmAssociation createAssociation(FullQualifiedName fqName) throws ODataException {
    EdmAssociation edmAssociation = mock(EdmAssociation.class);
    when(edmAssociation.getNamespace()).thenReturn(fqName.getNamespace());
    when(edmAssociation.getName()).thenReturn(fqName.getName());
    return edmAssociation;
  }
}