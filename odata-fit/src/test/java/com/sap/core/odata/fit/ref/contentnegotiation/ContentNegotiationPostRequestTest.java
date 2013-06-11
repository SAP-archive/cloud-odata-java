package com.sap.core.odata.fit.ref.contentnegotiation;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.core.uri.UriType;

/**
 * @author SAP AG
 */
public class ContentNegotiationPostRequestTest extends AbstractContentNegotiationTest {

  public static final String EMPLOYEE_1_XML =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/\"  m:etag=\"W/&quot;1&quot;\">" +
          "<id>http://localhost:19000/Employees('1')</id>" +
          "<title type=\"text\">Walter Winter</title>" +
          "<updated>1999-01-01T00:00:00Z</updated>" +
          "<category term=\"RefScenario.Employee\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/>" +
          "<link href=\"Employees('1')\" rel=\"edit\" title=\"Employee\"/>" +
          "<link href=\"Employees('1')/$value\" rel=\"edit-media\" type=\"application/octet-stream\" etag=\"mmEtag\"/>" +
          "<link href=\"Employees('1')/ne_Room\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/ne_Room\" type=\"application/atom+xml; type=entry\" title=\"ne_Room\"/>" +
          "<link href=\"Employees('1')/ne_Manager\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/ne_Manager\" type=\"application/atom+xml; type=entry\" title=\"ne_Manager\"/>" +
          "<link href=\"Employees('1')/ne_Team\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/ne_Team\" type=\"application/atom+xml; type=entry\" title=\"ne_Team\"/>" +
          "<content type=\"application/octet-stream\" src=\"Employees('1')/$value\"/>" +
          "<m:properties>" +
          "<d:EmployeeId>1</d:EmployeeId>" +
          "<d:EmployeeName>Walter Winter</d:EmployeeName>" +
          "<d:ManagerId>1</d:ManagerId>" +
          "<d:RoomId>1</d:RoomId>" +
          "<d:TeamId>1</d:TeamId>" +
          "<d:Location m:type=\"RefScenario.c_Location\">" +
          "<d:Country>Germany</d:Country>" +
          "<d:City m:type=\"RefScenario.c_City\">" +
          "<d:PostalCode>69124</d:PostalCode>" +
          "<d:CityName>Heidelberg</d:CityName>" +
          "</d:City>" +
          "</d:Location>" +
          "<d:Age>52</d:Age>" +
          "<d:EntryDate>1999-01-01T00:00:00</d:EntryDate>" +
          "<d:ImageUrl>/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg</d:ImageUrl>" +
          "</m:properties>" +
          "</entry>";

  private static final String ROOM_1_XML =
      "<?xml version='1.0' encoding='UTF-8'?>" +
          "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/test/\" m:etag=\"W/&quot;1&quot;\">" +
          "<id>http://localhost:19000/test/Rooms('1')</id>" +
          "<title type=\"text\">Room 1</title>" +
          "<updated>2013-01-11T13:50:50.541+01:00</updated>" +
          "<category term=\"RefScenario.Room\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/>" +
          "<link href=\"Rooms('1')\" rel=\"edit\" title=\"Room\"/>" +
          "<link href=\"Rooms('1')/nr_Employees\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/nr_Employees\" type=\"application/atom+xml; type=feed\" title=\"nr_Employees\"/>" +
          "<link href=\"Rooms('1')/nr_Building\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/nr_Building\" type=\"application/atom+xml; type=entry\" title=\"nr_Building\"/>" +
          "<content type=\"application/xml\">" +
          "<m:properties>" +
          "<d:Id>1</d:Id>" +
          "<d:Name>Room 1</d:Name>" +
          "<d:Seats>1</d:Seats>" +
          "<d:Version>1</d:Version>" +
          "</m:properties>" +
          "</content>" +
          "</entry>";

  @Test
  @Ignore("Currently ignored because of bad jenkins server performance")
  public void testURI_1_EntitySet_GET() throws Exception {
    // create test set
    FitTestSet testSet = FitTestSet.create(UriType.URI1, "/Employees", false, true, false).init();

    // set specific response 'Content-Type's for '$format'
    testSet.setTestParam(Arrays.asList("application/xml", "application/xml; charset=utf-8"), 200, "application/xml; charset=utf-8");
    testSet.setTestParam(Arrays.asList("", "application/atom+xml", "application/atom+xml; charset=utf-8"),
        200, "application/atom+xml; type=feed; charset=utf-8");

    // set all 'NOT ACCEPTED' requests
    final List<String> notAcceptedHeaderValues = Arrays.asList(
        "text/plain",
        "text/plain; charset=utf-8",
        "application/atomsvc+xml",
        "application/atomsvc+xml; charset=utf-8"
        );
    testSet.setTestParam(notAcceptedHeaderValues, 406, "application/xml");

    //
    final List<String> notAcceptedJsonHeaderValues = Arrays.asList(
        "application/json",
        "application/json; charset=utf-8"
        );
    // TODO: check which behavior is currently wanted
    testSet.setTestParam(notAcceptedJsonHeaderValues, 406, "application/json");

    // execute all defined tests
    testSet.execute(getEndpoint());
  }

  @Test
  @Ignore("Currently ignored because of bad jenkins server performance")
  public void testURI_1_EntitySet() throws Exception {
    // create test set
    FitTestSet testSet = FitTestSet.create(UriType.URI1, "/Rooms")
        .httpMethod("POST")
        .queryOptions(Arrays.asList(""))
        .acceptHeader(Arrays.asList("", "application/xml"))
        .content(ROOM_1_XML)
        .requestContentTypes(CONTENT_TYPE_VALUES)
        //        .requestContentTypes(Arrays.asList("application/xml; charset=utf-8", "application/xml"))
        .expectedStatusCode(201)
        .init();

    //
    testSet.setTestParam(Arrays.asList(""), 201, "application/atom+xml; type=entry; charset=utf-8");

    // set all 'NOT ACCEPTED' requests
    final List<String> unsupportedRequestContentTypes = Arrays.asList(
        "text/plain",
        "text/plain; charset=utf-8",
        //        "application/json",
        //        "application/json; charset=utf-8",
        "application/atomsvc+xml",
        "application/atomsvc+xml; charset=utf-8"
        );
    testSet.modifyRequestContentTypes(unsupportedRequestContentTypes, 415, "application/xml");
    testSet.modifyRequestContentTypes(Arrays.asList("application/json", "application/json; charset=utf-8"), 400, "application/xml");

    // execute all defined tests
    testSet.execute(getEndpoint());
  }

  @Test
  public void testURI_1_ForIssue() throws Exception {
    // create test set
    UriType uriType = UriType.URI1;
    String httpMethod = "POST";
    String path = "/Employees";
    String queryOption = "";
    String acceptHeader = "*";
    String content = "IMAGE_;o)";
    String requestContentType = "image/jpeg";
    int expectedStatusCode = 201;
    String expectedContentType = "application/atom+xml; charset=utf-8; type=entry";

    FitTest test = FitTest.create(uriType, httpMethod, path, queryOption, acceptHeader, content, requestContentType, expectedStatusCode, expectedContentType);

    test.execute(getEndpoint());
  }

  @Test
  @Ignore("What is expected service behavior?")
  public void testURI_2_ForIssue() throws Exception {
    // create test set
    UriType uriType = UriType.URI2;
    String httpMethod = "PUT";
    String path = "/Employees('1')";
    String queryOption = "";
    String acceptHeader = "*";
    String content = "IMAGE_;o)";
    String requestContentType = "image/jpeg";
    int expectedStatusCode = 201;
    String expectedContentType = "image/jpeg";

    FitTest test = FitTest.create(uriType, httpMethod, path, queryOption, acceptHeader, content, requestContentType, expectedStatusCode, expectedContentType);

    test.execute(getEndpoint());
  }

  @Test
  @Ignore("Some test failure")
  public void testURI_17_EntitySetWithMediaResource() throws Exception {
    // create test set
    FitTestSet testSet = FitTestSet.create(UriType.URI17, "/Employees('1')")
        .httpMethod("POST")
        .queryOptions(Arrays.asList(""))
        .acceptHeader(Arrays.asList("", "image/jpeg"))
        .content("Jon Doe")
        .requestContentTypes(Arrays.asList("image/jpeg"))
        .expectedStatusCode(201)
        .init();

    // set all 'NOT ACCEPTED' requests
    final List<String> unsupportedRequestContentTypes = Arrays.asList(
        "text/plain",
        "text/plain; charset=utf-8",
        "application/xml",
        "application/xml; charset=utf-8",
        "application/json",
        "application/json; charset=utf-8",
        "application/atomsvc+xml",
        "application/atomsvc+xml; charset=utf-8"
        );
    testSet.modifyRequestContentTypes(unsupportedRequestContentTypes, 415, "application/xml");

    // execute all defined tests
    testSet.execute(getEndpoint());
  }

  @Test
  @Ignore("Some test failure")
  public void testURI_5_EntitySet() throws Exception {
    // create test set
    FitTestSet testSet = FitTestSet.create(UriType.URI1, "/Employees('1')/EmployeeName/$value")
        .httpMethod("PUT")
        .queryOptions(Arrays.asList(""))
        .acceptHeader(Arrays.asList("", "application/xml"))
        .content("Jon Doe")
        .requestContentTypes(Arrays.asList("text/plain; charset=utf-8", "text/plain"))
        .expectedStatusCode(201)
        .init();

    // set all 'NOT ACCEPTED' requests
    final List<String> unsupportedRequestContentTypes = Arrays.asList(
        "application/xml",
        "application/xml; charset=utf-8",
        "application/json",
        "application/json; charset=utf-8",
        "application/atomsvc+xml",
        "application/atomsvc+xml; charset=utf-8"
        );
    testSet.modifyRequestContentTypes(unsupportedRequestContentTypes, 415, "application/xml");

    // execute all defined tests
    testSet.execute(getEndpoint());
  }

}
