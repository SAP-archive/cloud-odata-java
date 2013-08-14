package com.sap.core.odata.core.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.batch.BatchException;
import com.sap.core.odata.api.batch.BatchRequestPart;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.ep.EntityProviderBatchProperties;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.core.ODataPathSegmentImpl;
import com.sap.core.odata.core.PathInfoImpl;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * @author SAP AG
 */
public class BatchRequestParserTest {

  private static final String LF = "\r\n";
  private static final String CONTENT_ID_REFERENCE = "NewEmployee";
  private static final String PUT_MIME_HEADER_CONTENT_ID = "BBB_MIMEPART1";
  private static final String PUT_REQUEST_HEADER_CONTENT_ID = "BBB_REQUEST1";
  private static final String SERVICE_ROOT = "http://localhost/odata/";
  private static EntityProviderBatchProperties batchProperties;
  private static final String contentType = "multipart/mixed;boundary=batch_8194-cf13-1f56";
  private static final String MIME_HEADERS = "Content-Type: application/http" + LF
      + "Content-Transfer-Encoding: binary" + LF;
  private static final String GET_REQUEST = MIME_HEADERS + LF
      + "GET Employees('1')/EmployeeName HTTP/1.1" + LF
      + LF
      + LF;

  @BeforeClass
  public static void setProperties() throws URISyntaxException {
    PathInfoImpl pathInfo = new PathInfoImpl();
    pathInfo.setServiceRoot(new URI(SERVICE_ROOT));
    batchProperties = EntityProviderBatchProperties.init().pathInfo(pathInfo).build();

  }

  @Test
  public void test() throws IOException, BatchException, URISyntaxException {
    String fileName = "/batchWithPost.txt";
    InputStream in = ClassLoader.class.getResourceAsStream(fileName);
    if (in == null) {
      throw new IOException("Requested file '" + fileName + "' was not found.");
    }

    BatchRequestParser parser = new BatchRequestParser(contentType, batchProperties);
    List<BatchRequestPart> batchRequestParts = parser.parse(in);
    assertNotNull(batchRequestParts);
    assertEquals(false, batchRequestParts.isEmpty());
    for (BatchRequestPart object : batchRequestParts) {
      if (!object.isChangeSet()) {
        assertEquals(1, object.getRequests().size());
        ODataRequest retrieveRequest = object.getRequests().get(0);
        assertEquals(ODataHttpMethod.GET, retrieveRequest.getMethod());
        if (!retrieveRequest.getAcceptableLanguages().isEmpty()) {
          assertEquals(3, retrieveRequest.getAcceptableLanguages().size());
        }
        assertEquals(new URI(SERVICE_ROOT), retrieveRequest.getPathInfo().getServiceRoot());
        ODataPathSegmentImpl pathSegment = new ODataPathSegmentImpl("Employees('2')", null);
        assertEquals(pathSegment.getPath(), retrieveRequest.getPathInfo().getODataSegments().get(0).getPath());
        if (retrieveRequest.getQueryParameters().get("$format") != null) {
          assertEquals("json", retrieveRequest.getQueryParameters().get("$format"));
        }
        assertEquals(SERVICE_ROOT + "Employees('2')/EmployeeName?$format=json", retrieveRequest.getPathInfo().getRequestUri().toASCIIString());
      } else {
        List<ODataRequest> requests = object.getRequests();
        for (ODataRequest request : requests) {

          assertEquals(ODataHttpMethod.PUT, request.getMethod());
          assertEquals("100000", request.getRequestHeaderValue(HttpHeaders.CONTENT_LENGTH.toLowerCase()));
          assertEquals("application/json;odata=verbose", request.getContentType());
          assertEquals(3, request.getAcceptHeaders().size());
          assertNotNull(request.getAcceptableLanguages());
          assertTrue(request.getAcceptableLanguages().isEmpty());
          assertEquals("*/*", request.getAcceptHeaders().get(2));
          assertEquals("application/atomsvc+xml", request.getAcceptHeaders().get(0));
          assertEquals(new URI(SERVICE_ROOT + "Employees('2')/EmployeeName").toASCIIString(), request.getPathInfo().getRequestUri().toASCIIString());

          ODataPathSegmentImpl pathSegment = new ODataPathSegmentImpl("Employees('2')", null);
          assertEquals(pathSegment.getPath(), request.getPathInfo().getODataSegments().get(0).getPath());
          ODataPathSegmentImpl pathSegment2 = new ODataPathSegmentImpl("EmployeeName", null);
          assertEquals(pathSegment2.getPath(), request.getPathInfo().getODataSegments().get(1).getPath());

        }
      }
    }
  }

  @Test
  public void testImageInContent() throws IOException, BatchException, URISyntaxException {
    String fileName = "/batchWithContent.txt";
    InputStream contentInputStream = ClassLoader.class.getResourceAsStream(fileName);
    if (contentInputStream == null) {
      throw new IOException("Requested file '" + fileName + "' was not found.");
    }
    String content = StringHelper.inputStreamToString(contentInputStream);
    String batch = LF
        + "--batch_8194-cf13-1f56" + LF
        + "Content-Type: multipart/mixed; boundary=changeset_f980-1cb6-94dd" + LF
        + LF
        + "--changeset_f980-1cb6-94dd" + LF
        + "content-type:     Application/http" + LF
        + "content-transfer-encoding: Binary" + LF
        + "Content-ID: 1" + LF
        + LF
        + "POST Employees HTTP/1.1" + LF
        + "Content-length: 100000" + LF
        + "Content-type: application/octet-stream" + LF
        + LF
        + content + LF
        + LF
        + "--changeset_f980-1cb6-94dd--" + LF
        + LF
        + "--batch_8194-cf13-1f56" + LF
        + MIME_HEADERS
        + LF
        + "GET Employees?$filter=Age%20gt%2040 HTTP/1.1" + LF
        + "Accept: application/atomsvc+xml;q=0.8, application/json;odata=verbose;q=0.5, */*;q=0.1" + LF
        + "MaxDataServiceVersion: 2.0" + LF
        + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    List<BatchRequestPart> BatchRequestParts = parse(batch);
    for (BatchRequestPart object : BatchRequestParts) {
      if (!object.isChangeSet()) {
        assertEquals(1, object.getRequests().size());
        ODataRequest retrieveRequest = object.getRequests().get(0);
        assertEquals(ODataHttpMethod.GET, retrieveRequest.getMethod());
        assertEquals("Age gt 40", retrieveRequest.getQueryParameters().get("$filter"));
        assertEquals(new URI("http://localhost/odata/Employees?$filter=Age%20gt%2040"), retrieveRequest.getPathInfo().getRequestUri());
      } else {
        List<ODataRequest> requests = object.getRequests();
        for (ODataRequest request : requests) {
          assertEquals(ODataHttpMethod.POST, request.getMethod());
          assertEquals("100000", request.getRequestHeaderValue(HttpHeaders.CONTENT_LENGTH.toLowerCase()));
          assertEquals("1", request.getRequestHeaderValue(BatchHelper.MIME_HEADER_CONTENT_ID.toLowerCase()));
          assertEquals("application/octet-stream", request.getContentType());
          InputStream body = request.getBody();
          assertEquals(content, StringHelper.inputStreamToString(body));

        }

      }
    }
  }

  @Test
  public void testPostWithoutBody() throws IOException, BatchException, URISyntaxException {
    String fileName = "/batchWithContent.txt";
    InputStream contentInputStream = ClassLoader.class.getResourceAsStream(fileName);
    if (contentInputStream == null) {
      throw new IOException("Requested file '" + fileName + "' was not found.");
    }
    StringHelper.inputStreamToString(contentInputStream);
    String batch = LF
        + "--batch_8194-cf13-1f56" + LF
        + "Content-Type: multipart/mixed; boundary=changeset_f980-1cb6-94dd" + LF
        + LF
        + "--changeset_f980-1cb6-94dd" + LF
        + MIME_HEADERS
        + LF
        + "POST Employees('2') HTTP/1.1" + LF
        + "Content-Length: 100" + LF
        + "Content-Type: application/octet-stream" + LF
        + LF
        + LF
        + "--changeset_f980-1cb6-94dd--" + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    List<BatchRequestPart> batchRequestParts = parse(batch);
    for (BatchRequestPart object : batchRequestParts) {
      if (object.isChangeSet()) {
        List<ODataRequest> requests = object.getRequests();
        for (ODataRequest request : requests) {
          assertEquals(ODataHttpMethod.POST, request.getMethod());
          assertEquals("100", request.getRequestHeaderValue(HttpHeaders.CONTENT_LENGTH.toLowerCase()));
          assertEquals("application/octet-stream", request.getContentType());
          assertNotNull(request.getBody());
        }
      }
    }
  }

  @Test
  public void testBoundaryParameterWithQuotas() throws BatchException {
    String contentType = "multipart/mixed; boundary=\"batch_1.2+34:2j)0?\"";

    String batch = "--batch_1.2+34:2j)0?" + LF
        + GET_REQUEST
        + "--batch_1.2+34:2j)0?--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser(contentType, batchProperties);
    List<BatchRequestPart> batchRequestParts = parser.parse(in);
    assertNotNull(batchRequestParts);
    assertEquals(false, batchRequestParts.isEmpty());
  }

  @Test(expected = BatchException.class)
  public void testBatchWithInvalidContentType() throws BatchException {
    String invalidContentType = "multipart;boundary=batch_1740-bb84-2f7f";

    String batch = "--batch_1740-bb84-2f7f" + LF
        + GET_REQUEST
        + "--batch_1740-bb84-2f7f--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser(invalidContentType, batchProperties);
    parser.parse(in);
  }

  @Test(expected = BatchException.class)
  public void testBatchWithoutBoundaryParameter() throws BatchException {
    String invalidContentType = "multipart/mixed";
    String batch = "--batch_1740-bb84-2f7f" + LF
        + GET_REQUEST
        + "--batch_1740-bb84-2f7f--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser(invalidContentType, batchProperties);
    parser.parse(in);
  }

  @Test(expected = BatchException.class)
  public void testBoundaryParameterWithoutQuota() throws BatchException {
    String invalidContentType = "multipart;boundary=batch_1740-bb:84-2f7f";
    String batch = "--batch_1740-bb:84-2f7f" + LF
        + GET_REQUEST
        + "--batch_1740-bb:84-2f7f--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser(invalidContentType, batchProperties);
    parser.parse(in);
  }

  @Test(expected = BatchException.class)
  public void testWrongBoundaryString() throws BatchException {
    String batch = "--batch_8194-cf13-1f5" + LF
        + GET_REQUEST
        + "--batch_8194-cf13-1f56--";
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testBoundaryWithoutHyphen() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + GET_REQUEST
        + "batch_8194-cf13-1f56" + LF
        + GET_REQUEST
        + "--batch_8194-cf13-1f56--";
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testNoBoundaryString() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + GET_REQUEST
        //+ no boundary string
        + GET_REQUEST
        + "--batch_8194-cf13-1f56--";
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testBatchBoundaryEqualsChangeSetBoundary() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + "Content-Type: multipart/mixed;boundary=batch_8194-cf13-1f56" + LF
        + LF
        + "--batch_8194-cf13-1f56" + LF
        + MIME_HEADERS
        + LF
        + "PUT Employees('2')/EmployeeName HTTP/1.1" + LF
        + "Accept: application/atomsvc+xml;q=0.8, application/json;odata=verbose;q=0.5, */*;q=0.1" + LF
        + "Content-Type: application/json;odata=verbose" + LF
        + "MaxDataServiceVersion: 2.0" + LF
        + LF
        + "{\"EmployeeName\":\"Frederic Fall MODIFIED\"}" + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testNoContentType() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + "Content-Transfer-Encoding: binary" + LF
        + LF
        + "GET Employees('1')/EmployeeName HTTP/1.1" + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testMimeHeaderContentType() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + "Content-Type: text/plain" + LF
        + "Content-Transfer-Encoding: binary" + LF
        + LF
        + "GET Employees('1')/EmployeeName HTTP/1.1" + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testMimeHeaderEncoding() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + "Content-Type: application/http" + LF
        + "Content-Transfer-Encoding: 8bit" + LF
        + LF
        + "GET Employees('1')/EmployeeName HTTP/1.1" + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testMimeHeaderContentId() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + MIME_HEADERS
        + "Content-ID: 1" + LF
        + LF
        + "GET Employees('1')/EmployeeName HTTP/1.1" + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testInvalidMethodForBatch() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + MIME_HEADERS
        + LF
        + "POST Employees('1')/EmployeeName HTTP/1.1" + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testNoMethod() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + MIME_HEADERS
        + LF
        + /*GET*/"Employees('1')/EmployeeName HTTP/1.1" + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testInvalidMethodForChangeset() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + "Content-Type: multipart/mixed; boundary=changeset_f980-1cb6-94dd" + LF
        + LF
        + "--changeset_f980-1cb6-94dd" + LF
        + MIME_HEADERS
        + LF
        + "GET Employees('2')/EmployeeName HTTP/1.1" + LF
        + "Content-Type: application/json;odata=verbose" + LF
        + "MaxDataServiceVersion: 2.0" + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testInvalidChangeSetBoundary() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + "Content-Type: multipart/mixed;boundary=changeset_f980-1cb6-94dd" + LF
        + LF
        + "--changeset_f980-1cb6-94d"/*+"d"*/+ LF
        + MIME_HEADERS
        + LF
        + "POST Employees('2') HTTP/1.1" + LF
        + "Content-Type: application/json;odata=verbose" + LF
        + "MaxDataServiceVersion: 2.0" + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testNoCloseDelimiter() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + GET_REQUEST;
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testNoCloseDelimiter2() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + MIME_HEADERS
        + LF
        + "GET Employees('1')/EmployeeName HTTP/1.1" + LF;
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testInvalidUri() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + MIME_HEADERS
        + LF
        + "GET http://localhost/aa/odata/Employees('1')/EmployeeName HTTP/1.1" + LF
        + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testUriWithAbsolutePath() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + MIME_HEADERS
        + LF
        + "GET /odata/Employees('1')/EmployeeName HTTP/1.1" + LF
        + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    parseInvalidBatchBody(batch);
  }

  @Test(expected = BatchException.class)
  public void testNoCloseDelimiter3() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF + GET_REQUEST + "--batch_8194-cf13-1f56-"/*no hash*/;
    parseInvalidBatchBody(batch);
  }

  @Test
  public void testAcceptHeaders() throws BatchException, URISyntaxException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + MIME_HEADERS
        + LF
        + "GET Employees('2')/EmployeeName HTTP/1.1" + LF
        + "Content-Length: 100000" + LF
        + "Content-Type: application/json;odata=verbose" + LF
        + "Accept: application/xml;q=0.3, application/atomsvc+xml;q=0.8, application/json;odata=verbose;q=0.5, */*;q=0.001" + LF
        + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    List<BatchRequestPart> batchRequestParts = parse(batch);
    for (BatchRequestPart multipart : batchRequestParts) {
      if (!multipart.isChangeSet()) {
        assertEquals(1, multipart.getRequests().size());
        ODataRequest retrieveRequest = multipart.getRequests().get(0);
        assertEquals(ODataHttpMethod.GET, retrieveRequest.getMethod());
        assertNotNull(retrieveRequest.getAcceptHeaders());
        assertEquals(4, retrieveRequest.getAcceptHeaders().size());
        assertEquals("application/atomsvc+xml", retrieveRequest.getAcceptHeaders().get(0));
        assertEquals("*/*", retrieveRequest.getAcceptHeaders().get(3));
      }

    }
  }

  @Test
  public void testAcceptHeaders2() throws BatchException, URISyntaxException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + MIME_HEADERS
        + LF
        + "GET Employees('2')/EmployeeName HTTP/1.1" + LF
        + "Content-Length: 100000" + LF
        + "Content-Type: application/json;odata=verbose" + LF
        + "Accept: */*;q=0.5, application/json;odata=verbose;q=1.0,application/atom+xml" + LF
        + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    List<BatchRequestPart> batchRequestParts = parse(batch);
    for (BatchRequestPart multipart : batchRequestParts) {
      if (!multipart.isChangeSet()) {
        assertEquals(1, multipart.getRequests().size());
        ODataRequest retrieveRequest = multipart.getRequests().get(0);
        assertEquals(ODataHttpMethod.GET, retrieveRequest.getMethod());
        assertNotNull(retrieveRequest.getAcceptHeaders());
        assertEquals(3, retrieveRequest.getAcceptHeaders().size());
        assertEquals("application/json;odata=verbose", retrieveRequest.getAcceptHeaders().get(0));
        assertEquals("application/atom+xml", retrieveRequest.getAcceptHeaders().get(1));
        assertEquals("*/*", retrieveRequest.getAcceptHeaders().get(2));
      }

    }
  }

  @Test
  public void testAcceptHeaders3() throws BatchException, URISyntaxException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + MIME_HEADERS
        + LF
        + "GET Employees('2')/EmployeeName HTTP/1.1" + LF
        + "Content-Length: 100000" + LF
        + "Content-Type: application/json;odata=verbose" + LF
        + "accept: */*,application/atom+xml,application/atomsvc+xml,application/xml" + LF
        + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    List<BatchRequestPart> batchRequestParts = parse(batch);
    for (BatchRequestPart multipart : batchRequestParts) {
      if (!multipart.isChangeSet()) {
        assertEquals(1, multipart.getRequests().size());
        ODataRequest retrieveRequest = multipart.getRequests().get(0);
        assertEquals(ODataHttpMethod.GET, retrieveRequest.getMethod());
        assertNotNull(retrieveRequest.getAcceptHeaders());
        assertEquals(4, retrieveRequest.getAcceptHeaders().size());

        assertEquals("application/atom+xml", retrieveRequest.getAcceptHeaders().get(0));
        assertEquals("application/atomsvc+xml", retrieveRequest.getAcceptHeaders().get(1));

        assertEquals("application/xml", retrieveRequest.getAcceptHeaders().get(2));
      }

    }
  }

  @Test
  public void testContentId() throws BatchException {
    String batch = "--batch_8194-cf13-1f56" + LF
        + MIME_HEADERS
        + LF
        + "GET Employees HTTP/1.1" + LF
        + "accept: */*,application/atom+xml,application/atomsvc+xml,application/xml" + LF
        + "Content-Id: BBB" + LF
        + LF + LF
        + "--batch_8194-cf13-1f56" + LF
        + "Content-Type: multipart/mixed; boundary=changeset_f980-1cb6-94dd" + LF
        + LF
        + "--changeset_f980-1cb6-94dd" + LF
        + MIME_HEADERS
        + "Content-Id: " + CONTENT_ID_REFERENCE + LF
        + LF
        + "POST Employees HTTP/1.1" + LF
        + "Content-type: application/octet-stream" + LF
        + LF
        + "/9j/4AAQSkZJRgABAQEBLAEsAAD/4RM0RXhpZgAATU0AKgAAAAgABwESAAMAAAABAAEAAAEaAAUAAAABAAAAYgEbAAUAAAA" + LF
        + LF
        + "--changeset_f980-1cb6-94dd" + LF
        + MIME_HEADERS
        + "Content-ID: " + PUT_MIME_HEADER_CONTENT_ID + LF
        + LF
        + "PUT $" + CONTENT_ID_REFERENCE + "/EmployeeName HTTP/1.1" + LF
        + "Content-Type: application/json;odata=verbose" + LF
        + "Content-Id:" + PUT_REQUEST_HEADER_CONTENT_ID + LF
        + LF
        + "{\"EmployeeName\":\"Peter Fall\"}" + LF
        + "--changeset_f980-1cb6-94dd--" + LF
        + LF
        + "--batch_8194-cf13-1f56--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser(contentType, batchProperties);
    List<BatchRequestPart> batchRequestParts = parser.parse(in);
    assertNotNull(batchRequestParts);
    for (BatchRequestPart multipart : batchRequestParts) {
      if (!multipart.isChangeSet()) {
        assertEquals(1, multipart.getRequests().size());
        ODataRequest retrieveRequest = multipart.getRequests().get(0);
        assertEquals("BBB", retrieveRequest.getRequestHeaderValue(BatchHelper.REQUEST_HEADER_CONTENT_ID.toLowerCase()));
      } else {
        for (ODataRequest request : multipart.getRequests()) {
          if (ODataHttpMethod.POST.equals(request.getMethod())) {
            assertEquals(CONTENT_ID_REFERENCE, request.getRequestHeaderValue(BatchHelper.MIME_HEADER_CONTENT_ID.toLowerCase()));
          } else if (ODataHttpMethod.PUT.equals(request.getMethod())) {
            assertEquals(PUT_MIME_HEADER_CONTENT_ID, request.getRequestHeaderValue(BatchHelper.MIME_HEADER_CONTENT_ID.toLowerCase()));
            assertEquals(PUT_REQUEST_HEADER_CONTENT_ID, request.getRequestHeaderValue(BatchHelper.REQUEST_HEADER_CONTENT_ID.toLowerCase()));
            assertNull(request.getPathInfo().getRequestUri());
            assertEquals("$" + CONTENT_ID_REFERENCE, request.getPathInfo().getODataSegments().get(0).getPath());
          }
        }
      }
    }
  }

  private List<BatchRequestPart> parse(final String batch) throws BatchException {
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser(contentType, batchProperties);
    List<BatchRequestPart> batchRequestParts = parser.parse(in);
    assertNotNull(batchRequestParts);
    assertEquals(false, batchRequestParts.isEmpty());
    return batchRequestParts;
  }

  private void parseInvalidBatchBody(final String batch) throws BatchException {
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser(contentType, batchProperties);
    parser.parse(in);
  }
}
