package com.sap.core.odata.core.doc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

import com.sap.core.odata.api.doc.Categories;
import com.sap.core.odata.api.doc.Category;
import com.sap.core.odata.api.doc.Collection;
import com.sap.core.odata.api.doc.ExtensionElement;
import com.sap.core.odata.api.doc.Fixed;
import com.sap.core.odata.api.doc.ServiceDocumentParserException;
import com.sap.core.odata.api.doc.Workspace;
import com.sap.core.odata.api.edm.Edm;

public class AtomServiceDocumentParserTest {
  private static final String NAMESPACE_SAP = "http://www.sap.com/Protocols/SAPData";
  private static final String PREFIX_SAP = "sap";

  @Test
  public void testServiceDocument() throws IOException, ServiceDocumentParserException {
    AtomServiceDocumentParser svcDocumentParser = new AtomServiceDocumentParser();
    AtomServiceDokumentImpl svcDocument = svcDocumentParser.readServiceDokument(createStreamReader("/svcExample.xml"));
    assertNotNull(svcDocument);
    assertNotNull(svcDocument.getWorkspaces());
    for (Workspace workspace : svcDocument.getWorkspaces()) {
      assertEquals("Data", workspace.getTitle().getText());
      assertEquals(10, workspace.getCollections().size());
      for (Collection collection : workspace.getCollections()) {
        assertNotNull(collection.getHref());
        if ("TypeOneEntityCollection".equals(collection.getHref())) {
          assertEquals("TypeOneEntityCollection", collection.getTitle().getText());
          assertFalse(collection.getCommonAttributes().getAttributes().isEmpty());
          assertEquals("content-version", collection.getCommonAttributes().getAttributes().get(0).getName());
          assertEquals(NAMESPACE_SAP, collection.getCommonAttributes().getAttributes().get(0).getNamespace());
          assertEquals(PREFIX_SAP, collection.getCommonAttributes().getAttributes().get(0).getPrefix());
          assertEquals("1", collection.getCommonAttributes().getAttributes().get(0).getText());
          assertFalse(collection.getExtesionElements().isEmpty());
          for (ExtensionElement extElement : collection.getExtesionElements()) {
            assertEquals(PREFIX_SAP, extElement.getPrefix());
            assertEquals("member-title", extElement.getName());
            assertEquals(NAMESPACE_SAP, extElement.getNamespace());
          }
        }
      }
    }
  }

  @Test
  public void testExtensionsWithAttributes() throws IOException, ServiceDocumentParserException {
    AtomServiceDocumentParser svcDocumentParser = new AtomServiceDocumentParser();
    AtomServiceDokumentImpl svcDocument = svcDocumentParser.readServiceDokument(createStreamReader("/svcExample.xml"));
    assertNotNull(svcDocument);
    assertNotNull(svcDocument.getExtesionElements());
    assertEquals(2, svcDocument.getExtesionElements().size());
    for (ExtensionElement extElement : svcDocument.getExtesionElements()) {
      assertEquals("link", extElement.getName());
      assertEquals("atom", extElement.getPrefix());
      assertEquals(2, extElement.getAttributes().size());
      assertEquals("rel", extElement.getAttributes().get(0).getName());
      assertEquals("http://ldcigmd.wdf.sap.corp:50055/sap/opu/odata/IWBEP/TEA_TEST_APPLICATION/", extElement.getAttributes().get(1).getText());
      assertEquals("href", extElement.getAttributes().get(1).getName());
    }

  }

  @Test
  public void testServiceDocument2() throws IOException, ServiceDocumentParserException {
    AtomServiceDocumentParser svcDocumentParser = new AtomServiceDocumentParser();
    AtomServiceDokumentImpl svcDocument = svcDocumentParser.readServiceDokument(createStreamReader("/svcAtomExample.xml"));
    assertNotNull(svcDocument);
    assertEquals(2, svcDocument.getWorkspaces().size());

    Workspace workspace = svcDocument.getWorkspaces().get(0);
    assertEquals("Main Site", workspace.getTitle().getText());
    assertEquals(2, workspace.getCollections().size());

    workspace = svcDocument.getWorkspaces().get(1);
    assertEquals("Sidebar Blog", workspace.getTitle().getText());
    assertEquals(1, workspace.getCollections().size());
    Collection collection = workspace.getCollections().get(0);
    assertEquals("Remaindered Links", collection.getTitle().getText());

    assertEquals(1, collection.getAcceptElements().size());
    assertEquals("application/atom+xml;type=entry", collection.getAcceptElements().get(0).getValue());

  }

  @Test
  public void testCategories() throws IOException, ServiceDocumentParserException {
    AtomServiceDocumentParser svcDocumentParser = new AtomServiceDocumentParser();
    AtomServiceDokumentImpl svcDocument = svcDocumentParser.readServiceDokument(createStreamReader("/svcAtomExample.xml"));
    assertNotNull(svcDocument);
    assertEquals(2, svcDocument.getWorkspaces().size());

    Workspace workspace = svcDocument.getWorkspaces().get(0);
    assertEquals(2, workspace.getCollections().size());
    for (Collection collection : workspace.getCollections()) {
      for (Categories categories : collection.getCategories()) {
        assertEquals("http://example.com/cats/forMain.cats", categories.getHref());
      }
    }

    workspace = svcDocument.getWorkspaces().get(1);
    for (Collection collection : workspace.getCollections()) {
      for (Categories categories : collection.getCategories()) {
        assertEquals(Fixed.YES, categories.getFixed());
        for (Category category : categories.getCategoryList()) {
          assertEquals("http://example.org/extra-cats/", category.getScheme());
        }
      }
    }

  }

  @Test
  public void testNestedExtensions() throws IOException, ServiceDocumentParserException {
    AtomServiceDocumentParser svcDocumentParser = new AtomServiceDocumentParser();
    AtomServiceDokumentImpl svcDocument = svcDocumentParser.readServiceDokument(createStreamReader("/svcAtomExample.xml"));
    assertNotNull(svcDocument);
    assertNotNull(svcDocument.getExtesionElements());
    for (ExtensionElement extElement : svcDocument.getExtesionElements()) {
      for (ExtensionElement nestedExtElement : extElement.getElements()) {
        if ("extension2".equals(nestedExtElement.getName())) {
          assertFalse(nestedExtElement.getAttributes().isEmpty());
          assertEquals("attributeValue", nestedExtElement.getAttributes().get(0).getText());
          assertEquals("attr", nestedExtElement.getAttributes().get(0).getName());
        } else if ("extension3".equals(nestedExtElement.getName())) {
          assertTrue(nestedExtElement.getAttributes().isEmpty());
          assertEquals("value", nestedExtElement.getText());
        } else if ("extension4".equals(nestedExtElement.getName())) {
          assertEquals("text", nestedExtElement.getText());
          assertFalse(nestedExtElement.getAttributes().isEmpty());
          assertEquals("attributeValue", nestedExtElement.getAttributes().get(0).getText());
          assertEquals("attr", nestedExtElement.getAttributes().get(0).getName());
        } else {
          fail();
        }
      }
    }
  }

  @Test(expected = ServiceDocumentParserException.class)
  public void testWithoutTitle() throws IOException, ServiceDocumentParserException {
    AtomServiceDocumentParser svcDocumentParser = new AtomServiceDocumentParser();
    svcDocumentParser.readServiceDokument(createStreamReader("/svcDocument.xml"));
  }

  @Test(expected = ServiceDocumentParserException.class)
  public void testSvcWithoutWorkspaces() throws IOException, ServiceDocumentParserException {
    AtomServiceDocumentParser svcDocumentParser = new AtomServiceDocumentParser();
    svcDocumentParser.readServiceDokument(createStreamReader("/invalidSvcExample.xml"));
  }

  @Test
  public void testServiceDocument3() throws IOException, ServiceDocumentParserException {
    AtomServiceDocumentParser svcDocumentParser = new AtomServiceDocumentParser();
    AtomServiceDokumentImpl svcDocument = svcDocumentParser.readServiceDokument(createStreamReader("/serviceDocExample.xml"));
    assertNotNull(svcDocument);
    assertNotNull(svcDocument.getWorkspaces());
    for (Workspace workspace : svcDocument.getWorkspaces()) {
      assertEquals("Data", workspace.getTitle().getText());
      assertEquals(9, workspace.getCollections().size());
      for (Collection collection : workspace.getCollections()) {
        assertNotNull(collection.getHref());
        if ("TravelagencyCollection".equals(collection.getHref())) {
          assertEquals("TravelagencyCollection", collection.getTitle().getText());
          assertEquals(2, collection.getCommonAttributes().getAttributes().size());
          assertEquals("content-version", collection.getCommonAttributes().getAttributes().get(1).getName());
          assertEquals(NAMESPACE_SAP, collection.getCommonAttributes().getAttributes().get(1).getNamespace());
          assertEquals(PREFIX_SAP, collection.getCommonAttributes().getAttributes().get(1).getPrefix());
          assertEquals("1", collection.getCommonAttributes().getAttributes().get(1).getText());
          assertFalse(collection.getExtesionElements().isEmpty());
          for (ExtensionElement extElement : collection.getExtesionElements()) {
            if ("member-title".equals(extElement.getName())) {
              assertEquals(PREFIX_SAP, extElement.getPrefix());
              assertEquals(NAMESPACE_SAP, extElement.getNamespace());
              assertEquals("Travelagency", extElement.getText());
            } else if ("collectionLayout".equals(extElement.getName())) {
              assertEquals("gp", extElement.getPrefix());
              assertEquals("http://www.sap.com/Protocols/SAPData/GenericPlayer", extElement.getNamespace());
              assertNotNull(extElement.getAttributes());
              assertEquals(2, extElement.getAttributes().size());
              assertEquals("display-order", extElement.getAttributes().get(0).getName());
              assertEquals("0010", extElement.getAttributes().get(0).getText());
              assertEquals("top-level", extElement.getAttributes().get(1).getName());
              assertEquals("true", extElement.getAttributes().get(1).getText());
            } else if ("link".equals(extElement.getName())) {
              assertEquals(Edm.NAMESPACE_ATOM_2005, extElement.getNamespace());
              assertEquals(4, extElement.getAttributes().size());
              assertEquals("TravelagencyCollection/OpenSearchDescription.xml", extElement.getAttributes().get(0).getText());
              assertEquals("href", extElement.getAttributes().get(0).getName());
            } else {
              fail();
            }
          }
        }
      }
    }
  }

  private XMLStreamReader createStreamReader(final String fileName) throws ServiceDocumentParserException, IOException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
    InputStream in = ClassLoader.class.getResourceAsStream(fileName);
    if (in == null) {
      throw new IOException("Requested file '" + fileName + "' was not found.");
    }
    XMLStreamReader streamReader;
    try {
      streamReader = factory.createXMLStreamReader(in);
    } catch (XMLStreamException e) {
      throw new ServiceDocumentParserException("Invalid Service Document");
    }

    return streamReader;
  }
}
