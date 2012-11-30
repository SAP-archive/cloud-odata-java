package com.sap.core.odata.core.edm.provider.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jetty.http.HttpMethods;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmParameter;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.FunctionImportParameter;
import com.sap.core.odata.api.edm.provider.ReturnType;
import com.sap.core.odata.core.edm.provider.EdmEntityContainerImplProv;
import com.sap.core.odata.core.edm.provider.EdmFunctionImportImplProv;
import com.sap.core.odata.core.edm.provider.EdmImplProv;

public class EdmFunctionImportImplProvTest {

  private static EdmFunctionImportImplProv edmFunctionImport;
  private static EdmFunctionImportImplProv edmFunctionImportWithoutParameters;
  private static EdmEntityContainerImplProv edmEntityContainer;

  @BeforeClass
  public static void getEdmEntityContainerImpl() throws Exception {
    EdmProvider edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    EntityContainerInfo containerInfo = new EntityContainerInfo().setName("Container");
    when(edmProvider.getEntityContainerInfo("Container")).thenReturn(containerInfo);
    edmEntityContainer = new EdmEntityContainerImplProv(edmImplProv, containerInfo);

    EntitySet fooEntitySet = new EntitySet().setName("fooEntitySet");
    when(edmProvider.getEntitySet("Container", "fooEntitySet")).thenReturn(fooEntitySet);

    ReturnType fooReturnType = new ReturnType().setQualifiedName(EdmSimpleTypeKind.String.getFullQualifiedName()).setMultiplicity(EdmMultiplicity.ONE);

    Collection<FunctionImportParameter> parameters = new ArrayList<FunctionImportParameter>();
    FunctionImportParameter parameter = new FunctionImportParameter().setName("fooParameter1");
    parameters.add(parameter);

    parameter = new FunctionImportParameter().setName("fooParameter2");
    parameters.add(parameter);

    parameter = new FunctionImportParameter().setName("fooParameter3");
    parameters.add(parameter);

    FunctionImport functionImportFoo = new FunctionImport().setName("foo").setHttpMethod(HttpMethods.GET).setReturnType(fooReturnType).setEntitySet("fooEntitySet").setParameters(parameters);
    when(edmProvider.getFunctionImport("Container", "foo")).thenReturn(functionImportFoo);
    edmFunctionImport = new EdmFunctionImportImplProv(edmImplProv, functionImportFoo, edmEntityContainer);

    FunctionImport functionImportBar = new FunctionImport().setName("bar").setHttpMethod(HttpMethods.GET);
    when(edmProvider.getFunctionImport("Container", "bar")).thenReturn(functionImportBar);
    edmFunctionImportWithoutParameters = new EdmFunctionImportImplProv(edmImplProv, functionImportBar, edmEntityContainer);

  }

  @Test
  public void functionImport() throws Exception {
    assertEquals("foo", edmFunctionImport.getName());
    assertEquals(HttpMethods.GET, edmFunctionImport.getHttpMethod());

  }

  @Test
  public void containerName() throws Exception {
    assertEquals(edmEntityContainer, edmFunctionImport.getEntityContainer());
  }

  @Test
  public void returnType() throws Exception {
    EdmTyped returnType = edmFunctionImport.getReturnType();
    assertNotNull(returnType);
    assertEquals(EdmSimpleTypeKind.String.getFullQualifiedName().getName(), returnType.getType().getName());
    assertEquals(EdmMultiplicity.ONE, returnType.getMultiplicity());
  }

  @Test
  public void entitySet() throws Exception {
    EdmEntitySet entitySet = edmFunctionImport.getEntitySet();
    assertNotNull(entitySet);
    assertEquals("fooEntitySet", entitySet.getName());
    assertEquals(edmEntityContainer.getEntitySet("fooEntitySet"), entitySet);
  }

  @Test
  public void parameterExisting() throws Exception {
    Collection<String> parameterNames = edmFunctionImport.getParameterNames();
    assertTrue(parameterNames.contains("fooParameter1"));
    assertTrue(parameterNames.contains("fooParameter2"));
    assertTrue(parameterNames.contains("fooParameter3"));

    EdmParameter parameter = edmFunctionImport.getParameter("fooParameter1");
    assertNotNull(parameter);
    assertEquals("fooParameter1", parameter.getName());

    parameter = null;
    parameter = edmFunctionImport.getParameter("fooParameter1");
    assertNotNull(parameter);
    assertEquals("fooParameter1", parameter.getName());

    parameter = null;
    parameter = edmFunctionImport.getParameter("fooParameter2");
    assertNotNull(parameter);
    assertEquals("fooParameter2", parameter.getName());

    parameter = null;
    parameter = edmFunctionImport.getParameter("fooParameter3");
    assertNotNull(parameter);
    assertEquals("fooParameter3", parameter.getName());
  }

  @Test
  public void parameterNotExisting() throws Exception {
    assertNotNull(edmFunctionImportWithoutParameters.getParameterNames());

    EdmParameter parameter = edmFunctionImportWithoutParameters.getParameter("fooParameter1");
    assertNull(parameter);

    parameter = null;
    parameter = edmFunctionImportWithoutParameters.getParameter("fooParameter2");
    assertNull(parameter);

    parameter = null;
    parameter = edmFunctionImportWithoutParameters.getParameter("fooParameter3");
    assertNull(parameter);

  }
}
