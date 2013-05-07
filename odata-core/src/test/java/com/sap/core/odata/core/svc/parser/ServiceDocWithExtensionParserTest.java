package com.sap.core.odata.core.svc.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

import com.sap.core.odata.api.ep.EntityProviderException;

public class ServiceDocWithExtensionParserTest {
  private static final String NAMESPACE_SAP="http://www.sap.com/Protocols/SAPData";
  private static final String PREFIX_SAP = "sap";

  @Test
  public void testServiceDocument() throws EntityProviderException, IOException {
    ServiceDocWithExtensionParser svcDocumentParser = new ServiceDocWithExtensionParser();
    ServiceDokumentImpl svcDocument = svcDocumentParser.readServiceDokument(createStreamReader("/svcExample.xml"));
    assertNotNull(svcDocument);
    assertNotNull(svcDocument.getWorkspaces());
    for(WorkspaceImpl workspace : svcDocument.getWorkspaces()){
      assertEquals("Data", workspace.getTitle().getText());
      assertEquals(10, workspace.getCollections().size());
      for(CollectionImpl collection: workspace.getCollections()){
        assertNotNull(collection.getHref());
        if("TypeOneEntityCollection".equals(collection.getHref())){
          assertEquals("TypeOneEntityCollection", collection.getTitle().getText());
          assertFalse(collection.getCommonAttributes().getAttributes().isEmpty());
          assertEquals("content-version", collection.getCommonAttributes().getAttributes().get(0).getName());
          assertEquals(NAMESPACE_SAP, collection.getCommonAttributes().getAttributes().get(0).getNamespace());
          assertEquals(PREFIX_SAP, collection.getCommonAttributes().getAttributes().get(0).getPrefix());
          assertEquals("1", collection.getCommonAttributes().getAttributes().get(0).getText());
          assertFalse(collection.getExtesionElements().isEmpty());
          for(ExtensionElementImpl extElement: collection.getExtesionElements()){
            assertEquals(PREFIX_SAP, extElement.getPrefix());
            assertEquals("member-title", extElement.getName());
            assertEquals(NAMESPACE_SAP, extElement.getNamespace());
          }
        }
      }
    }
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
  private XMLStreamReader createStreamReader(final String fileName) throws EntityProviderException, IOException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
    InputStream in =  ClassLoader.class.getResourceAsStream(fileName);
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
