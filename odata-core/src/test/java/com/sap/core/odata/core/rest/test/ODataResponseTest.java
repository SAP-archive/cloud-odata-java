package com.sap.core.odata.core.rest.test;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.sap.core.odata.api.rest.ODataResponse;



public class ODataResponseTest {

  @Test
  public void buildStatusResponseTest() {
    ODataResponse response = ODataResponse.status(123).build();
    assertEquals(123, response.getStatus());
  }

  @Test
  public void buildEntityResponseTest() {
    ODataResponse response = ODataResponse.entity("abc").build();
    assertEquals(200, response.getStatus());
    assertEquals("abc", response.getEntity());    
  }

  @Test
  public void buildHeaderResponseTest() {
    ODataResponse response = ODataResponse
        .header("abc", "123")
        .header("def", "456")
        .build();
    assertEquals(200, response.getStatus());
    assertEquals("123", response.getHeader("abc"));
    assertEquals("456", response.getHeader("def"));
  }
  
}
