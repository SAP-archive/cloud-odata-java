package com.sap.core.odata.fit.ref.test;

import static org.junit.Assert.*;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.testutils.helper.StringHelper;

public class MiscReadOnlyTest extends AbstractRefTest {

  public String checkUri(String uri) throws Exception {
    HttpResponse response = callUri(uri, HttpStatusCodes.OK);
    assertNotNull(response);
    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    return payload;
  }

  @Test
  public void checkUrls() throws Exception {
    checkUri("/");

    checkUri("Managers('1')/$links/nm_Employees");
    checkUri("Managers('1')/$links/nm_Employees()");
    checkUri("Managers('1')/$links/nm_Employees('2')");
    //checkUri("Employees('1')/ne_Room/nr_Employees");
    //checkUri("Employees('1')/ne_Room/nr_Employees()");
    //checkUri("Employees('2')/ne_Team/nt_Employees('1')");

   // checkUri("Employees('2')/ne_Team/nt_Employees('1')/Location");
    checkUri("Employees('2')/ne_Team/nt_Employees('1')/Location/City/CityName");
    checkUri("Employees('2')/ne_Team/nt_Employees('1')/Location/City/CityName/$value");
    checkUri("Employees('2')/ne_Team/nt_Employees('1')/$links/ne_Room");
    checkUri("Employees('2')/ne_Team/nt_Employees('1')/ne_Room/$links/nr_Employees");

    //checkUri("Employees('2')/ne_Team/nt_Employees('3')/ne_Room");
    //checkUri("Employees('2')/ne_Team/nt_Employees('3')/ne_Room/nr_Employees");
    //checkUri("Employees('2')/ne_Manager");
    checkUri("Employees('2')/ne_Manager/$links/nm_Employees()");
    //checkUri("Employees('2')/ne_Manager/nm_Employees('3')");
    checkUri("Employees('2')/ne_Manager/nm_Employees('3')/Age");
  }

  @Test
  public void count() throws Exception {
    assertEquals("103", checkUri("Rooms()/$count"));
    assertEquals("4", checkUri("Rooms('2')/nr_Employees/$count"));
    assertEquals("1", checkUri("Employees('1')/ne_Room/$count"));
    assertEquals("1", checkUri("Managers('3')/nm_Employees('5')/$count"));
    assertEquals("4", checkUri("Rooms('2')/$links/nr_Employees/$count"));
    assertEquals("1", checkUri("Employees('1')/$links/ne_Room/$count"));
    assertEquals("1", checkUri("Managers('3')/$links/nm_Employees('5')/$count"));

    // badRequest("Rooms('1')/Seats/$count");
    //notFound("Managers('3')/nm_Employees('1')/$count");
  }

  @Test
  public void mediaResource() throws Exception {
    checkUri("Employees('3')/$value");
    //    checkMediaType(response, IMAGE_JPEG);
    //    assertNull(response.getEntityTag());

    checkUri("Managers('1')/$value");
    //    checkMediaType(response, IMAGE_JPEG);
    //    assertNull(response.getEntityTag());
    //    final byte[] expected = (byte[]) response.getEntity();

    checkUri("Employees('2')/ne_Manager/$value");
    //    checkMediaType(response, IMAGE_JPEG);
    //    assertNull(response.getEntityTag());
    //    assertEquals(expected.length, ((byte[]) response.getEntity()).length);

    //    notFound("Employees('99')/$value");
    // badRequest("Teams('3')/$value");
  }
}
