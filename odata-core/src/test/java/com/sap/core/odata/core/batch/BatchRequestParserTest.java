package com.sap.core.odata.core.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
            assertEquals("*/*;q=0.1", request.getAcceptHeaders().get(2));
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
}
