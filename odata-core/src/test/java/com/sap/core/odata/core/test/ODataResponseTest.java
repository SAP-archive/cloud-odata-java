package com.sap.core.odata.core.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.processor.ODataResponse;



public class ODataResponseTest {

  @Test
  public void buildStatusResponseTest() {
    ODataResponse response = ODataResponse.status(HttpStatusCodes.FOUND).build();
    assertEquals(HttpStatusCodes.FOUND, response.getStatus());
  }

  @Test
  public void buildEntityResponseTest() {
    ODataResponse response = ODataResponse.entity("abc").build();
    assertEquals(HttpStatusCodes.OK, response.getStatus());
    assertEquals("abc", response.getEntity());    
  }

  @Test
  public void buildHeaderResponseTest() {
    ODataResponse response = ODataResponse
        .header("abc", "123")
        .header("def", "456")
        .build();
    assertEquals(HttpStatusCodes.OK, response.getStatus());
    assertEquals("123", response.getHeader("abc"));
    assertEquals("456", response.getHeader("def"));
  }
  
}
