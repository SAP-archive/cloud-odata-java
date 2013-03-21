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

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.api.ep.callback.Callback;
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

  private HashMap<String, Callback> callbacksEmployee;
  private HashMap<String, Callback> callbacksRoom;

  public XmlExpandProducerTest() {
    super();

    try {
      inlineBaseUri = new URI("http://hubbeldubbel.com/");
    } catch (URISyntaxException e) {
      throw new ODataRuntimeException(e);
    }

    callbacksEmployee = new HashMap<String, Callback>();
    callbacksEmployee.put("ne_Room", new RoomCallback(roomData, inlineBaseUri));

    callbacksRoom = new HashMap<String, Callback>();
    callbacksRoom.put("nr_Employees", new EmployeesCallback(employeesData, inlineBaseUri, roomData));
  }

  @Test
  public void expandSelectedEmployees() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Rooms('1')", "nr_Employees", "nr_Employees");

    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksRoom).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"), roomData, properties);

    verifyResponse(response);
  }

  @Test
  public void deepExpandSelectedEmployees() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Rooms('1')", "nr_Employees/ne_Room", "nr_Employees/ne_Room");

    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksRoom).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"), roomData, properties);

    verifyResponse(response);
  }

  @Test
  public void deepExpandSelectedEmployeesWithRoomId() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Rooms('1')", "nr_Employees/ne_Room/Id", "nr_Employees/ne_Room");

    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksRoom).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"), roomData, properties);

    verifyResponse(response);
  }

  @Test
  public void expandSelectedRoom() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Employees('1')", "ne_Room", "ne_Room");

    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).callbacks(callbacksEmployee).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);

    String xmlString = verifyResponse(response);
    verifyNavigationProperties(xmlString, F, T, F);
    assertXpathNotExists("/a:entry/m:properties", xmlString);
    verifyRoom(xmlString);
  }

  @Test(expected = EntityProviderException.class)
  public void expandSelectedRoomWithoutCallback() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Employees('1')", "ne_Room", "ne_Room");

    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);
  }

  private void verifyRoom(final String xmlString) throws XpathException, IOException, SAXException {
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']", xmlString);
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']/m:inline", xmlString);

    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']/m:inline/a:entry", xmlString);
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']/m:inline/a:entry[@xml:base='" + inlineBaseUri.toString() + "']", xmlString);
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']/m:inline/a:entry/a:id", xmlString);
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']/m:inline/a:entry/a:title", xmlString);
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']/m:inline/a:entry/a:updated", xmlString);

    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']/m:inline/a:entry/a:category", xmlString);
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']/m:inline/a:entry/a:link", xmlString);

    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']/m:inline/a:entry/a:content", xmlString);
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']/m:inline/a:entry/a:content/m:properties", xmlString);
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']/m:inline/a:entry/a:content/m:properties/d:Id", xmlString);
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']/m:inline/a:entry/a:content/m:properties/d:Name", xmlString);
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']/m:inline/a:entry/a:content/m:properties/d:Seats", xmlString);
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Room\" and @title='ne_Room']/m:inline/a:entry/a:content/m:properties/d:Version", xmlString);

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

  public abstract class TeamCallback implements Callback {

  }

  public abstract class ManagerCallback implements Callback {

  }

  public abstract class BuildingCallback implements Callback {

  }

}
