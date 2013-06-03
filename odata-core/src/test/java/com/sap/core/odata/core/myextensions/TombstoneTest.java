package com.sap.core.odata.core.myextensions;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

public class TombstoneTest {
  private ArrayList<Map<String, Object>> roomsData;
  private ArrayList<Map<String, Object>> deletedRoomsData;
  private Map<String, ODataCallback> callbacks;
  protected static final URI BASE_URI;

  static {
    try {
      BASE_URI = new URI("http://host:80/service/");
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  @Before
  public void setXmlNamespacePrefixes() throws Exception {
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", Edm.NAMESPACE_ATOM_2005);
    prefixMap.put("d", Edm.NAMESPACE_D_2007_08);
    prefixMap.put("m", Edm.NAMESPACE_M_2007_08);
    prefixMap.put("at", AtomExtension.TOMBSTONE_NAMESPACE);
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));
  }

  @Test
  public void testOneElementDeletedEntry() throws EdmException, ODataException, IOException, XpathException, SAXException {
    initializeRoomData(2);
    initializeCallbacks();

    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).mediaResourceMimeType("mediatype").callbacks(callbacks).build();
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");

    ODataResponse response = EntityProvider.writeFeed("application/atom+xml", entitySet, roomsData, properties);
    String xmlString = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathExists("/a:feed/at:deleted-entry", xmlString);
    assertXpathEvaluatesTo("http://host:80/service/Rooms('2')", "/a:feed/at:deleted-entry/@ref", xmlString);
  }

  @Test
  public void testTwoElementsDeletedEntry() throws EdmException, ODataException, IOException, XpathException, SAXException {
    initializeRoomData(4);
    initializeCallbacks();

    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).mediaResourceMimeType("mediatype").callbacks(callbacks).build();
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");

    ODataResponse response = EntityProvider.writeFeed("application/atom+xml", entitySet, roomsData, properties);
    String xmlString = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathExists("/a:feed/at:deleted-entry", xmlString);
    assertXpathEvaluatesTo("2", "count(/a:feed/at:deleted-entry)", xmlString);
    System.out.println(xmlString);
  }

  @Test
  public void testNoElementDeletedEntry() throws EdmException, ODataException, IOException, XpathException, SAXException {
    initializeRoomData(1);
    initializeCallbacks();

    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).mediaResourceMimeType("mediatype").callbacks(callbacks).build();
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");

    ODataResponse response = EntityProvider.writeFeed("application/atom+xml", entitySet, roomsData, properties);
    String xmlString = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathNotExists("/a:feed/at:deleted-entry", xmlString);
  }

  @Test
  public void testJson() throws EdmException, ODataException, IOException, XpathException, SAXException {
    initializeRoomData(1);
    initializeCallbacks();

    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).mediaResourceMimeType("mediatype").callbacks(callbacks).build();
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");

    EntityProvider.writeFeed("application/json", entitySet, roomsData, properties);
  }

  private void initializeDeletedRoomData() {
    deletedRoomsData = new ArrayList<Map<String, Object>>();
    for (int i = 2; i <= roomsData.size(); i = i + 2) {
      HashMap<String, Object> tmp = new HashMap<String, Object>();
      tmp.put("Id", "" + i);
      tmp.put("Name", "Neu Schwanstein" + i);
      tmp.put("Seats", new Integer(20));
      tmp.put("Version", new Integer(3));
      deletedRoomsData.add(tmp);
    }

  }

  private void initializeRoomData(final int count) {
    roomsData = new ArrayList<Map<String, Object>>();
    for (int i = 1; i <= count; i++) {
      HashMap<String, Object> tmp = new HashMap<String, Object>();
      tmp.put("Id", "" + i);
      tmp.put("Name", "Neu Schwanstein" + i);
      tmp.put("Seats", new Integer(20));
      tmp.put("Version", new Integer(3));
      roomsData.add(tmp);
    }
  }

  private void initializeCallbacks() {
    initializeDeletedRoomData();
    TombstoneCallback callback = new TombstoneCallback(deletedRoomsData);
    callbacks = new HashMap<String, ODataCallback>();
    callbacks.put("deleted-entry", callback);
  }

}
