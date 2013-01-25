package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpStatusCodes;

public class DataServiceVersionTest extends AbstractRefTest {


  @Test
  public void testDataServiceVersionCase() throws Exception {
    HttpResponse response = callUri("Employees");
    Header header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("2.0", header.getValue());
    
    header = response.getFirstHeader("DataServiceVersion");
    assertNotNull(header);
    assertEquals("2.0", header.getValue());
  }
  
  @Test
  public void testDataServiceVersionWithSemicolon() throws Exception {
    HttpResponse response = callUri("Employees", "dataserviceversion", "2.0;hallo", HttpStatusCodes.OK);
    Header header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("2.0", header.getValue());
  }

  @Test
  public void testDataServiceVersionNotSetOnEntitySet() throws Exception {
    HttpResponse response = callUri("Employees");
    Header header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("2.0", header.getValue());
  }

  @Test
  public void testDataServiceVersionSetOnEntitySet() throws Exception {
    HttpResponse response = callUri("Employees", "dataserviceversion", "2.0", HttpStatusCodes.OK);
    Header header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("2.0", header.getValue());
    getBody(response);

    response = callUri("Employees", "dataserviceversion", "1.0", HttpStatusCodes.OK);
    header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("2.0", header.getValue());
  }

  @Test
  public void testDataServiceVersionSetOnEntitySetFail() throws Exception {
    HttpResponse response = callUri("Employees", "dataserviceversion", "3.0", HttpStatusCodes.BAD_REQUEST);
    Header header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("1.0", header.getValue());
    getBody(response);
    response = callUri("$metadata", "dataserviceversion", "3.0", HttpStatusCodes.BAD_REQUEST);
    header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("1.0", header.getValue());
    getBody(response);
    response = callUri("Employees", "dataserviceversion", "4.0", HttpStatusCodes.BAD_REQUEST);
    header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("1.0", header.getValue());
    getBody(response);
    response = callUri("$metadata", "dataserviceversion", "somethingwrong", HttpStatusCodes.BAD_REQUEST);
    header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("1.0", header.getValue());
    getBody(response);
    response = callUri("$metadata", "dataserviceversion", "3.2", HttpStatusCodes.BAD_REQUEST);
    header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("1.0", header.getValue());

  }

  @Test
  public void testDataServiceVersionNotSetOnMetadata() throws Exception {
    HttpResponse response = callUri("$metadata");
    Header header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("2.0", header.getValue());
  }

  @Test
  public void testDataServiceVersionSetOnMetadata() throws Exception {
    HttpResponse response = callUri("$metadata", "dataserviceversion", "1.0", HttpStatusCodes.OK);
    Header header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("2.0", header.getValue());
    getBody(response);

    response = callUri("$metadata", "dataserviceversion", "2.0", HttpStatusCodes.OK);
    header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("2.0", header.getValue());
    getBody(response);
  }

  @Test
  public void testDataServiceVersionNotSetOnServiceDocument() throws Exception {
    HttpResponse response = callUri("");
    Header header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("1.0", header.getValue());
  }

  @Test
  public void testDataServiceVersionSetOnServiceDocument() throws Exception {
    HttpResponse response = callUri("", "dataserviceversion", "1.0", HttpStatusCodes.OK);
    Header header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("1.0", header.getValue());
    getBody(response);

    response = callUri("", "dataserviceversion", "2.0", HttpStatusCodes.OK);
    header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals("1.0", header.getValue());
  }

}
