package com.sap.core.odata.ref.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.sap.core.odata.api.exception.ODataException;

/**
 * @author SAP AG
 */
public class MiscReadonlyTest extends AbstractTest {

  private void checkUrl(final String urlString) throws ODataException {
    final Response response = ok(urlString);
    assertNotNull(response);
  }

  @Test
  public void checkUrls() throws Exception {
    checkUrl("/");

    checkUrl("Managers('1')/$links/nm_Employees");
    checkUrl("Managers('1')/$links/nm_Employees()");
    checkUrl("Managers('1')/$links/nm_Employees('2')");
    checkUrl("Employees('1')/ne_Room/nr_Employees");
    checkUrl("Employees('1')/ne_Room/nr_Employees()");
    checkUrl("Employees('2')/ne_Team/nt_Employees('1')");

    //    checkUrl("Employees('2')/ne_Team/nt_Employees('1')/Location");
    //    checkUrl("Employees('2')/ne_Team/nt_Employees('1')/Location/City/CityName");
    //    checkUrl("Employees('2')/ne_Team/nt_Employees('1')/Location/City/CityName/$value");
    checkUrl("Employees('2')/ne_Team/nt_Employees('1')/$links/ne_Room");
    checkUrl("Employees('2')/ne_Team/nt_Employees('1')/ne_Room/$links/nr_Employees");

    checkUrl("Employees('2')/ne_Team/nt_Employees('3')/ne_Room");
    checkUrl("Employees('2')/ne_Team/nt_Employees('3')/ne_Room/nr_Employees");
    checkUrl("Employees('2')/ne_Manager");
    checkUrl("Employees('2')/ne_Manager/$links/nm_Employees()");
    checkUrl("Employees('2')/ne_Manager/nm_Employees('3')");
    //    checkUrl("Employees('2')/ne_Manager/nm_Employees('3')/Age");
  }

  @Test
  public void count() throws Exception {
    // assertEquals("103", ok("Rooms()/$count").getEntity());
    assertEquals("4", ok("Rooms('2')/nr_Employees/$count").getEntity());
    assertEquals("1", ok("Employees('1')/ne_Room/$count").getEntity());
    assertEquals("1", ok("Managers('3')/nm_Employees('5')/$count").getEntity());
    assertEquals("4", ok("Rooms('2')/$links/nr_Employees/$count").getEntity());
    assertEquals("1", ok("Employees('1')/$links/ne_Room/$count").getEntity());
    assertEquals("1", ok("Managers('3')/$links/nm_Employees('5')/$count").getEntity());

    // badRequest("Rooms('1')/Seats/$count");
    notFound("Managers('3')/nm_Employees('1')/$count");
  }
}
