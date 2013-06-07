package com.sap.core.odata.core.ep.producer;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.ep.callback.TombstoneCallback;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.AbstractProviderTest;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.util.CircleStreamBuffer;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

public class TombstoneProducerTest extends AbstractProviderTest {

  private static final String DEFAULT_CHARSET = ContentType.CHARSET_UTF_8;
  private static final String XML_VERSION = "1.0";
  private XMLStreamWriter writer;
  private EntityProviderWriteProperties defaultProperties;
  private EntityInfoAggregator defaultEia;
  private CircleStreamBuffer csb;

  public TombstoneProducerTest(final StreamWriterImplType type) {
    super(type);
  }

  @Before
  public void initialize() throws Exception {
    csb = new CircleStreamBuffer();
    OutputStream outStream = csb.getOutputStream();
    writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
    defaultProperties = EntityProviderWriteProperties.serviceRoot(BASE_URI).build();
    defaultEia = EntityInfoAggregator.create(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"), defaultProperties.getExpandSelectTree());
  }

  @Test
  public void oneDeletedEntryWithAllProperties() throws Exception {
    //Prepare Data
    List<Map<String, Object>> deletedEntries = new ArrayList<Map<String, Object>>();
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("Id", "1");
    data.put("Name", "Neu Schwanstein");
    data.put("Seats", new Integer(20));
    data.put("Version", new Integer(3));
    deletedEntries.add(data);
    //Execute producer
    execute(deletedEntries);
    //Verify
    String xml = getXML();
    assertXpathExists("/a:feed/at:deleted-entry[@ref and @when]", xml);
    assertXpathEvaluatesTo("http://host:80/service/Rooms('1')", "/a:feed/at:deleted-entry/@ref", xml);
  }

  @Test
  public void twoDeletedEntriesWithAllProperties() throws Exception {
    //Prepare Data
    List<Map<String, Object>> deletedEntries = new ArrayList<Map<String, Object>>();
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("Id", "1");
    data.put("Name", "Neu Schwanstein");
    data.put("Seats", new Integer(20));
    data.put("Version", new Integer(3));
    deletedEntries.add(data);

    Map<String, Object> data2 = new HashMap<String, Object>();
    data2.put("Id", "2");
    data2.put("Name", "Neu Schwanstein");
    data2.put("Seats", new Integer(20));
    data2.put("Version", new Integer(3));
    deletedEntries.add(data2);
    //Execute producer
    execute(deletedEntries);
    //Verify
    String xml = getXML();
    assertXpathExists("/a:feed/at:deleted-entry[@ref and @when]", xml);
    assertXpathExists("/a:feed/at:deleted-entry[@ref=\"http://host:80/service/Rooms('1')\"]", xml);
    assertXpathExists("/a:feed/at:deleted-entry[@ref=\"http://host:80/service/Rooms('2')\"]", xml);
  }

  @Test
  public void oneDeletedEntryWithKeyProperties() throws Exception {
    //Prepare Data
    List<Map<String, Object>> deletedEntries = new ArrayList<Map<String, Object>>();
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("Id", "1");
    deletedEntries.add(data);
    //Execute producer
    execute(deletedEntries);
    //Verify
    String xml = getXML();
    assertXpathExists("/a:feed/at:deleted-entry[@ref and @when]", xml);
    assertXpathEvaluatesTo("http://host:80/service/Rooms('1')", "/a:feed/at:deleted-entry/@ref", xml);
  }

  @Test(expected = EntityProviderException.class)
  public void oneDeletedEntryWithoutProperties() throws Exception {
    //Prepare Data
    List<Map<String, Object>> deletedEntries = new ArrayList<Map<String, Object>>();
    Map<String, Object> data = new HashMap<String, Object>();
    deletedEntries.add(data);
    //Execute producer
    execute(deletedEntries);
  }

  @Test
  public void emptyEntryList() throws Exception {
    //Prepare Data
    List<Map<String, Object>> deletedEntries = new ArrayList<Map<String, Object>>();
    //Execute producer
    execute(deletedEntries);
    //Verify
    String xml = getXML();
    assertXpathExists("/a:feed", xml);
    assertXpathNotExists("/a:feed/at:deleted-entry[@ref and @when]", xml);
  }

  @Test
  public void entryWithSyndicatedUpdatedMappingPresent() throws Exception {
    //Prepare Data
    List<Map<String, Object>> deletedEntries = new ArrayList<Map<String, Object>>();
    deletedEntries.add(employeeData);
    defaultEia = EntityInfoAggregator.create(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), defaultProperties.getExpandSelectTree());
    //Execute producer
    execute(deletedEntries);
    //Verify
    String xml = getXML();
    assertXpathExists("/a:feed/at:deleted-entry[@ref and @when]", xml);
    assertXpathEvaluatesTo("http://host:80/service/Employees('1')", "/a:feed/at:deleted-entry/@ref", xml);
    assertXpathEvaluatesTo("1999-01-01T00:00:00Z", "/a:feed/at:deleted-entry/@when", xml);
  }

  @Test
  public void entryWithSyndicatedUpdatedMappingNotPresent() throws Exception {
    //Prepare Data
    List<Map<String, Object>> deletedEntries = new ArrayList<Map<String, Object>>();
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("EmployeeId", "1");
    deletedEntries.add(data);
    defaultEia = EntityInfoAggregator.create(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), defaultProperties.getExpandSelectTree());
    //Execute producer
    execute(deletedEntries);
    //Verify
    String xml = getXML();
    assertXpathExists("/a:feed/at:deleted-entry[@ref and @when]", xml);
    assertXpathEvaluatesTo("http://host:80/service/Employees('1')", "/a:feed/at:deleted-entry/@ref", xml);
    assertXpathNotExists("/a:feed/at:deleted-entry[@when='1999-01-01T00:00:00Z']", xml);
  }

  @Test
  public void entryWithSyndicatedUpdatedMappingNull() throws Exception {
    //Prepare Data
    List<Map<String, Object>> deletedEntries = new ArrayList<Map<String, Object>>();
    employeeData.put("EntryDate", null);
    deletedEntries.add(employeeData);
    defaultEia = EntityInfoAggregator.create(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), defaultProperties.getExpandSelectTree());
    //Execute producer
    execute(deletedEntries);
    //Verify
    String xml = getXML();
    assertXpathExists("/a:feed/at:deleted-entry[@ref and @when]", xml);
    assertXpathEvaluatesTo("http://host:80/service/Employees('1')", "/a:feed/at:deleted-entry/@ref", xml);
    assertXpathNotExists("/a:feed/at:deleted-entry[@when='1999-01-01T00:00:00Z']", xml);
  }

  private String getXML() throws IOException {
    InputStream inputStream = csb.getInputStream();
    assertNotNull(inputStream);
    String xml = StringHelper.inputStreamToString(inputStream);
    assertNotNull(xml);
    return xml;
  }

  void execute(final List<Map<String, Object>> deletedEntries) throws XMLStreamException, EntityProviderException {
    TombstoneProducer producer = new TombstoneProducer();
    writer.writeStartDocument(DEFAULT_CHARSET, XML_VERSION);
    writer.writeStartElement("feed");
    writer.writeDefaultNamespace(Edm.NAMESPACE_ATOM_2005);
    writer.writeNamespace(TombstoneCallback.PREFIX_TOMBSTONE, TombstoneCallback.NAMESPACE_TOMBSTONE);
    producer.appendTombstones(writer, defaultEia, defaultProperties, deletedEntries);
    writer.writeEndDocument();
  }

}
