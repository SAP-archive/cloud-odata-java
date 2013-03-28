package com.sap.core.odata.core.ep.producer;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.core.ODataPathSegmentImpl;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.AbstractProviderTest;
import com.sap.core.odata.core.ep.AtomEntityProvider;
import com.sap.core.odata.core.uri.ExpandSelectTreeCreator;
import com.sap.core.odata.core.uri.UriParserImpl;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.EdmTestProvider;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class XmlSelectProducerTest extends AbstractProviderTest {

  public XmlSelectProducerTest(final StreamWriterImplType type) {
    super(type);
  }

  private static final boolean T = true;
  private static final boolean F = false;

  @Test
  public void allPropertiesNoSelect() throws Exception {
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, DEFAULT_PROPERTIES);

    String xmlString = verifyResponse(response);

    verifyNavigationProperties(xmlString, T, T, T);
    assertXpathExists("/a:entry/m:properties", xmlString);
    verifyKeyProperties(xmlString, T, T, T, T);
    verifySingleProperties(xmlString, T, T, T, T);
    verifyComplexProperties(xmlString, T);
  }

  @Test
  public void allPropertiesSelectStar() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("*", null);

    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);

    String xmlString = verifyResponse(response);

    verifyNavigationProperties(xmlString, T, T, T);
    assertXpathExists("/a:entry/m:properties", xmlString);
    verifyKeyProperties(xmlString, T, T, T, T);
    verifySingleProperties(xmlString, T, T, T, T);
    verifyComplexProperties(xmlString, T);
  }

  @Test
  public void selectEmployeeId() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("EmployeeId", null);

    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);

    String xmlString = verifyResponse(response);

    verifyNavigationProperties(xmlString, F, F, F);
    assertXpathExists("/a:entry/m:properties", xmlString);
    verifyKeyProperties(xmlString, T, F, F, F);
    verifySingleProperties(xmlString, F, F, F, F);
    verifyComplexProperties(xmlString, F);
  }

  @Test
  public void selectNavigationProperties() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("ne_Team, ne_Manager", null);

    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);

    String xmlString = verifyResponse(response);

    verifyNavigationProperties(xmlString, T, F, T);
    assertXpathNotExists("/a:entry/m:properties", xmlString);
    verifyKeyProperties(xmlString, F, F, F, F);
    verifySingleProperties(xmlString, F, F, F, F);
    verifyComplexProperties(xmlString, F);
  }

  @Test
  public void selectComplexProperties() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Location", null);

    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);

    String xmlString = verifyResponse(response);

    verifyNavigationProperties(xmlString, F, F, F);
    assertXpathExists("/a:entry/m:properties", xmlString);
    verifyKeyProperties(xmlString, F, F, F, F);
    verifySingleProperties(xmlString, F, F, F, F);
    verifyComplexProperties(xmlString, T);
  }

  @Test
  public void selectComplexAndNavigationProperties() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Location, ne_Room", null);

    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);

    String xmlString = verifyResponse(response);

    verifyNavigationProperties(xmlString, F, T, F);
    assertXpathExists("/a:entry/m:properties", xmlString);
    verifyKeyProperties(xmlString, F, F, F, F);
    verifySingleProperties(xmlString, F, F, F, F);
    verifyComplexProperties(xmlString, T);
  }

  @Test
  public void selectComplexAndNavigationAndKeyProperties() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Location, ne_Room, EmployeeId, TeamId", null);

    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);

    String xmlString = verifyResponse(response);

    verifyNavigationProperties(xmlString, F, T, F);
    assertXpathExists("/a:entry/m:properties", xmlString);
    verifyKeyProperties(xmlString, T, F, F, T);
    verifySingleProperties(xmlString, F, F, F, F);
    verifyComplexProperties(xmlString, T);
  }

  @Test
  public void selectEmployeeIdEmployeeNameImageUrl() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("EmployeeId, EmployeeName, ImageUrl", null);

    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);

    String xmlString = verifyResponse(response);

    verifyNavigationProperties(xmlString, F, F, F);
    assertXpathExists("/a:entry/m:properties", xmlString);
    verifyKeyProperties(xmlString, T, F, F, F);
    verifySingleProperties(xmlString, T, F, F, T);
    verifyComplexProperties(xmlString, F);
  }

  @Test
  public void selectAge() throws Exception {
    ExpandSelectTreeNode selectTree = getSelectExpandTree("Age", null);

    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).expandSelectTree(selectTree).build();
    AtomEntityProvider provider = createAtomEntityProvider();
    ODataResponse response = provider.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);

    String xmlString = verifyResponse(response);

    verifyNavigationProperties(xmlString, F, F, F);
    assertXpathExists("/a:entry/m:properties", xmlString);
    verifyKeyProperties(xmlString, F, F, F, F);
    verifySingleProperties(xmlString, F, T, F, F);
    verifyComplexProperties(xmlString, F);
  }

  private void verifyComplexProperties(final String xmlString, final boolean location) throws IOException, SAXException, XpathException {
    if (location) {
      assertXpathExists("/a:entry/m:properties/d:Location", xmlString);
    } else {
      assertXpathNotExists("/a:entry/m:properties/d:Location", xmlString);
    }
  }

  private void verifySingleProperties(final String xmlString, final boolean employeeName, final boolean age, final boolean entryDate, final boolean imageUrl) throws IOException, SAXException, XpathException {
    if (employeeName) {
      assertXpathExists("/a:entry/m:properties/d:EmployeeName", xmlString);
    } else {
      assertXpathNotExists("/a:entry/m:properties/d:EmployeeName", xmlString);
    }
    if (age) {
      assertXpathExists("/a:entry/m:properties/d:Age", xmlString);
    } else {
      assertXpathNotExists("/a:entry/m:properties/d:Age", xmlString);
    }
    if (entryDate) {
      assertXpathExists("/a:entry/m:properties/d:EntryDate", xmlString);
    } else {
      assertXpathNotExists("/a:entry/m:properties/d:EntryDate", xmlString);
    }
    if (imageUrl) {
      assertXpathExists("/a:entry/m:properties/d:ImageUrl", xmlString);
    } else {
      assertXpathNotExists("/a:entry/m:properties/d:ImageUrl", xmlString);
    }
  }

  private void verifyKeyProperties(final String xmlString, final boolean employeeId, final boolean managerId, final boolean roomId, final boolean teamId) throws IOException, SAXException, XpathException {
    if (employeeId) {
      assertXpathExists("/a:entry/m:properties/d:EmployeeId", xmlString);
    } else {
      assertXpathNotExists("/a:entry/m:properties/d:EmployeeId", xmlString);
    }
    if (managerId) {
      assertXpathExists("/a:entry/m:properties/d:ManagerId", xmlString);
    } else {
      assertXpathNotExists("/a:entry/m:properties/d:ManagerId", xmlString);
    }
    if (roomId) {
      assertXpathExists("/a:entry/m:properties/d:RoomId", xmlString);
    } else {
      assertXpathNotExists("/a:entry/m:properties/d:RoomId", xmlString);
    }
    if (teamId) {
      assertXpathExists("/a:entry/m:properties/d:TeamId", xmlString);
    } else {
      assertXpathNotExists("/a:entry/m:properties/d:TeamId", xmlString);
    }
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

  private ExpandSelectTreeNode getSelectExpandTree(final String selectString, final String expandString) throws Exception {

    Edm edm = RuntimeDelegate.createEdm(new EdmTestProvider());
    UriParserImpl uriParser = new UriParserImpl(edm);

    List<PathSegment> pathSegments = new ArrayList<PathSegment>();
    pathSegments.add(new ODataPathSegmentImpl("Employees('1')", null));

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
