package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySetInfo;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.servicedocument.ServiceDocument;

public class JsonServiceDocumentConsumerTest {

  @Test
  public void test() throws EntityProviderException {
    JsonServiceDocumentConsumer parser = new JsonServiceDocumentConsumer();
    InputStream in = ClassLoader.class.getResourceAsStream("/svcDocJson.txt");
    ServiceDocument serviceDoc = parser.parseJson(in);
    List<EdmEntitySetInfo> entitySetsInfo = serviceDoc.getEntitySetsInfo();
    assertNotNull(entitySetsInfo);
    assertEquals(6, entitySetsInfo.size());
    for (EdmEntitySetInfo entitySetInfo : entitySetsInfo) {
      if (!entitySetInfo.isDefaultEntityContainer()) {
        if ("Container2".equals(entitySetInfo.getEntityContainerName())) {
          assertEquals("Photos", entitySetInfo.getEntitySetName());
        } else if ("Container.Nr1".equals(entitySetInfo.getEntityContainerName())) {
          assertEquals("Employees", entitySetInfo.getEntitySetName());
        } else {
          fail();
        }
      }
    }
  }

  @Test(expected = EntityProviderException.class)
  public void testInvalidServiceDocument() throws EntityProviderException {
    JsonServiceDocumentConsumer parser = new JsonServiceDocumentConsumer();
    InputStream in = ClassLoader.class.getResourceAsStream("/invalidSvcDocJson.txt");
    parser.parseJson(in);
  }

  @Test(expected = EntityProviderException.class)
  public void testServiceDocumentWithInvalidStructure() throws EntityProviderException {
    JsonServiceDocumentConsumer parser = new JsonServiceDocumentConsumer();
    InputStream in = ClassLoader.class.getResourceAsStream("/invalidSvcDocJson2.txt");
    parser.parseJson(in);
  }
}
