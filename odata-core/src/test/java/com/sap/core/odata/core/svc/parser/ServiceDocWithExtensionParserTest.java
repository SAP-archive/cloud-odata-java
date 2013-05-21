package com.sap.core.odata.core.svc.parser;

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

import com.sap.core.odata.api.ep.EntityProviderException;

public class ServiceDocWithExtensionParserTest {
  private static final String NAMESPACE_SAP = "http://www.sap.com/Protocols/SAPData";
  private static final String PREFIX_SAP = "sap";

  @Test
  public void testServiceDocument() throws EntityProviderException, IOException {
    ServiceDocWithExtensionParser svcDocumentParser = new ServiceDocWithExtensionParser();
    ServiceDokumentImpl svcDocument = svcDocumentParser.readServiceDokument(createStreamReader("/svcExample.xml"));
    assertNotNull(svcDocument);
    assertNotNull(svcDocument.getWorkspaces());
    for (WorkspaceImpl workspace : svcDocument.getWorkspaces()) {
      assertEquals("Data", workspace.getTitle().getText());
      assertEquals(10, workspace.getCollections().size());
      for (CollectionImpl collection : workspace.getCollections()) {
        assertNotNull(collection.getHref());
        if ("TypeOneEntityCollection".equals(collection.getHref())) {
          assertEquals("TypeOneEntityCollection", collection.getTitle().getText());
          assertFalse(collection.getCommonAttributes().getAttributes().isEmpty());
          assertEquals("content-version", collection.getCommonAttributes().getAttributes().get(0).getName());
          assertEquals(NAMESPACE_SAP, collection.getCommonAttributes().getAttributes().get(0).getNamespace());
          assertEquals(PREFIX_SAP, collection.getCommonAttributes().getAttributes().get(0).getPrefix());
          assertEquals("1", collection.getCommonAttributes().getAttributes().get(0).getText());
          assertFalse(collection.getExtesionElements().isEmpty());
          for (ExtensionElementImpl extElement : collection.getExtesionElements()) {
            assertEquals(PREFIX_SAP, extElement.getPrefix());
            assertEquals("member-title", extElement.getName());
            assertEquals(NAMESPACE_SAP, extElement.getNamespace());
          }
        }
      }
    }
  }

  @Test
  public void testExtensions() throws EntityProviderException, IOException {
    ServiceDocWithExtensionParser svcDocumentParser = new ServiceDocWithExtensionParser();
    ServiceDokumentImpl svcDocument = svcDocumentParser.readServiceDokument(createStreamReader("/svcExample.xml"));
    assertNotNull(svcDocument);
    assertNotNull(svcDocument.getExtesionElements());
    for (ExtensionElementImpl extElement : svcDocument.getExtesionElements()) {
      assertEquals("link", extElement.getName());
      assertEquals("atom", extElement.getPrefix());
      assertEquals(2, extElement.getAttributes().size());
      assertEquals("rel", extElement.getAttributes().get(0).getName());
      assertEquals("http://ldcigmd.wdf.sap.corp:50055/sap/opu/odata/IWBEP/TEA_TEST_APPLICATION/", extElement.getAttributes().get(1).getText());
      assertEquals("href", extElement.getAttributes().get(1).getName());
    }

  }

  @Test(expected = EntityProviderException.class)
  public void testSvcWithoutWorkspaces() throws EntityProviderException, IOException {
    ServiceDocWithExtensionParser svcDocumentParser = new ServiceDocWithExtensionParser();
    svcDocumentParser.readServiceDokument(createStreamReader("/invalidSvcExample.xml"));
  }

  @Test
  public void testServiceDocument2() throws EntityProviderException, IOException {
    ServiceDocWithExtensionParser svcDocumentParser = new ServiceDocWithExtensionParser();
    ServiceDokumentImpl svcDocument = svcDocumentParser.readServiceDokument(createStreamReader("/svcAtomExample.xml"));
    assertNotNull(svcDocument);
    assertEquals(2, svcDocument.getWorkspaces().size());

    WorkspaceImpl workspace = svcDocument.getWorkspaces().get(0);
    assertEquals("Main Site", workspace.getTitle().getText());
    assertEquals(2, workspace.getCollections().size());

    workspace = svcDocument.getWorkspaces().get(1);
    assertEquals("Sidebar Blog", workspace.getTitle().getText());
    assertEquals(1, workspace.getCollections().size());
    CollectionImpl collection = workspace.getCollections().get(0);
    assertEquals("Remaindered Links", collection.getTitle().getText());

    assertEquals(1, collection.getAcceptElements().size());
    assertEquals("application/atom+xml;type=entry", collection.getAcceptElements().get(0).getText());

  }

  @Test
  public void testCategories() throws EntityProviderException, IOException {
    ServiceDocWithExtensionParser svcDocumentParser = new ServiceDocWithExtensionParser();
    ServiceDokumentImpl svcDocument = svcDocumentParser.readServiceDokument(createStreamReader("/svcAtomExample.xml"));
    assertNotNull(svcDocument);
    assertEquals(2, svcDocument.getWorkspaces().size());

    WorkspaceImpl workspace = svcDocument.getWorkspaces().get(0);
    assertEquals(2, workspace.getCollections().size());
    for (CollectionImpl collection : workspace.getCollections()) {
      for (CategoriesImpl categories : collection.getCategories()) {
        assertEquals("http://example.com/cats/forMain.cats", categories.getHref());
      }
    }

    workspace = svcDocument.getWorkspaces().get(1);
    for (CollectionImpl collection : workspace.getCollections()) {
      for (CategoriesImpl categories : collection.getCategories()) {
        assertEquals(Fixed.YES, categories.getFixed());
        for (CategoryImpl category : categories.getCategoryList()) {
          assertEquals("http://example.org/extra-cats/", category.getScheme());
        }
      }
    }

  }

  @Test
  public void testExtensions2() throws EntityProviderException, IOException {
    ServiceDocWithExtensionParser svcDocumentParser = new ServiceDocWithExtensionParser();
    ServiceDokumentImpl svcDocument = svcDocumentParser.readServiceDokument(createStreamReader("/svcAtomExample.xml"));
    assertNotNull(svcDocument);
    assertNotNull(svcDocument.getExtesionElements());
    for (ExtensionElementImpl extElement : svcDocument.getExtesionElements()) {
      for (ExtensionElementImpl nestedExtElement : extElement.getElements()) {
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

  private XMLStreamReader createStreamReader(final String fileName) throws EntityProviderException, IOException {
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
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }

    return streamReader;
  }
}
