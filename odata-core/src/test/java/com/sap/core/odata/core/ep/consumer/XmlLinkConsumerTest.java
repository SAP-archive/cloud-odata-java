package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.ep.EntityProviderException;

/**
 * @author SAP AG
 */
public class XmlLinkConsumerTest extends AbstractConsumerTest {

  private static final String SERVICE_ROOT = "http://localhost:80/odata/";
  private static final String MANAGER_1_EMPLOYEES =
      "<links xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">"
          + "<uri>" + SERVICE_ROOT + "Employees('1')</uri>"
          + "<uri>" + SERVICE_ROOT + "Employees('2')</uri>"
          + "<uri>" + SERVICE_ROOT + "Employees('3')</uri>"
          + "<uri>" + SERVICE_ROOT + "Employees('6')</uri>"
          + "</links>";
  private static final String SINGLE_LINK =
      "<uri xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">" + SERVICE_ROOT + "Employees('6')</uri>";

  public XmlLinkConsumerTest() {
    // CHECKSTYLE:OFF:Regexp
    System.setProperty("javax.xml.stream.XMLInputFactory", "com.ctc.wstx.stax.WstxInputFactory"); //NOSONAR
    System.setProperty("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory"); //NOSONAR
    // CHECKSTYLE:ON
  }

  @Test
  public void readLink() throws Exception {
    XMLStreamReader reader = createReaderForTest(SINGLE_LINK, true);
    final String link = new XmlLinkConsumer().readLink(reader, null);
    assertEquals(SERVICE_ROOT + "Employees('6')", link);
  }

  @Test
  public void readLinks() throws Exception {
    XMLStreamReader reader = createReaderForTest(MANAGER_1_EMPLOYEES, true);
    final List<String> links = new XmlLinkConsumer().readLinks(reader, null);

    assertEquals(4, links.size());
    assertEquals(SERVICE_ROOT + "Employees('1')", links.get(0));
    assertEquals(SERVICE_ROOT + "Employees('2')", links.get(1));
    assertEquals(SERVICE_ROOT + "Employees('3')", links.get(2));
    assertEquals(SERVICE_ROOT + "Employees('6')", links.get(3));
  }

  @Test
  public void readEmptyList() throws Exception {
    final String xml = "<?xml version=\"1.1\" encoding=\"UTF-8\"?>"
        + "<links xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\" />";
    final List<String> links = new XmlLinkConsumer().readLinks(createReaderForTest(xml, true), null);
    assertNotNull(links);
    assertTrue(links.isEmpty());
  }

  @Test
  public void withInlineCount() throws Exception {
    final String xml = "<links xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">"
        + "<m:count xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\">4</m:count>"
        + "<uri>" + SERVICE_ROOT + "Employees('5')</uri>"
        + "</links>";
    final List<String> links = new XmlLinkConsumer().readLinks(createReaderForTest(xml, true), null);
    assertEquals(1, links.size());
  }

  @Test(expected = EntityProviderException.class)
  public void wrongNamespace() throws Exception {
    new XmlLinkConsumer().readLink(createReaderForTest(SINGLE_LINK.replace(Edm.NAMESPACE_D_2007_08, Edm.NAMESPACE_M_2007_08), true), null);
  }

  @Test(expected = EntityProviderException.class)
  public void xmlContent() throws Exception {
    final String xml = "<uri xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\"><uri>X</uri></uri>";
    new XmlLinkConsumer().readLink(createReaderForTest(xml, true), null);
  }
}
