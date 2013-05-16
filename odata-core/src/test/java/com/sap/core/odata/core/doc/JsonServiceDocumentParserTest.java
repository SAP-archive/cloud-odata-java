package com.sap.core.odata.core.doc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.api.doc.ServiceDocumentParserException;
import com.sap.core.odata.api.edm.provider.EntitySet;

public class JsonServiceDocumentParserTest {

  @Test
  public void test() throws UnsupportedEncodingException, ServiceDocumentParserException {
    JsonServiceDocumentParser parser = new JsonServiceDocumentParser();
    InputStream in = ClassLoader.class.getResourceAsStream("/svcDocJson.txt");
    List<EntitySet> entitySets = parser.parseJson(in);
    assertNotNull(entitySets);
    assertEquals(6, entitySets.size());
  }

  @Test(expected = ServiceDocumentParserException.class)
  public void testInvalidServiceDocument() throws UnsupportedEncodingException, ServiceDocumentParserException {
    JsonServiceDocumentParser parser = new JsonServiceDocumentParser();
    InputStream in = ClassLoader.class.getResourceAsStream("/invalidSvcDocJson.txt");
    parser.parseJson(in);
  }
}
