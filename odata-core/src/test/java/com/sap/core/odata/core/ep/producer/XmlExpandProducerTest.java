package com.sap.core.odata.core.ep.producer;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.ep.callback.OnWriteEntryContent;
import com.sap.core.odata.api.ep.callback.OnWriteFeedContent;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackResult;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackResult;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.core.ODataPathSegmentImpl;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.AbstractProviderTest;
import com.sap.core.odata.core.ep.AtomEntityProvider;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.core.uri.ExpandSelectTreeCreator;
import com.sap.core.odata.core.uri.UriParserImpl;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.EdmTestProvider;
import com.sap.core.odata.testutil.mock.MockFacade;

public class XmlExpandProducerTest extends AbstractProviderTest {

  private static final boolean T = true;
  private static final boolean F = false;
  private final URI inlineBaseUri;

  private String employeeXPathString = "/a:entry/a:link[@href=\"Rooms('1')/nr_Employees\" and @title='nr_Employees']";
  private String roomXPathString = "/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']";
  private String teamXPathString = "/a:entry/a:link[@href=\"Employees('1')/ne_Team\" and @title='ne_Team']";
  private String buildingXPathString = "/a:entry/a:link[@href=\"Buildings('1')/nb_Rooms\" and @title='nb_Rooms']";

  public XmlExpandProducerTest(final StreamWriterImplType type) {
    super(type);

    try {
      inlineBaseUri = new URI("http://hubbeldubbel.com/");
    } catch (URISyntaxException e) {
      throw new ODataRuntimeException(e);
    }
  }

  @Test
  public void expandSelectedEmployees() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Rooms('1')", "nr_Employees", "nr_Employees");

    HashMap<String, ODataCallback> callbacksRoom = createCallbacks("Rooms");
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksRoom).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"), roomData, properties);

    String xmlString = verifyResponse(response);
    assertXpathNotExists("/a:entry/m:properties", xmlString);
    assertXpathExists("/a:entry/a:link", xmlString);
    verifyEmployees(employeeXPathString, xmlString);
  }

  @Test
  public void expandSelectedEmployeesNull() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Rooms('1')", "nr_Employees", "nr_Employees");

    HashMap<String, ODataCallback> callbacksRoom = new HashMap<String, ODataCallback>();
    ODataCallback employeeCallback = new OnWriteFeedContent() {

      @Override
      public WriteFeedCallbackResult retrieveFeedResult(final WriteFeedCallbackContext context) throws ODataApplicationException {
        WriteFeedCallbackResult writeFeedCallbackResult = new WriteFeedCallbackResult();
        writeFeedCallbackResult.setInlineProperties(DEFAULT_PROPERTIES);
        writeFeedCallbackResult.setFeedData(null);
        return writeFeedCallbackResult;
      }
    };
    callbacksRoom.put("nr_Employees", employeeCallback);
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksRoom).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"), roomData, properties);

    String xmlString = verifyResponse(response);
    assertXpathNotExists("/a:entry/m:properties", xmlString);
    assertXpathExists("/a:entry/a:link", xmlString);
    assertXpathExists(employeeXPathString + "/m:inline", xmlString);
    assertXpathExists(employeeXPathString + "/m:inline/a:feed", xmlString);
    assertXpathNotExists(employeeXPathString + "/m:inline/a:feed/a:entry", xmlString);
  }

  @Test
  public void expandSelectedEmployeesEmpty() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Rooms('1')", "nr_Employees", "nr_Employees");

    HashMap<String, ODataCallback> callbacksRoom = new HashMap<String, ODataCallback>();
    ODataCallback employeeCallback = new OnWriteFeedContent() {

      @Override
      public WriteFeedCallbackResult retrieveFeedResult(final WriteFeedCallbackContext context) throws ODataApplicationException {
        WriteFeedCallbackResult writeFeedCallbackResult = new WriteFeedCallbackResult();
        writeFeedCallbackResult.setInlineProperties(DEFAULT_PROPERTIES);
        writeFeedCallbackResult.setFeedData(new ArrayList<Map<String, Object>>());
        return writeFeedCallbackResult;
      }
    };
    callbacksRoom.put("nr_Employees", employeeCallback);
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksRoom).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"), roomData, properties);

    String xmlString = verifyResponse(response);
    assertXpathNotExists("/a:entry/m:properties", xmlString);
    assertXpathExists("/a:entry/a:link", xmlString);
    assertXpathExists(employeeXPathString + "/m:inline", xmlString);
    assertXpathExists(employeeXPathString + "/m:inline/a:feed", xmlString);
  }

  @Test
  public void expandSelectedEmployeesWithSelfLink() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Rooms('1')", "nr_Employees", "nr_Employees");

    HashMap<String, ODataCallback> callbacksRoom = createCallbacks("Rooms");
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksRoom).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"), roomData, properties);

    String xmlString = verifyResponse(response);
    assertXpathNotExists("/a:entry/m:properties", xmlString);
    assertXpathExists("/a:entry/a:link", xmlString);
    verifyEmployees(employeeXPathString, xmlString);
    assertXpathExists(employeeXPathString + "/m:inline/a:feed/a:link[@href=\"Rooms('1')/nr_Employees\"]", xmlString);
  }

  @Test
  public void deepExpandSelectedEmployees() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Rooms('1')", "nr_Employees/ne_Room", "nr_Employees/ne_Room");

    HashMap<String, ODataCallback> callbacksRoom = createCallbacks("Rooms");
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksRoom).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"), roomData, properties);

    String xmlString = verifyResponse(response);
    assertXpathNotExists("/a:entry/m:properties", xmlString);
    assertXpathExists(employeeXPathString, xmlString);
    assertXpathExists(employeeXPathString + "/m:inline/a:feed" + roomXPathString, xmlString);
    assertXpathExists(employeeXPathString + "/m:inline/a:feed" + roomXPathString + "/m:inline/a:entry/a:content/m:properties", xmlString);
  }

  @Test
  public void deepExpandSelectedEmployeesWithRoomId() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Rooms('1')", "nr_Employees/ne_Room/Id", "nr_Employees/ne_Room");

    HashMap<String, ODataCallback> callbacksRoom = createCallbacks("Rooms");
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksRoom).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"), roomData, properties);

    String xmlString = verifyResponse(response);
    assertXpathNotExists("/a:entry/m:properties", xmlString);
    assertXpathExists(employeeXPathString, xmlString);
    assertXpathExists(employeeXPathString + "/m:inline/a:feed" + roomXPathString, xmlString);
    assertXpathExists(employeeXPathString + "/m:inline/a:feed" + roomXPathString + "/m:inline", xmlString);
    assertXpathExists(employeeXPathString + "/m:inline/a:feed" + roomXPathString + "/m:inline/a:entry", xmlString);
    assertXpathExists(employeeXPathString + "/m:inline/a:feed" + roomXPathString + "/m:inline/a:entry/a:content/m:properties/d:Id", xmlString);
  }

  @Test
  public void expandSelectedRoom() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Employees('1')", "ne_Room", "ne_Room");

    HashMap<String, ODataCallback> callbacksEmployee = createCallbacks("Employees");
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksEmployee).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);

    String xmlString = verifyResponse(response);
    verifyNavigationProperties(xmlString, F, T, F);
    assertXpathNotExists("/a:entry/m:properties", xmlString);
    verifyRoom(roomXPathString, xmlString);
  }

  @Test
  public void expandSelectedTeamNull() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Employees('1')", "ne_Team", "ne_Team");

    HashMap<String, ODataCallback> callbacksEmployee = createCallbacks("Employees");
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksEmployee).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);

    String xmlString = verifyResponse(response);
    verifyNavigationProperties(xmlString, F, F, T);
    assertXpathNotExists("/a:entry/m:properties", xmlString);
    assertXpathExists(teamXPathString + "/m:inline", xmlString);
    assertXpathNotExists(teamXPathString + "/m:inline/a:entry", xmlString);
  }

  @Test
  public void expandSelectedTeamEmptyDataMap() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Employees('1')", "ne_Team", "ne_Team");

    HashMap<String, ODataCallback> callbacksEmployee = new HashMap<String, ODataCallback>();
    OnWriteEntryContent callback = new OnWriteEntryContent() {

      @Override
      public WriteEntryCallbackResult retrieveEntryResult(final WriteEntryCallbackContext context) throws ODataApplicationException {
        WriteEntryCallbackResult result = new WriteEntryCallbackResult();
        result.setInlineProperties(DEFAULT_PROPERTIES);
        result.setEntryData(new HashMap<String, Object>());
        return result;
      }
    };
    callbacksEmployee.put("ne_Team", callback);
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksEmployee).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);

    String xmlString = verifyResponse(response);
    verifyNavigationProperties(xmlString, F, F, T);
    assertXpathNotExists("/a:entry/m:properties", xmlString);
    assertXpathExists(teamXPathString + "/m:inline", xmlString);
    assertXpathNotExists(teamXPathString + "/m:inline/a:entry", xmlString);
  }

  @Test
  public void expandSelectedRoomsNull() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Buildings('1')", "nb_Rooms", "nb_Rooms");

    HashMap<String, ODataCallback> callbacksEmployee = createCallbacks("Buildings");
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksEmployee).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Buildings"), buildingData, properties);

    String xmlString = verifyResponse(response);
    assertXpathNotExists("/a:entry/m:properties", xmlString);
    assertXpathExists(buildingXPathString + "/m:inline", xmlString);

    assertXpathExists(buildingXPathString + "/m:inline/a:feed", xmlString);
    assertXpathExists(buildingXPathString + "/m:inline/a:feed/a:id", xmlString);
    assertXpathExists(buildingXPathString + "/m:inline/a:feed/a:title", xmlString);
    assertXpathExists(buildingXPathString + "/m:inline/a:feed/a:updated", xmlString);
    assertXpathExists(buildingXPathString + "/m:inline/a:feed/a:author", xmlString);
    assertXpathExists(buildingXPathString + "/m:inline/a:feed/a:author/a:name", xmlString);
    assertXpathExists(buildingXPathString + "/m:inline/a:feed/a:link[@href=\"Buildings('1')/nb_Rooms\"]", xmlString);
    assertXpathNotExists(buildingXPathString + "/m:inline/a:feed/a:entry", xmlString);
  }

  @Test(expected = EntityProviderException.class)
  public void expandSelectedRoomsWithNullCallback() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Buildings('1')", "nb_Rooms", "nb_Rooms");

    HashMap<String, ODataCallback> callbacksBuilding = new HashMap<String, ODataCallback>();
    callbacksBuilding.put("nb_Rooms", null);
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksBuilding).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Buildings"), buildingData, properties);
  }

  private HashMap<String, ODataCallback> createCallbacks(final String entitySetName) throws EdmException, ODataException {
    HashMap<String, ODataCallback> callbacksEmployee = new HashMap<String, ODataCallback>();
    MyCallback callback = new MyCallback(this, inlineBaseUri);
    for (String navPropName : MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet(entitySetName).getEntityType().getNavigationPropertyNames()) {
      callbacksEmployee.put(navPropName, callback);
    }
    return callbacksEmployee;
  }

  @Test
  public void expandSelectedRoomWithoutCallback() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Employees('1')", "ne_Room", "ne_Room");

    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);

    String xmlString = verifyResponse(response);
    verifyNavigationProperties(xmlString, F, T, F);
    assertXpathNotExists("/a:entry/m:properties", xmlString);
    assertXpathNotExists(roomXPathString + "/m:inline", xmlString);
  }

  @Test(expected = EntityProviderException.class)
  public void expandSelectedRoomWithNullCallback() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Employees('1')", "ne_Room", "ne_Room");
    HashMap<String, ODataCallback> callbacksEmployee = new HashMap<String, ODataCallback>();
    callbacksEmployee.put("ne_Room", null);

    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksEmployee).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);
  }

  private void verifyEmployees(final String path, final String xmlString) throws XpathException, IOException, SAXException {
    assertXpathExists(path, xmlString);
    assertXpathExists(path + "/m:inline", xmlString);

    assertXpathExists(path + "/m:inline/a:feed[@xml:base='" + inlineBaseUri.toString() + "']", xmlString);
    assertXpathExists(path + "/m:inline/a:feed/a:entry", xmlString);
    assertXpathExists(path + "/m:inline/a:feed/a:entry/a:id", xmlString);
    assertXpathExists(path + "/m:inline/a:feed/a:entry/a:title", xmlString);
    assertXpathExists(path + "/m:inline/a:feed/a:entry/a:updated", xmlString);

    assertXpathExists(path + "/m:inline/a:feed/a:entry/a:category", xmlString);
    assertXpathExists(path + "/m:inline/a:feed/a:entry/a:link", xmlString);

    assertXpathExists(path + "/m:inline/a:feed/a:entry/a:content", xmlString);
    assertXpathExists(path + "/m:inline/a:feed/a:entry/m:properties", xmlString);
    assertXpathExists(path + "/m:inline/a:feed/a:entry/m:properties/d:EmployeeId", xmlString);
    assertXpathExists(path + "/m:inline/a:feed/a:entry/m:properties/d:EmployeeName", xmlString);
    assertXpathExists(path + "/m:inline/a:feed/a:entry/m:properties/d:ManagerId", xmlString);
    assertXpathExists(path + "/m:inline/a:feed/a:entry/m:properties/d:TeamId", xmlString);
    assertXpathExists(path + "/m:inline/a:feed/a:entry/m:properties/d:RoomId", xmlString);
    assertXpathExists(path + "/m:inline/a:feed/a:entry/m:properties/d:Location", xmlString);
    assertXpathExists(path + "/m:inline/a:feed/a:entry/m:properties/d:Age", xmlString);
    assertXpathExists(path + "/m:inline/a:feed/a:entry/m:properties/d:ImageUrl", xmlString);

  }

  private void verifyRoom(final String path, final String xmlString) throws XpathException, IOException, SAXException {
    assertXpathExists(path, xmlString);
    assertXpathExists(path + "/m:inline", xmlString);

    assertXpathExists(path + "/m:inline/a:entry", xmlString);
    assertXpathExists(path + "/m:inline/a:entry[@xml:base='" + inlineBaseUri.toString() + "']", xmlString);
    assertXpathExists(path + "/m:inline/a:entry/a:id", xmlString);
    assertXpathExists(path + "/m:inline/a:entry/a:title", xmlString);
    assertXpathExists(path + "/m:inline/a:entry/a:updated", xmlString);

    assertXpathExists(path + "/m:inline/a:entry/a:category", xmlString);
    assertXpathExists(path + "/m:inline/a:entry/a:link", xmlString);

    assertXpathExists(path + "/m:inline/a:entry/a:content", xmlString);
    assertXpathExists(path + "/m:inline/a:entry/a:content/m:properties", xmlString);
    assertXpathExists(path + "/m:inline/a:entry/a:content/m:properties/d:Id", xmlString);
    assertXpathExists(path + "/m:inline/a:entry/a:content/m:properties/d:Name", xmlString);
    assertXpathExists(path + "/m:inline/a:entry/a:content/m:properties/d:Seats", xmlString);
    assertXpathExists(path + "/m:inline/a:entry/a:content/m:properties/d:Version", xmlString);
  }

  private void verifyNavigationProperties(final String xmlString, final boolean neManager, final boolean neRoom, final boolean neTeam) throws IOException, SAXException, XpathException {
    if (neManager) {
      assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Manager\" and @title='ne_Manager']", xmlString);
    } else {
      assertXpathNotExists("/a:entry/a:link[@href=\"Employees('1')/ne_Manager\" and @title='ne_Manager']", xmlString);
    }
    if (neRoom) {
      assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']", xmlString);
    } else {
      assertXpathNotExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']", xmlString);
    }
    if (neTeam) {
      assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Team\" and @title='ne_Team']", xmlString);
    } else {
      assertXpathNotExists("/a:entry/a:link[@href=\"Employees('1')/ne_Team\" and @title='ne_Team']", xmlString);
    }
  }

  private String verifyResponse(final ODataResponse response) throws IOException {
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_ATOM_XML_ENTRY_CS_UTF_8.toContentTypeString(), response.getContentHeader());
    String xmlString = StringHelper.inputStreamToString((InputStream) response.getEntity());
    return xmlString;
  }

  private ExpandSelectTreeNode getSelectExpandTree(final String pathSegment, final String selectString, final String expandString) throws Exception {

    Edm edm = RuntimeDelegate.createEdm(new EdmTestProvider());
    UriParserImpl uriParser = new UriParserImpl(edm);

    List<PathSegment> pathSegments = new ArrayList<PathSegment>();
    pathSegments.add(new ODataPathSegmentImpl(pathSegment, null));

    Map<String, String> queryParameters = new HashMap<String, String>();
    if (selectString != null) {
      queryParameters.put("$select", selectString);
    }
    if (expandString != null) {
      queryParameters.put("$expand", expandString);
    }
    UriInfo uriInfo = uriParser.parse(pathSegments, queryParameters);

    ExpandSelectTreeCreator expandSelectTreeCreator = new ExpandSelectTreeCreator(uriInfo.getSelect(), uriInfo.getExpand());
    ExpandSelectTreeNode expandSelectTree = expandSelectTreeCreator.create();
    assertNotNull(expandSelectTree);
    return expandSelectTree;
  }
}
