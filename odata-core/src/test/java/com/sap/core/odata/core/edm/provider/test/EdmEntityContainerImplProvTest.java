package com.sap.core.odata.core.edm.provider.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.core.edm.provider.EdmEntityContainerImplProv;
import com.sap.core.odata.core.edm.provider.EdmImplProv;

public class EdmEntityContainerImplProvTest {

  private static EdmEntityContainerImplProv edmEntityContainer;

  @BeforeClass
  public static void getEdmEntityContainerImpl() throws Exception {
    EdmProvider edmProvider = mock(EdmProvider.class);

    EntityContainer entityContainer = new EntityContainer().setName("Container1");
    when(edmProvider.getEntityContainer("Container1")).thenReturn(entityContainer);

    edmEntityContainer = new EdmEntityContainerImplProv(mock(EdmImplProv.class), entityContainer);
  }

  @Test
  public void testEntityContainerName() throws EdmException {
    assertEquals("Container1", edmEntityContainer.getName());
  }
}