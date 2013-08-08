package com.sap.core.odata.core.batch;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.api.batch.BatchException;
import com.sap.core.odata.api.client.batch.BatchSingleResponse;
import com.sap.core.odata.api.commons.HttpHeaders;

public class BatchResponseParserTest {

  private static final String LF = "\r\n";

  @Test
  public void test() throws BatchException {
    String getResponse = "--batch_123" + LF
        + "Content-Type: application/http" + LF
        + "Content-Transfer-Encoding: binary" + LF
        + "Content-ID: 1" + LF
        + LF
        + "HTTP/1.1 200 OK" + LF
        + "DataServiceVersion: 2.0" + LF
        + "Content-Type: text/plain;charset=utf-8" + LF
        + "Content-length: 22" + LF
        + LF
        + "Frederic Fall MODIFIED" + LF
        + LF
        + "--batch_123--";

    InputStream in = new ByteArrayInputStream(getResponse.getBytes());
    BatchResponseParser parser = new BatchResponseParser("multipart/mixed;boundary=batch_123");
    List<BatchSingleResponse> responses = parser.parse(in);
    for (BatchSingleResponse response : responses) {
      assertEquals("200", response.getStatusCode());
      assertEquals("OK", response.getStatusInfo());
      assertEquals("text/plain;charset=utf-8", response.getHeaders().get(HttpHeaders.CONTENT_TYPE));
      assertEquals("22", response.getHeaders().get("Content-length"));
      assertEquals("1", response.getContentId());
    }
  }

  @Test
  public void test2() throws BatchException {
    String putResponse = "--batch_123" + LF
        + "Content-Type: " + BatchConstants.MULTIPART_MIXED + ";boundary=changeset_12ks93js84d" + LF
        + LF
        + "--changeset_12ks93js84d" + LF
        + "Content-Type: application/http" + LF
        + "Content-Transfer-Encoding: binary" + LF
        + "Content-ID: 1" + LF
        + LF
        + "HTTP/1.1 204 No Content" + LF
        + "DataServiceVersion: 2.0" + LF
        + LF
        + LF
        + "--changeset_12ks93js84d--" + LF
        + LF
        + "--batch_123--";

    InputStream in = new ByteArrayInputStream(putResponse.getBytes());
    BatchResponseParser parser = new BatchResponseParser("multipart/mixed;boundary=batch_123");
    List<BatchSingleResponse> responses = parser.parse(in);
    for (BatchSingleResponse response : responses) {
      assertEquals("204", response.getStatusCode());
      assertEquals("No Content", response.getStatusInfo());
      assertEquals("1", response.getContentId());
    }
  }

  @Test(expected = BatchException.class)
  public void testInvalidMimeHeader() throws BatchException {
    String putResponse = "--batch_123" + LF
        + "Content-Type: " + BatchConstants.MULTIPART_MIXED + ";boundary=changeset_12ks93js84d" + LF
        + LF
        + "--changeset_12ks93js84d" + LF
        + "Content-Type: application/http" + LF
        + "Content-Transfer-Encoding: 7bit" + LF // Content-Transfer-Encoding must be binary
        + LF
        + "HTTP/1.1 No Content" + LF
        + "DataServiceVersion: 2.0" + LF
        + LF
        + LF
        + "--changeset_12ks93js84d--" + LF
        + LF
        + "--batch_123--";

    parseInvalidBatchResponseBody(putResponse);
  }

  @Test(expected = BatchException.class)
  public void testMissingMimeHeader() throws BatchException {
    String putResponse = "--batch_123" + LF
        + "Content-Type: " + BatchConstants.MULTIPART_MIXED + ";boundary=changeset_12ks93js84d" + LF
        + LF
        + "--changeset_12ks93js84d" + LF
        + LF
        + "HTTP/1.1 No Content" + LF
        + "DataServiceVersion: 2.0" + LF
        + LF
        + LF
        + "--changeset_12ks93js84d--" + LF
        + LF
        + "--batch_123--";

    parseInvalidBatchResponseBody(putResponse);
  }

  @Test(expected = BatchException.class)
  public void testInvalidContentType() throws BatchException {
    String putResponse = "--batch_123" + LF
        + "Content-Type: " + BatchConstants.MULTIPART_MIXED + LF //Missing boundary parameter
        + LF
        + "--changeset_12ks93js84d" + LF
        + "Content-Type: application/http" + LF
        + "Content-Transfer-Encoding: binary" + LF
        + LF
        + "HTTP/1.1 No Content" + LF
        + "DataServiceVersion: 2.0" + LF
        + LF
        + LF
        + "--changeset_12ks93js84d--" + LF
        + LF
        + "--batch_123--";

    parseInvalidBatchResponseBody(putResponse);
  }

  @Test(expected = BatchException.class)
  public void testInvalidStatusLine() throws BatchException {
    String putResponse = "--batch_123" + LF
        + "Content-Type: " + BatchConstants.MULTIPART_MIXED + ";boundary=changeset_12ks93js84d" + LF
        + LF
        + "--changeset_12ks93js84d" + LF
        + "Content-Type: application/http" + LF
        + "Content-Transfer-Encoding: binary" + LF
        + LF
        + "HTTP/1.1 No Content" + LF
        + "DataServiceVersion: 2.0" + LF
        + LF
        + LF
        + "--changeset_12ks93js84d--" + LF
        + LF
        + "--batch_123--";

    parseInvalidBatchResponseBody(putResponse);

  }

  @Test(expected = BatchException.class)
  public void testMissingCloseDelimiter() throws BatchException {
    String putResponse = "--batch_123" + LF
        + "Content-Type: " + BatchConstants.MULTIPART_MIXED + ";boundary=changeset_12ks93js84d" + LF
        + LF
        + "--changeset_12ks93js84d" + LF
        + "Content-Type: application/http" + LF
        + "Content-Transfer-Encoding: binary" + LF
        + LF
        + "HTTP/1.1 204 No Content" + LF
        + "DataServiceVersion: 2.0" + LF
        + LF
        + LF
        + "--changeset_12ks93js84d--" + LF
        + LF;

    parseInvalidBatchResponseBody(putResponse);

  }

  @Test(expected = BatchException.class)
  public void testInvalidEnteredContentLength() throws BatchException {
    String getResponse = "--batch_123" + LF
        + "Content-Type: application/http" + LF
        + "Content-Transfer-Encoding: binary" + LF
        + "Content-ID: 1" + LF
        + LF
        + "HTTP/1.1 200 OK" + LF
        + "DataServiceVersion: 2.0" + LF
        + "Content-Type: text/plain;charset=utf-8" + LF
        + "Content-length: 100" + LF
        + LF
        + "Frederic Fall" + LF
        + LF
        + "--batch_123--";

    parseInvalidBatchResponseBody(getResponse);
  }

  private void parseInvalidBatchResponseBody(final String putResponse) throws BatchException {
    InputStream in = new ByteArrayInputStream(putResponse.getBytes());
    BatchResponseParser parser = new BatchResponseParser("multipart/mixed;boundary=batch_123");
    parser.parse(in);
  }
}
