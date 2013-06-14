package com.sap.core.odata.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class ODataResponseTest extends BaseTest {

  @Test
  public void buildStatusResponseTest() {
    ODataResponse response = ODataResponse.status(HttpStatusCodes.FOUND).build();
    assertEquals(HttpStatusCodes.FOUND, response.getStatus());
  }

  @Test
  public void buildEntityResponseTest() {
    ODataResponse response = ODataResponse.entity("abc").build();
    assertNull(response.getStatus());
    assertEquals("abc", response.getEntity());
  }

  @Test
  public void buildHeaderResponseTest() {
    ODataResponse response = ODataResponse
        .header("abc", "123")
        .header("def", "456")
        .header("ghi", null)
        .build();
    assertNull(response.getStatus());
    assertEquals("123", response.getHeader("abc"));
    assertEquals("456", response.getHeader("def"));
    assertNull(response.getHeader("ghi"));
  }

  @Test
  public void contentHeader() {
    final ODataResponse response = ODataResponse.contentHeader(HttpContentType.APPLICATION_OCTET_STREAM).build();
    assertNull(response.getStatus());
    assertEquals(HttpContentType.APPLICATION_OCTET_STREAM, response.getContentHeader());
    assertTrue(response.containsHeader(HttpHeaders.CONTENT_TYPE));
    assertEquals(HttpContentType.APPLICATION_OCTET_STREAM, response.getHeader(HttpHeaders.CONTENT_TYPE));
    assertFalse(response.containsHeader(HttpHeaders.CONTENT_LENGTH));
    assertEquals(new HashSet<String>(Arrays.asList(HttpHeaders.CONTENT_TYPE)), response.getHeaderNames());
  }

  @Test
  public void completeResponse() {
    final ODataResponse response = ODataResponse.newBuilder()
        .status(HttpStatusCodes.OK)
        .header("def", "456")
        .eTag("x")
        .contentHeader(HttpContentType.TEXT_PLAIN)
        .idLiteral("id")
        .entity("body")
        .build();
    assertEquals(HttpStatusCodes.OK, response.getStatus());
    assertEquals("456", response.getHeader("def"));
    assertEquals("x", response.getETag());
    assertEquals(HttpContentType.TEXT_PLAIN, response.getContentHeader());
    assertEquals("id", response.getIdLiteral());
    assertEquals("body", response.getEntity());

    final ODataResponse responseCopy = ODataResponse.fromResponse(response).build();
    assertEquals(HttpStatusCodes.OK, responseCopy.getStatus());
    assertEquals("456", responseCopy.getHeader("def"));
    assertEquals("x", responseCopy.getETag());
    assertEquals(HttpContentType.TEXT_PLAIN, response.getContentHeader());
    assertEquals("id", responseCopy.getIdLiteral());
    assertEquals("body", responseCopy.getEntity());
  }
}
