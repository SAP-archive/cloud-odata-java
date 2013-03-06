package com.sap.core.odata.core.uri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.core.ODataPathSegmentImpl;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.mock.EdmTestProvider;

public class ExpandSelectTreeCreatorImplTest extends BaseTest {

  @Test
  public void testViaRuntimeDelegate() throws Exception {

    ExpandSelectTreeNode test = UriParser.createExpandSelectTree(null, null);
    assertNotNull(test);
  }

  @Test
  public void allNull() throws Exception {
    //{"all":true,"properties":[],"links":[]}
    String expected = "{\"all\":true,\"properties\":[],\"links\":[]}";
    String actual = getExpandSelectTree(null, null);
    assertEquals(expected, actual);
  }

  @Test
  public void oneProperty() throws Exception {
    //{"all":false,"properties":["Age"],"links":[]}
    String expected = "{\"all\":false,\"properties\":[\"Age\"],\"links\":[]}";

    //$select=Age
    String actual = getExpandSelectTree("Age", null);
    assertEquals(expected, actual);

  }

  @Ignore
  @Test
  public void onePropertyOneExpandLink() throws Exception {
    //{"all":false,"properties":["Age"],"links":[]}
    String expected = "{\"all\":false,\"properties\":[\"Age\"],\"links\":[]}";

    //$select=Age&$expand=ne_Room
    String actual = getExpandSelectTree("Age", "ne_Room");
    assertEquals(expected, actual);

  }

  @Test
  public void starAndExpandLink() throws Exception {
    //{"all":true,"properties":[],"links":[]}
    String expected = "{\"all\":true,\"properties\":[],\"links\":[]}";

    //$select=*&$expand=ne_Room
    String actual = getExpandSelectTree("*", "ne_Room");
    assertEquals(expected, actual);

  }

  @Test
  public void oneSelectLink() throws Exception {
    //{"all":false,"properties":[],"links":[{"ne_Room":null}]}
    String expected = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Room\":null}]}";

    //$select=ne_Room
    String actual = getExpandSelectTree("ne_Room", null);
    assertEquals(expected, actual);

  }

  @Ignore
  @Test
  public void sameLinkInSelectAndExpand() throws Exception {
    //{"all":false,"properties":[],"links":[{"ne_Room":{"all":true,"properties":[],"links":[]}}]}
    String expected = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Room\":{\"all\":true,\"properties\":[],\"links\":[]}}]}";

    //$select=ne_Room&$expand=ne_Room
    String actual = getExpandSelectTree("ne_Room", "ne_Room");
    assertEquals(expected, actual);

    //$select=ne_Room/*&$expand=ne_Room
    actual = getExpandSelectTree("ne_Room/*", "ne_Room");
    assertEquals(expected, actual);

  }

  @Test
  public void expandOneLink() throws Exception {
    //{"all":true,"properties":[],"links":[{"ne_Room":{"all":true,"properties":[],"links":[]}}]}
    String expected = "{\"all\":true,\"properties\":[],\"links\":[{\"ne_Room\":{\"all\":true,\"properties\":[],\"links\":[]}}]}";

    //$expand=ne_Room
    String actual = getExpandSelectTree(null, "ne_Room");
    assertEquals(expected, actual);
  }

  @Ignore
  @Test
  public void complexSelectExpand() throws Exception {
    //{"all":false,"properties":["Age"],"links":[{"ne_Room":{"all":false,"properties":["Seats"],"links":[]}},{"ne_Team":null},{"ne_Manager":{"all":true,"properties":[],"links":[{"ne_Team":{"all":true,"properties":[],"links":[{"nt_Employees":{"all":true,"properties":[],"links":[]}}]}}]}}]}
    String expected = "{\"all\":false,\"properties\":[\"Age\"],\"links\":[{\"ne_Room\":{\"all\":false,\"properties\":[\"Seats\"],\"links\":[]}},{\"ne_Team\":null},{\"ne_Manager\":{\"all\":true,\"properties\":[],\"links\":[{\"ne_Team\":{\"all\":true,\"properties\":[],\"links\":[{\"nt_Employees\":{\"all\":true,\"properties\":[],\"links\":[]}}]}}]}}]}";

    String select = "Age,ne_Room/Seats,ne_Team/Name,ne_Manager/*,ne_Manager/ne_Team,ne_Team";
    String expand = "ne_Room/nr_Building,ne_Manager/ne_Team/nt_Employees,ne_Manager/ne_Room";
    String actual = getExpandSelectTree(select, expand);
    assertEquals(expected, actual);
  }

  @Test
  public void twoProperties() throws Exception {
    //{"all":false,"properties":["Age","EmployeeId"],"links":[]}
    String expected = "{\"all\":false,\"properties\":[\"Age\",\"EmployeeId\"],\"links\":[]}";

    //$select=Age,EmployeeId
    String actual = getExpandSelectTree("Age,EmployeeId", null);
    assertEquals(expected, actual);
  }

  @Test
  public void sameProperties() throws Exception {
    //{"all":false,"properties":["EmployeeId","Age"],"links":[]}
    String expected = "{\"all\":false,\"properties\":[\"EmployeeId\",\"Age\"],\"links\":[]}";
    //$select=EmployeeId,Age,EmployeeId
    String actual = getExpandSelectTree("EmployeeId,Age,EmployeeId", null);
    assertEquals(expected, actual);
  }

  @Test
  public void propertiesAndStar() throws Exception {
    //{"all":true,"properties":[],"links":[]}
    String expected = "{\"all\":true,\"properties\":[],\"links\":[]}";

    //$select=Age,EmployeeId,*
    String actual = getExpandSelectTree("Age,EmployeeId,*", null);
    assertEquals(expected, actual);
  }

  @Test
  public void multiSelectLinkWithoutExpand() throws Exception {
    //{"all":false,"properties":[],"links":[{"ne_Manager":null}]}
    String expected = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Manager\":null}]}";

    //$select=ne_Manager
    String actual = getExpandSelectTree("ne_Manager", null);
    assertEquals(expected, actual);

    //$select=ne_Manager/ne_Manager
    actual = getExpandSelectTree("ne_Manager/ne_Manager", null);
    assertEquals(expected, actual);

    //$select=ne_Manager/ne_Manager,ne_Manager
    actual = getExpandSelectTree("ne_Manager/ne_Manager,ne_Manager", null);
    assertEquals(expected, actual);
  }

  @Test
  public void sameSelectLinks() throws Exception {
    //{"all":false,"properties":[],"links":[{"ne_Manager":null}]}
    String expected = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Manager\":null}]}";

    //$select=ne_Manager,ne_Manager
    String actual = getExpandSelectTree("ne_Manager,ne_Manager", null);
    assertEquals(expected, actual);
  }

  @Test
  public void sameExpandLinks() throws Exception {
    //{"all":true,"properties":[],"links":[{"ne_Manager":{"all":true,"properties":[],"links":[]}}]}
    String expected = "{\"all\":true,\"properties\":[],\"links\":[{\"ne_Manager\":{\"all\":true,\"properties\":[],\"links\":[]}}]}";

    //$expand=ne_Manager,ne_Manager
    String actual = getExpandSelectTree(null, "ne_Manager,ne_Manager");
    assertEquals(expected, actual);
  }
  
  @Test
  public void multiExpandLinkWithoutSelect() throws Exception {
    //{"all":true,"properties":[],"links":[{"ne_Manager":{"all":true,"properties":[],"links":[{"ne_Manager":{"all":true,"properties":[],"links":[]}}]}}]}
    String expected = "{\"all\":true,\"properties\":[],\"links\":[{\"ne_Manager\":{\"all\":true,\"properties\":[],\"links\":[{\"ne_Manager\":{\"all\":true,\"properties\":[],\"links\":[]}}]}}]}";

    //$expand=ne_Manager/ne_Manager,ne_Manager
    String actual = getExpandSelectTree(null , "ne_Manager/ne_Manager,ne_Manager");
    assertEquals(expected, actual);
  }

  //This test is ignored because Manager and room links may change positions randomly because of the HashMap in toString()
  @Ignore
  @Test
  public void twoSelectLinks() throws Exception {
    //{"all":false,"properties":[],"links":[{"ne_Manager":null},{"ne_Room":null}]}
    String expected = "{\"all\":false,\"properties\":[],\"links\":[{\"ne_Manager\":null},{\"ne_Room\":null}]}";

    //$select=ne_Manager,ne_Room
    String actual = getExpandSelectTree("ne_Manager,ne_Room", null);
    assertEquals(expected, actual);
  }

  private String getExpandSelectTree(String selectString, String expandString) throws Exception {

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
    UriInfoImpl uriInfo = (UriInfoImpl) uriParser.parse(pathSegments, queryParameters);

    ExpandSelectTreeCreatorImpl expandSelectTreeCreator = new ExpandSelectTreeCreatorImpl(uriInfo.getSelect(), uriInfo.getExpand());
    ExpandSelectTreeNode expandSelectTree = expandSelectTreeCreator.create();
    assertNotNull(expandSelectTree);
    return expandSelectTree.toString();
  }
}
