package com.sap.core.odata.ref.fit.test;

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
    // assertEquals("1", call("EmployeeSearch('1')/ne_Room/Id/$value?q='alter'", null, null).getEntity()); + etag
    // assertThat(call("EmployeeSearch?q='-'", null, null).getEntity());  // contains no entity
    assertTrue(call("AllLocations", null, null).getEntity().toString().contains(CITY_2_NAME));
    assertTrue(call("AllUsedRoomIds", null, null).getEntity().toString().contains("3"));
    assertTrue(call("MaximalAge", null, null).getEntity().toString().contains(EMPLOYEE_3_AGE));
    assertTrue(call("MostCommonLocation", null, null).getEntity().toString().contains(CITY_2_NAME));
    // assertThat(call("ManagerPhoto?Id='1'", null, null).getEntity());  // is not empty
    // assertThat(call("ManagerPhoto/$value?Id='1'", null, null).getEntity());  // is not empty + etag + contentType
    assertTrue(call("OldestEmployee", null, null).getEntity().toString().contains(EMPLOYEE_3_NAME));
    // assertTrue(call("OldestEmployee?$format=xml", null, null).getEntity().toString().contains(EMPLOYEE_3_NAME));
    // TODO: negative tests
  }
}