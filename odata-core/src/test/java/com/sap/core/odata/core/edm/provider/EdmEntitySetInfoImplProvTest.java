package com.sap.core.odata.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySetInfo;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.testutil.fit.BaseTest;

public class EdmEntitySetInfoImplProvTest extends BaseTest {

  @Test
  public void entityWithDefaultContainer() throws Exception {
    String entitySetName = "Employees";
    URI entitySetUri = new URI(entitySetName);
    String entityContainerName = "Container";

    EntitySet entitySet = new EntitySet();
    entitySet.setName(entitySetName);

    EntityContainerInfo entityContainerInfo = new EntityContainerInfo();
    entityContainerInfo.setName(entityContainerName).setDefaultEntityContainer(true);

    EdmEntitySetInfo info = new EdmEntitySetInfoImplProv(entitySet, entityContainerInfo);

    assertEquals(entitySetName, info.getEntitySetName());
    assertEquals(entitySetUri.toASCIIString(), info.getEntitySetUri().toASCIIString());
    assertEquals(entityContainerName, info.getEntityContainerName());
    assertTrue(info.isDefaultEntityContainer());
  }

  @Test
  public void entityWithoutDefaultContainer() throws Exception {
    String entitySetName = "Employees";
    String entityContainerName = "Container";
    URI entitySetUri = new URI(entityContainerName + "." + entitySetName);

    EntitySet entitySet = new EntitySet();
    entitySet.setName(entitySetName);

    EntityContainerInfo entityContainerInfo = new EntityContainerInfo();
    entityContainerInfo.setName(entityContainerName).setDefaultEntityContainer(false);

    EdmEntitySetInfo info = new EdmEntitySetInfoImplProv(entitySet, entityContainerInfo);

    assertEquals(entitySetName, info.getEntitySetName());
    assertEquals(entitySetUri.toASCIIString(), info.getEntitySetUri().toASCIIString());
    assertEquals(entityContainerName, info.getEntityContainerName());
    assertFalse(info.isDefaultEntityContainer());
  }

}
