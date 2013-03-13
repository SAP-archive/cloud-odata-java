package com.sap.core.odata.core.uri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * Tests for the combining of expand and select options into a single tree.
 * @author SAP AG
 */
public class ExpandSelectTreeCreatorImplTest extends BaseTest {

  private static Edm edm;

  @BeforeClass
  public static void setEdm() throws ODataException {
    edm = MockFacade.getMockEdm();
  }

  @Test
  public void testViaRuntimeDelegate() throws Exception {
    ExpandSelectTreeNode test = UriParser.createExpandSelectTree(null, null);
    assertNotNull(test);
  }

  @Test
  public void allNull() throws Exception {
    //{"all":true,"properties":[],"links":[]}
    String expected = "{\"all\":true,\"properties\":[],\"links\":[]}";
    String actual = getExpandSelectTree(null, null).toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void oneProperty() throws Exception {
    //{"all":false,"properties":["Age"],"links":[]}
    String expected = "{\"all\":false,\"properties\":[\"Age\"],\"links\":[]}";

    //$select=Age
    String actual = getExpandSelectTree("Age", null).toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void complexProperty() throws Exception {
    final ExpandSelectTreeNode actual = getExpandSelectTree("Location", null);
    assertNotNull(actual);
    assertFalse(actual.isAll());
    assertEquals(Arrays.asList(edm.getEntityType("RefScenario", "Employee").getProperty("Location")),
        actual.getProperties());
    assertTrue(actual.getLinks().isEmpty());
  }

  @Test
  public void onlyPropertyAndExpandLink() throws Exception {
    //{"all":false,"properties":["Age"],"links":[]}
    String expected = "{\"all\":false,\"properties\":[\"Age\"],\"links\":[]}";

    //$select=Age&$expand=ne_Room
    String actual = getExpandSelectTree("Age", "ne_Room").toJsonString();
    assertEquals(expected, actual);

  }

  @Test
  public void starAndExpandLink() throws Exception {
    //{"all":true,"properties":[],"links":[]}
    String expected = "{\"all\":true,\"properties\":[],\"links\":[]}";

    //$select=*&$expand=ne_Room
    String actual = getExpandSelectTree("*", "ne_Room").toJsonString();
    assertEquals(expected, actual);

    //$select=*,EmployeeId&$expand=ne_Room
    actual = getExpandSelectTree("*,EmployeeId", "ne_Room").toJsonString();
    assertEquals(expected, actual);

    //$select=EmployeeId,*&$expand=ne_Room
    actual = getExpandSelectTree("EmployeeId,*", "ne_Room").toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void oneSelectLink() throws Exception {
    //{"all":false,"properties":[],"links":[{"ne_Room":null}]}
    String expected = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Room\":null}]}";

    //$select=ne_Room
    String actual = getExpandSelectTree("ne_Room", null).toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void starAndNavPropInSelectAndExpand() throws Exception {
    //{"all":false,"properties":[],"links":[{\"ne_Room\":{\"all\":true,\"properties\":[],\"links\":[]}}]}
    String expected = "{\"all\":true,\"properties\":[],\"links\":[{\"ne_Room\":{\"all\":true,\"properties\":[],\"links\":[]}}]}";

    //$select=ne_Room,* $expand=ne_Room
    String actual = getExpandSelectTree("ne_Room,*", "ne_Room").toJsonString();
    assertEquals(expected, actual);

    //$select=*,ne_Room $expand=ne_Room
    actual = getExpandSelectTree("*,ne_Room", "ne_Room").toJsonString();
    assertEquals(expected, actual);

    //$select=*,ne_Room,ne_Manager $expand=ne_Room
    actual = getExpandSelectTree("*,ne_Room,ne_Manager", "ne_Room").toJsonString();
    assertEquals(expected, actual);

    //$select=ne_Room,*,ne_Manager $expand=ne_Room
    actual = getExpandSelectTree("ne_Room,*,ne_Manager", "ne_Room").toJsonString();
    assertEquals(expected, actual);

    //$select=ne_Room,ne_Manager,* $expand=ne_Room
    actual = getExpandSelectTree("ne_Room,ne_Manager,*", "ne_Room").toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void sameLinkInSelectAndExpand() throws Exception {
    //{"all":false,"properties":[],"links":[{"ne_Room":{"all":true,"properties":[],"links":[]}}]}
    String expected = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Room\":{\"all\":true,\"properties\":[],\"links\":[]}}]}";

    //$select=ne_Room&$expand=ne_Room
    String actual = getExpandSelectTree("ne_Room", "ne_Room").toJsonString();
    assertEquals(expected, actual);

    //$select=ne_Room/*&$expand=ne_Room
    actual = getExpandSelectTree("ne_Room/*", "ne_Room").toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void deepLinkInSelectAndExpand() throws Exception {
    //{"all":false,"properties":[],"links":[{"ne_Room":{"all":false,"properties":[],"links":[{"nr_Employees":{"all":true,"properties":[],"links":[]}}]}}]}
    String expected = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Room\":{\"all\":false,\"properties\":[],\"links\":[{\"nr_Employees\":{\"all\":true,\"properties\":[],\"links\":[]}}]}}]}";

    //$select=ne_Room/nr_Employees&$expand=ne_Room,ne_Manager,ne_Room/nr_Employees,ne_Room/nr_Building
    String actual = getExpandSelectTree("ne_Room/nr_Employees", "ne_Room,ne_Manager,ne_Room/nr_Employees,ne_Room/nr_Building").toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void thirdLevel() throws Exception {
    assertEquals("{\"all\":false,\"properties\":[],\"links\":["
        + "{\"ne_Team\":{\"all\":false,\"properties\":[],\"links\":["
        + "{\"nt_Employees\":{\"all\":false,\"properties\":[],\"links\":["
        + "{\"ne_Manager\":{\"all\":false,\"properties\":[\"EmployeeId\"],\"links\":[]}}]}}]}}]}",
        getExpandSelectTree("ne_Team/nt_Employees/ne_Manager/EmployeeId", "ne_Team/nt_Employees/ne_Manager").toJsonString());
  }

  @Test
  public void expandOneLink() throws Exception {
    //{"all":true,"properties":[],"links":[{"ne_Room":{"all":true,"properties":[],"links":[]}}]}
    String expected = "{\"all\":true,\"properties\":[],\"links\":[{\"ne_Room\":{\"all\":true,\"properties\":[],\"links\":[]}}]}";

    //$expand=ne_Room
    String actual = getExpandSelectTree(null, "ne_Room").toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void complexSelectExpand() throws Exception {
    //{"all":false,"properties":["Age"],"links":[{"ne_Room":{"all":false,"properties":["Seats"],"links":[]}},{"ne_Team":null},{"ne_Manager":{"all":true,"properties":[],"links":[{"ne_Team":{"all":true,"properties":[],"links":[{"nt_Employees":{"all":true,"properties":[],"links":[]}}]}}]}}]}

    String select = "Age,ne_Room/Seats,ne_Team/Name,ne_Manager/*,ne_Manager/ne_Team,ne_Team";
    String expand = "ne_Room/nr_Building,ne_Manager/ne_Team/nt_Employees,ne_Manager/ne_Room";
    ExpandSelectTreeNode actual = getExpandSelectTree(select, expand);

    assertFalse(actual.isAll());
    assertEquals("Age", actual.getProperties().get(0).getName());
    assertNotNull(actual.getLinks());

    Map<EdmNavigationProperty, ExpandSelectTreeNode> links = actual.getLinks();
    assertEquals(3, links.size());
    for (EdmNavigationProperty navProperty : links.keySet()) {
      if ("ne_Room".equals(navProperty.getName())) {
        ExpandSelectTreeNode roomNode = links.get(navProperty);
        assertFalse(roomNode.isAll());
        assertEquals("Seats", roomNode.getProperties().get(0).getName());
        assertTrue(roomNode.getLinks().isEmpty());
      } else if ("ne_Team".equals(navProperty.getName())) {
        assertNull(links.get(navProperty));
      } else if ("ne_Manager".equals(navProperty.getName())) {
        ExpandSelectTreeNodeImpl managerNode = (ExpandSelectTreeNodeImpl) links.get(navProperty);
        String expected = "{\"all\":true,\"properties\":[],\"links\":[{\"ne_Team\":{\"all\":true,\"properties\":[],\"links\":[{\"nt_Employees\":{\"all\":true,\"properties\":[],\"links\":[]}}]}}]}";
        String actualString = managerNode.toJsonString();
        assertEquals(expected, actualString);
      } else {
        fail("Unknown navigation property in links: " + navProperty.getName());
      }
    }
  }

  @Test
  public void twoProperties() throws Exception {
    //{"all":false,"properties":["Age","EmployeeId"],"links":[]}
    String expected = "{\"all\":false,\"properties\":[\"Age\",\"EmployeeId\"],\"links\":[]}";

    //$select=Age,EmployeeId
    String actual = getExpandSelectTree("Age,EmployeeId", null).toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void sameProperties() throws Exception {
    //{"all":false,"properties":["EmployeeId","Age"],"links":[]}
    String expected = "{\"all\":false,\"properties\":[\"EmployeeId\",\"Age\"],\"links\":[]}";
    //$select=EmployeeId,Age,EmployeeId
    String actual = getExpandSelectTree("EmployeeId,Age,EmployeeId", null).toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void propertiesAndStar() throws Exception {
    //{"all":true,"properties":[],"links":[]}
    String expected = "{\"all\":true,\"properties\":[],\"links\":[]}";

    //$select=Age,EmployeeId,*
    String actual = getExpandSelectTree("Age,EmployeeId,*", null).toJsonString();
    assertEquals(expected, actual);

    //$select=*,Age,EmployeeId
    actual = getExpandSelectTree("*,Age,EmployeeId", null).toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void multiSelectLinkWithoutExpand() throws Exception {
    //{"all":false,"properties":[],"links":[{"ne_Manager":null}]}
    String expected = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Manager\":null}]}";

    //$select=ne_Manager
    String actual = getExpandSelectTree("ne_Manager", null).toJsonString();
    assertEquals(expected, actual);

    //$select=ne_Manager/ne_Manager
    actual = getExpandSelectTree("ne_Manager/ne_Manager", null).toJsonString();
    assertEquals(expected, actual);

    //$select=ne_Manager/ne_Manager,ne_Manager
    actual = getExpandSelectTree("ne_Manager/ne_Manager,ne_Manager", null).toJsonString();
    assertEquals(expected, actual);

    //$select=ne_Manager/ne_Manager/ne_Manager/ne_Manager,ne_Manager
    actual = getExpandSelectTree("ne_Manager/ne_Manager/ne_Manager/ne_Manager,ne_Manager", null).toJsonString();
    assertEquals(expected, actual);

    //$select=ne_Manager,ne_Manager/ne_Manager/ne_Manager/ne_Manager
    actual = getExpandSelectTree("ne_Manager,ne_Manager/ne_Manager/ne_Manager/ne_Manager", null).toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void sameSelectLinks() throws Exception {
    //{"all":false,"properties":[],"links":[{"ne_Manager":null}]}
    String expected = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Manager\":null}]}";

    //$select=ne_Manager,ne_Manager
    String actual = getExpandSelectTree("ne_Manager,ne_Manager", null).toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void sameExpandLinks() throws Exception {
    //{"all":true,"properties":[],"links":[{"ne_Manager":{"all":true,"properties":[],"links":[]}}]}
    String expected = "{\"all\":true,\"properties\":[],\"links\":[{\"ne_Manager\":{\"all\":true,\"properties\":[],\"links\":[]}}]}";

    //$expand=ne_Manager,ne_Manager
    String actual = getExpandSelectTree(null, "ne_Manager,ne_Manager").toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void multiExpandLinkWithoutSelect() throws Exception {
    //{"all":true,"properties":[],"links":[{"ne_Manager":{"all":true,"properties":[],"links":[{"ne_Manager":{"all":true,"properties":[],"links":[]}}]}}]}
    String expected = "{\"all\":true,\"properties\":[],\"links\":[{\"ne_Manager\":{\"all\":true,\"properties\":[],\"links\":[{\"ne_Manager\":{\"all\":true,\"properties\":[],\"links\":[]}}]}}]}";

    //$expand=ne_Manager/ne_Manager,ne_Manager
    String actual = getExpandSelectTree(null, "ne_Manager/ne_Manager,ne_Manager").toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void twoSelectLinks() throws Exception {
    //One of the two is expected but the order of links is not defined
    String expected = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Manager\":null},{\"ne_Room\":null}]}";
    String expected2 = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Room\":null},{\"ne_Manager\":null}]}";

    //$select=ne_Manager,ne_Room
    String actual = getExpandSelectTree("ne_Manager,ne_Room", null).toJsonString();

    if (!expected.equals(actual) && !expected2.equals(actual)) {
      fail("Either " + expected + " or " + expected2 + " expected but was: " + actual);
    }
  }

  @Test
  public void oneSelectDeepExpand() throws Exception {
    //{"all":false,"properties":[],"links":[{"ne_Manager":{"all":true,"properties":[],"links":[{"ne_Room":{"all":true,"properties":[],"links":[{"nr_Building":{"all":true,"properties":[],"links":[]}}]}}]}}]}
    String expected = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Manager\":{\"all\":true,\"properties\":[],\"links\":[{\"ne_Room\":{\"all\":true,\"properties\":[],\"links\":[{\"nr_Building\":{\"all\":true,\"properties\":[],\"links\":[]}}]}}]}}]}";

    //$select=ne_Manager $expand=ne_Manager/ne_Room/nr_Building
    String actual = getExpandSelectTree("ne_Manager", "ne_Manager/ne_Room/nr_Building").toJsonString();
    assertEquals(expected, actual);

    //$select=ne_Manager,ne_Manager/EmployeeId $expand=ne_Manager/ne_Room/nr_Building
    actual = getExpandSelectTree("ne_Manager,ne_Manager/EmployeeId", "ne_Manager/ne_Room/nr_Building").toJsonString();
    assertEquals(expected, actual);

    //$select=ne_Manager/EmployeeId,ne_Manager $expand=ne_Manager/ne_Room/nr_Building
    actual = getExpandSelectTree("ne_Manager/EmployeeId,ne_Manager", "ne_Manager/ne_Room/nr_Building").toJsonString();
    assertEquals(expected, actual);

    //$select=ne_Manager,ne_Manager $expand=ne_Manager/ne_Room/nr_Building
    actual = getExpandSelectTree("ne_Manager,ne_Manager", "ne_Manager/ne_Room/nr_Building").toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void starAtEndWithExpand() throws Exception {

    //{"all":false,"properties":["EmployeeId"],"links":[{"ne_Room":{"all":true,"properties":[],"links":[]}}]}
    String expected = "{\"all\":false,\"properties\":[\"EmployeeId\"],\"links\":[{\"ne_Room\":{\"all\":true,\"properties\":[],\"links\":[]}}]}";

    //$select=EmployeeId,ne_Room/* $expand=ne_Room/nr_Building
    String actual = getExpandSelectTree("EmployeeId,ne_Room/*", "ne_Room/nr_Building").toJsonString();
    assertEquals(expected, actual);

    //$select=EmployeeId,ne_Room/Id $expand=ne_Room/nr_Building/nb_Rooms
    actual = getExpandSelectTree("EmployeeId,ne_Room/*", "ne_Room/nr_Building/nb_Rooms").toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void propertyAtEndWithExpand() throws Exception {

    //{"all":false,"properties":["EmployeeId"],"links":[{"ne_Room":{"all":true,"properties":[],"links":[]}}]}
    String expected = "{\"all\":false,\"properties\":[\"EmployeeId\"],\"links\":[{\"ne_Room\":{\"all\":false,\"properties\":[\"Id\"],\"links\":[]}}]}";

    //$select=EmployeeId,ne_Room/* $expand=ne_Room/nr_Building
    String actual = getExpandSelectTree("EmployeeId,ne_Room/Id", "ne_Room/nr_Building").toJsonString();
    assertEquals(expected, actual);

    //$select=EmployeeId,ne_Room/Id $expand=ne_Room/nr_Building/nb_Rooms
    actual = getExpandSelectTree("EmployeeId,ne_Room/Id", "ne_Room/nr_Building/nb_Rooms").toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void starTest() throws Exception {

    //{"all":false,"properties":["EmployeeId"],"links":[{"ne_Room":{"all":true,"properties":[],"links":[]}}]}
    String expected = "{\"all\":true,\"properties\":[],\"links\":[{\"ne_Room\":{\"all\":true,\"properties\":[],\"links\":[]}}]}";

    //$select=EmployeeId,ne_Room/* $expand=ne_Room/nr_Building
    String actual = getExpandSelectTree("*,ne_Room/*", "ne_Room/nr_Building").toJsonString();
    assertEquals(expected, actual);

    //$select=EmployeeId,ne_Room/Id $expand=ne_Room/nr_Building/nb_Rooms
    actual = getExpandSelectTree("ne_Room/*,*", "ne_Room/nr_Building").toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void twoExpandsTwoSelects() throws Exception {

    //{"all":false,"properties":[],"links":[{"ne_Manager":{"all":false,"properties":["EmployeeId"],"links":[{"ne_Room":{"all":true,"properties":[],"links":[{"nr_Building":{"all":true,"properties":[],"links":[]}}]}}]}}]}
    String expected = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Manager\":{\"all\":false,\"properties\":[\"EmployeeId\"],\"links\":[{\"ne_Room\":{\"all\":true,\"properties\":[],\"links\":[{\"nr_Building\":{\"all\":true,\"properties\":[],\"links\":[]}}]}}]}}]}";

    //$select=ne_Manager/EmployeeId,ne_Manager/ne_Room $expand=ne_Manager/ne_Room/nr_Building
    String actual = getExpandSelectTree("ne_Manager/ne_Room,ne_Manager/EmployeeId", "ne_Manager/ne_Room/nr_Building").toJsonString();
    assertEquals(expected, actual);

    //$select=EmployeeId,ne_Room/Id $expand=ne_Room/nr_Building/nb_Rooms
    actual = getExpandSelectTree("ne_Manager/EmployeeId,ne_Manager/ne_Room", "ne_Manager/ne_Room/nr_Building").toJsonString();
    assertEquals(expected, actual);
  }

  @Test
  public void twoExpandsTest() throws Exception {

    //{"all":false,"properties":[],"links":[{"ne_Manager":{"all":true,"properties":[],"links":[{"ne_Room":{"all":true,"properties":[],"links":[{"nr_Building":{"all":true,"properties":[],"links":[]}}]}},{"ne_Team":{"all":true,"properties":[],"links":[]}}]}}]}
    String expected1 = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Manager\":{\"all\":true,\"properties\":[],\"links\":[{\"ne_Room\":{\"all\":true,\"properties\":[],\"links\":[{\"nr_Building\":{\"all\":true,\"properties\":[],\"links\":[]}}]}},{\"ne_Team\":{\"all\":true,\"properties\":[],\"links\":[]}}]}}]}";
    String expected2 = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Manager\":{\"all\":true,\"properties\":[],\"links\":[{\"ne_Team\":{\"all\":true,\"properties\":[],\"links\":[]}},{\"ne_Room\":{\"all\":true,\"properties\":[],\"links\":[{\"nr_Building\":{\"all\":true,\"properties\":[],\"links\":[]}}]}}]}}]}";

    //$select=ne_Manager $expand=ne_Manager/ne_Room/nr_Building,ne_Manager/ne_Team
    String actual = getExpandSelectTree("ne_Manager", "ne_Manager/ne_Room/nr_Building,ne_Manager/ne_Team").toJsonString();
    if (!expected1.equals(actual) && !expected2.equals(actual)) {
      fail("Either " + expected1 + " or " + expected2 + " expected but was: " + actual);
    }

    //$select=ne_Manager $expand=ne_Manager/ne_Team,ne_Manager/ne_Room/nr_Building
    actual = getExpandSelectTree("ne_Manager", "ne_Manager/ne_Team,ne_Manager/ne_Room/nr_Building").toJsonString();
    if (!expected1.equals(actual) && !expected2.equals(actual)) {
      fail("Either " + expected1 + " or " + expected2 + " expected but was: " + actual);
    }

    //$select=ne_Manager,ne_Manager/ne_Team/Id $expand=ne_Manager/ne_Team,ne_Manager/ne_Room/nr_Building
    actual = getExpandSelectTree("ne_Manager,ne_Manager/ne_Team/Id", "ne_Manager/ne_Team,ne_Manager/ne_Room/nr_Building").toJsonString();
    if (!expected1.equals(actual) && !expected2.equals(actual)) {
      fail("Either " + expected1 + " or " + expected2 + " expected but was: " + actual);
    }

    //$select=ne_Manager/ne_Team/Id,ne_Manager $expand=ne_Manager/ne_Team,ne_Manager/ne_Room/nr_Building
    actual = getExpandSelectTree("ne_Manager/ne_Team/Id,ne_Manager", "ne_Manager/ne_Team,ne_Manager/ne_Room/nr_Building").toJsonString();
    if (!expected1.equals(actual) && !expected2.equals(actual)) {
      fail("Either " + expected1 + " or " + expected2 + " expected but was: " + actual);
    }
  }

  private ExpandSelectTreeNodeImpl getExpandSelectTree(final String selectString, final String expandString) throws Exception {
    final List<PathSegment> pathSegments = MockFacade.getPathSegmentsAsODataPathSegmentMock(Arrays.asList("Employees('1')"));

    Map<String, String> queryParameters = new HashMap<String, String>();
    if (selectString != null)
      queryParameters.put("$select", selectString);
    if (expandString != null)
      queryParameters.put("$expand", expandString);

    final UriInfo uriInfo = UriParser.parse(edm, pathSegments, queryParameters);

    ExpandSelectTreeCreator expandSelectTreeCreator = new ExpandSelectTreeCreator(uriInfo.getSelect(), uriInfo.getExpand());
    ExpandSelectTreeNode expandSelectTree = expandSelectTreeCreator.create();
    assertNotNull(expandSelectTree);
    return (ExpandSelectTreeNodeImpl) expandSelectTree;
  }
}
