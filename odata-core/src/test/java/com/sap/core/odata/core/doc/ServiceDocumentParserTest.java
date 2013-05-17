package com.sap.core.odata.core.doc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;

import com.sap.core.odata.api.doc.AtomServiceDocument;
import com.sap.core.odata.api.doc.Collection;
import com.sap.core.odata.api.doc.ExtensionElement;
import com.sap.core.odata.api.doc.ServiceDocumentParserException;
import com.sap.core.odata.api.doc.Workspace;
import com.sap.core.odata.api.ep.EntityProvider;

public class ServiceDocumentParserTest {

  @Test
  public void test() throws ServiceDocumentParserException {
    InputStream in = ClassLoader.class.getResourceAsStream("/svcExample.xml");
    assertNotNull(EntityProvider.readServiceDocumentXml(in));
  }

  @Test
  public void testServiceDocument() throws ServiceDocumentParserException {
    InputStream in = ClassLoader.class.getResourceAsStream("/svcExample.xml");
    AtomServiceDocument serviceDocument = EntityProvider.readServiceDocumentXml(in);
    assertNotNull(serviceDocument);
    for (Workspace workspace : serviceDocument.getWorkspaces()) {
      assertEquals(10, workspace.getCollections().size());
      for (Collection collection : workspace.getCollections()) {
        assertNotNull(collection.getExtesionElements().get(0));
        assertEquals("member-title", collection.getExtesionElements().get(0).getName());
        assertEquals("sap", collection.getExtesionElements().get(0).getPrefix());
      }
    }
    for (ExtensionElement extElement : serviceDocument.getExtesionElements()) {
      assertEquals(2, extElement.getAttributes().size());
    }
  }
}
