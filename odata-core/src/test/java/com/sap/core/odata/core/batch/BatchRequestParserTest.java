package com.sap.core.odata.core.batch;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class BatchRequestParserTest {

  @Test
    public void test() throws IOException {
      String fileName = "/batchRequest.txt";
      InputStream in = ClassLoader.class.getResourceAsStream(fileName);
      if (in == null) {
        throw new IOException("Requested file '" + fileName + "' was not found.");
      }
      System.out.println(in);
      BatchRequestParser parser = new BatchRequestParser();
      parser.parseBatchRequest(in);
  }
  
  

}
