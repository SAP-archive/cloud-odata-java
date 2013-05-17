package com.sap.core.odata.core.doc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.api.doc.ServiceDocument;
import com.sap.core.odata.api.edm.EdmEntitySetInfo;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProviderException;

public class JsonServiceDocumentParserTest {

  @Test
  public void test() throws UnsupportedEncodingException, EdmException, EntityProviderException {
    JsonServiceDocumentParser parser = new JsonServiceDocumentParser();
    InputStream in = ClassLoader.class.getResourceAsStream("/svcDocJson.txt");
    ServiceDocument serviceDoc = parser.parseJson(in);
    List<EdmEntitySetInfo> entitySetsInfo = serviceDoc.getEntitySetsInfo();
    assertNotNull(entitySetsInfo);
    assertEquals(6, entitySetsInfo.size());
    for (EdmEntitySetInfo entitySetInfo : entitySetsInfo) {
      if (!entitySetInfo.isDefaultEntityContainer()) {
        assertEquals("Container2", entitySetInfo.getEntityContainerName());
        assertEquals("Photos", entitySetInfo.getEntitySetName());
      }
    }
  }

  @Test(expected = EntityProviderException.class)
  public void testInvalidServiceDocument() throws UnsupportedEncodingException, EdmException, EntityProviderException {
    JsonServiceDocumentParser parser = new JsonServiceDocumentParser();
    InputStream in = ClassLoader.class.getResourceAsStream("/invalidSvcDocJson.txt");
    parser.parseJson(in);
  }

  @Test(expected = EntityProviderException.class)
  public void testServiceDocumentWithInvalidStructure() throws UnsupportedEncodingException, EdmException, EntityProviderException {
    JsonServiceDocumentParser parser = new JsonServiceDocumentParser();
    InputStream in = ClassLoader.class.getResourceAsStream("/invalidSvcDocJson2.txt");
    parser.parseJson(in);
  }
}
