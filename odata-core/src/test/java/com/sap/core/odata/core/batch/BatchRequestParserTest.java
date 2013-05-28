package com.sap.core.odata.core.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;

public class BatchRequestParserTest {

  @Test
  public void test() throws IOException, ODataException {
    String fileName = "/batchWithPost.txt";
    InputStream in = ClassLoader.class.getResourceAsStream(fileName);
    if (in == null) {
      throw new IOException("Requested file '" + fileName + "' was not found.");
    }
    BatchRequestParser parser = new BatchRequestParser();
    List<Object> requests = parser.parse(in);
    assertEquals(false, requests.isEmpty());
    for (Object object : requests) {
      if (object instanceof ODataRequest) {
        ODataRequest retrieveRequest = (ODataRequest) object;
        assertEquals(ODataHttpMethod.GET, retrieveRequest.getMethod());
        if (retrieveRequest.getAcceptableLanguages() != null) {
          assertEquals(3, retrieveRequest.getAcceptableLanguages().size());
        }
      } else {
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) object;
        for (Object obj2 : list) {
          if (obj2 instanceof ODataRequest) {
            ODataRequest request = (ODataRequest) obj2;
            assertEquals(ODataHttpMethod.PUT, request.getMethod());
            assertEquals("100000", request.getHeaderValue("Content-Length"));
            assertEquals("application/json; odata=verbose", request.getContentType());
            assertEquals(3, request.getAcceptHeaders().size());
            assertEquals("*/*", request.getAcceptHeaders().get(2));
            assertEquals("application/atomsvc+xml", request.getAcceptHeaders().get(0));
          }
        }
      }

    }
  }

  @Test
  public void test2() throws IOException, ODataException {
    String fileName = "/batchRequest.txt";
    InputStream in = ClassLoader.class.getResourceAsStream(fileName);
    if (in == null) {
      throw new IOException("Requested file '" + fileName + "' was not found.");
    }
    BatchRequestParser parser = new BatchRequestParser();
    assertNotNull(parser.parse(in));
  }

  @Test(expected = ODataException.class)
  public void testBatchWithInvalidContentType() throws ODataException {
    String batch = "POST /service/$batch HTTP/1.1" + "\n"
        + "Content-Type:multipart;boundary=batch_1740-bb84-2f7f" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f" + "\n"
        + "Content-Type: application/http" + "\n"
        + "Content-Transfer-Encoding: binary" + "\n"
        + "\n"
        + "GET Employees('1')/EmployeeName HTTP/1.1" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser();
    parser.parse(in);
  }

  @Test(expected = ODataException.class)
  public void testBatchWithoutBoundaryParameter() throws ODataException {
    String batch = "POST /service/$batch HTTP/1.1" + "\n"
        + "Content-Type:multipart/mixed" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f" + "\n"
        + "Content-Type: application/http" + "\n"
        + "Content-Transfer-Encoding: binary" + "\n"
        + "\n"
        + "GET Employees('1')/EmployeeName HTTP/1.1" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser();
    parser.parse(in);
  }

  @Test(expected = ODataException.class)
  public void testBoundaryWithoutQuota() throws ODataException {
    String batch = "POST /service/$batch HTTP/1.1" + "\n"
        + "Content-Type:multipart/mixed; boundary=batch_1740-bb:84-2f7f" + "\n"
        + "\n"
        + "--batch_1740-bb:84-2f7f" + "\n"
        + "Content-Type: application/http" + "\n"
        + "Content-Transfer-Encoding: binary" + "\n"
        + "\n"
        + "GET Employees('1')/EmployeeName HTTP/1.1" + "\n"
        + "\n"
        + "--batch_1740-bb:84-2f7f--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser();
    parser.parse(in);
  }

  @Test(expected = ODataException.class)
  public void testWrongBoundaryString() throws ODataException {
    String batch = "POST /service/$batch HTTP/1.1" + "\n"
        + "Content-Type:multipart/mixed; boundary=batch_1740-bb84-2f7f" + "\n"
        + "\n"
        + "--batch_1740-bb:84-2f7f" + "\n"
        + "Content-Type: application/http" + "\n"
        + "Content-Transfer-Encoding: binary" + "\n"
        + "\n"
        + "GET Employees('1')/EmployeeName HTTP/1.1" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser();
    parser.parse(in);
  }

  // Nachfragen ob es ein Fehler ist
  @Test(expected = ODataException.class)
  public void testMimeHeaderContentType() throws ODataException {
    String batch = "POST /service/$batch HTTP/1.1" + "\n"
        + "Content-Type:multipart/mixed; boundary=batch_1740-bb84-2f7f" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f" + "\n"
        + "Content-Type: text/plain" + "\n"
        + "Content-Transfer-Encoding: binary" + "\n"
        + "\n"
        + "GET Employees('1')/EmployeeName HTTP/1.1" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser();
    parser.parse(in);
  }

  // Nachfragen ob es ein Fehler ist
  @Test(expected = ODataException.class)
  public void testMimeHeaderEncoding() throws ODataException {
    String batch = "POST /service/$batch HTTP/1.1" + "\n"
        + "Content-Type:multipart/mixed; boundary=batch_1740-bb84-2f7f" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f" + "\n"
        + "Content-Type: application/http" + "\n"
        + "Content-Transfer-Encoding: 8bit" + "\n"
        + "\n"
        + "GET Employees('1')/EmployeeName HTTP/1.1" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser();
    parser.parse(in);
  }

  @Test(expected = ODataException.class)
  public void testInvalidMethodForBatch() throws ODataException {
    String batch = "POST /service/$batch HTTP/1.1" + "\n"
        + "Content-Type:multipart/mixed; boundary=batch_1740-bb84-2f7f" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f" + "\n"
        + "Content-Type: application/http" + "\n"
        + "Content-Transfer-Encoding: binary" + "\n"
        + "\n"
        + "POST Employees('1')/EmployeeName HTTP/1.1" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser();
    parser.parse(in);
  }

  @Test(expected = ODataException.class)
  public void testNoMethod() throws ODataException {
    String batch = "POST /service/$batch HTTP/1.1" + "\n"
        + "Content-Type:multipart/mixed; boundary=batch_1740-bb84-2f7f" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f" + "\n"
        + "Content-Type: application/http" + "\n"
        + "Content-Transfer-Encoding: binary" + "\n"
        + "\n"
        + "Employees('1')/EmployeeName HTTP/1.1" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser();
    parser.parse(in);
  }

  @Test(expected = ODataException.class)
  public void testNoBoundaryString() throws ODataException {
    String batch = "POST /service/$batch HTTP/1.1" + "\n"
        + "Content-Type:multipart/mixed; boundary=batch_1740-bb84-2f7f" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f" + "\n"
        + "Content-Type: application/http" + "\n"
        + "Content-Transfer-Encoding: binary" + "\n"
        + "\n"
        + "GET Employees('1')/EmployeeName HTTP/1.1" + "\n"
        + "\n"
        + "Content-Type: application/http" + "\n"
        + "Content-Transfer-Encoding: binary" + "\n"
        + "\n"
        + "GET Employees('1')/EmployeeName HTTP/1.1" + "\n";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser();
    parser.parse(in);
  }

  @Test(expected = ODataException.class)
  public void testInvalidMethodForChangeset() throws ODataException {
    String batch = "POST /service/$batch HTTP/1.1" + "\n"
        + "Content-Type:multipart/mixed; boundary=batch_8194-cf13-1f56" + "\n"
        + "\n"
        + "--batch_8194-cf13-1f56" + "\n"
        + "Content-Type: multipart/mixed; boundary=changeset_f980-1cb6-94dd" + "\n"
        + "\n"
        + "--changeset_f980-1cb6-94dd" + "\n"
        + "Content-Type: application/http" + "\n"
        + "Content-Transfer-Encoding: binary" + "\n"
        + "\n"
        + "GET Employees('2')/EmployeeName HTTP/1.1" + "\n"
        + "Content-Type: application/json;odata=verbose" + "\n"
        + "MaxDataServiceVersion: 2.0" + "\n"
        + "\n"
        + "--batch_8194-cf13-1f56--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser();
    parser.parse(in);
  }

  @Test(expected = ODataException.class)
  public void testInvalidChangeSetBoundary() throws ODataException {
    String batch = "POST /service/$batch HTTP/1.1" + "\n"
        + "Content-Type:multipart/mixed; boundary=batch_8194-cf13-1f56" + "\n"
        + "\n"
        + "--batch_8194-cf13-1f56" + "\n"
        + "Content-Type: multipart/mixed;boundary=changeset_f980-1cb6-94dd" + "\n"
        + "\n"
        + "--changeset_f980-1cb6-94d" + "\n"
        + "Content-Type: application/http" + "\n"
        + "Content-Transfer-Encoding: binary" + "\n"
        + "\n"
        + "POST Employees('2') HTTP/1.1" + "\n"
        + "Content-Type: application/json;odata=verbose" + "\n"
        + "MaxDataServiceVersion: 2.0" + "\n"
        + "\n"
        + "--batch_8194-cf13-1f56--";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser();
    parser.parse(in);
  }

  @Test(expected = ODataException.class)
  public void testNoCloseDelimiter() throws ODataException {
    String batch = "POST /service/$batch HTTP/1.1" + "\n"
        + "Content-Type:multipart/mixed; boundary=batch_1740-bb84-2f7f" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f" + "\n"
        + "Content-Type: application/http" + "\n"
        + "Content-Transfer-Encoding: binary" + "\n"
        + "\n"
        + "GET Employees('1')/EmployeeName HTTP/1.1" + "\n"
        + "\n";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser();
    parser.parse(in);
  }

  @Test(expected = ODataException.class)
  public void testNoCloseDelimiter2() throws ODataException {
    String batch = "POST /service/$batch HTTP/1.1" + "\n"
        + "Content-Type:multipart/mixed; boundary=batch_1740-bb84-2f7f" + "\n"
        + "\n"
        + "--batch_1740-bb84-2f7f" + "\n"
        + "Content-Type: application/http" + "\n"
        + "Content-Transfer-Encoding: binary" + "\n"
        + "\n"
        + "GET Employees('1')/EmployeeName HTTP/1.1" + "\n";
    InputStream in = new ByteArrayInputStream(batch.getBytes());
    BatchRequestParser parser = new BatchRequestParser();
    parser.parse(in);
  }

  String request = "POST /service/$batch HTTP/1.1" + "\n"
      + "Content-Type:multipart/mixed; boundary=batch_8194-cf13-1f56" + "\n"
      + "\n"
      + "--batch_8194-cf13-1f56" + "\n"
      + "Content-Type: multipart/mixed;boundary=changeset_f980-1cb6-94dd" + "\n"
      + "\n"
      + "--changeset_f980-1cb6-94d" + "\n"
      + "Content-Type: application/http" + "\n"
      + "Content-Transfer-Encoding: binary" + "\n"
      + "\n"
      + "GET Employees('2')/EmployeeName HTTP/1.1" + "\n"
      + "Content-Length: 100000"
      + "Accept: application/atomsvc+xml;q=0.8, application/json;odata=verbose;q=0.5, */*;q=0.1" + "\n"
      + "DataServiceVersion: 1.0" + "\n"
      + "Content-Type: application/json;odata=verbose" + "\n"
      + "MaxDataServiceVersion: 2.0" + "\n"
      + "\n"
      + "{\"EmployeeName\":\"Frederic Fall MODIFIED\"}" + "\n"
      + "\n"
      + "--batch_8194-cf13-1f56--";
}
