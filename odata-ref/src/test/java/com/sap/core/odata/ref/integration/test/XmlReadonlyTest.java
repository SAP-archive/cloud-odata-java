package com.sap.core.odata.ref.integration.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author SAP AG
 */
public class XmlReadonlyTest extends AbstractTest {

  private static final String EMPLOYEE_1_NAME = "Walter Winter";
  private static final String EMPLOYEE_2_NAME = "Frederic Fall";
  private static final String EMPLOYEE_3_NAME = "Jonathan Smith";
  private static final String EMPLOYEE_4_NAME = "Peter Burke";
  private static final String EMPLOYEE_5_NAME = "John Field";
  private static final String EMPLOYEE_6_NAME = "Susan Bay";
  private static final String MANAGER_NAME = EMPLOYEE_1_NAME;
  private static final String EMPLOYEE_2_AGE = "32";
  private static final String EMPLOYEE_3_AGE = "56";
  private static final String EMPLOYEE_6_AGE = "29";
  private static final String CITY_2_NAME = "Walldorf";

  @Test
  public void functionImport() throws Exception {
    // TODO: check content type
    // assertEquals("1", call("EmployeeSearch('1')/ne_Room/Id/$value?q='alter'").getEntity()); + etag
    // assertThat(call("EmployeeSearch?q='-'").getEntity());  // contains no entity
    assertTrue(ok("AllLocations").getEntity().toString().contains(CITY_2_NAME));
    assertTrue(ok("AllUsedRoomIds").getEntity().toString().contains("3"));
    assertTrue(ok("MaximalAge").getEntity().toString().contains(EMPLOYEE_3_AGE));
    assertTrue(ok("MostCommonLocation").getEntity().toString().contains(CITY_2_NAME));
    // assertThat(ok("ManagerPhoto?Id='1'", null, null).getEntity());  // is not empty
    // assertThat(ok("ManagerPhoto/$value?Id='1'", null, null).getEntity());  // is not empty + etag + contentType
    assertTrue(ok("OldestEmployee").getEntity().toString().contains(EMPLOYEE_3_NAME));
    // assertTrue(ok("OldestEmployee?$format=xml").getEntity().toString().contains(EMPLOYEE_3_NAME));

    // badRequest("AllLocations/$count");
    // badRequest("AllUsedRoomIds/$value");
    // badRequest("MaximalAge()");
    // badRequest("MostCommonLocation/City/CityName");
    // notFound("ManagerPhoto");
    // badRequest("OldestEmployee()");
    // notFound("ManagerPhoto?Id='2'");
  }
}