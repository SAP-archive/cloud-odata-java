/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sap.core.odata.core.ep.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author d046871
 */
public class CircleStreamBufferTest {

  public CircleStreamBufferTest() {
  }

  @Before
  public void setUp() {
  }

  @Test
  public void testSimpleWriteReadOnce() throws Exception {
    CircleStreamBuffer csb = new CircleStreamBuffer();

    OutputStream write = csb.getOutputStream();
    write.write("Test".getBytes(), 0, 4);

    InputStream inStream = csb.getInputStream();
    byte[] buffer = new byte[4];
    int count = inStream.read(buffer);

    String result = new String(buffer);
    System.out.println("Test result = [" + result + "]");

    assertEquals("Test", result);
  }

  @Test
  public void testSimpleWriteReadTwice() throws Exception {
    CircleStreamBuffer csb = new CircleStreamBuffer();

    OutputStream outStream = csb.getOutputStream();
    InputStream inStream = csb.getInputStream();

    // first writeInternal/read cyclus
    outStream.write("Test_1".getBytes());
    String firstResult = readFrom(inStream);

    System.out.println("Test result = [" + firstResult + "]");
    assertEquals("Test_1", firstResult);

    // second writeInternal/read cyclus
    outStream.write("Test_2".getBytes());
    String secondResult = readFrom(inStream);

    System.out.println("Test result = [" + secondResult + "]");
    assertEquals("Test_2", secondResult);
  }

  @Test
  public void testSimpleWriteReadOnce8k() throws Exception {
    CircleStreamBuffer csb = new CircleStreamBuffer();

    OutputStream outStream = csb.getOutputStream();
    InputStream inStream = csb.getInputStream();
    final int signs = 8192;

    String testData = createTestString(signs);
    outStream.write(testData.getBytes());
    String result = readFrom(inStream);

    System.out.println("Test result = [" + result + "]");
    assertEquals(signs, result.length());
    assertEquals(testData, result);
  }

  @Test
  public void testSimpleWriteExactOnceMoreThenBufferSize() throws Exception {
    int bufferSize = 4096;
    CircleStreamBuffer csb = new CircleStreamBuffer(bufferSize);

    OutputStream outStream = csb.getOutputStream();
    InputStream inStream = csb.getInputStream();
    final int signs = bufferSize + 1;

    String testData = createTestString(signs);
    outStream.write(testData.getBytes());
    String result = readFrom(inStream, bufferSize * 2);

    System.out.println("Test result = [" + result + "]");
    assertEquals(signs, result.length());
    assertEquals(testData, result);
  }

  @Test
  public void testSimpleWriteReadOnceMoreThenBufferSize() throws Exception {
    int bufferSize = 4096;
    CircleStreamBuffer csb = new CircleStreamBuffer(bufferSize);

    OutputStream outStream = csb.getOutputStream();
    InputStream inStream = csb.getInputStream();

    int signs = (1 + bufferSize) * 3;
    String testData = createTestString(signs);
    outStream.write(testData.getBytes());
    String result = readFrom(inStream);
    // System.out.println("Test result = [" + result + "]");

    assertEquals(signs, result.length());
    assertEquals(testData, result);
  }

  // ###################################################
  // #
  // # Below here are test helper methods
  // #
  // ###################################################

  private String readFrom(InputStream stream) throws IOException {
    return readFrom(stream, 128);
  }

  private String readFrom(InputStream stream, int bufferSize) throws IOException {
    StringBuilder b = new StringBuilder();
    int count;
    byte[] buffer = new byte[bufferSize];
    while ((count = stream.read(buffer)) >= 0) {
      b.append(new String(buffer, 0, count));
    }
    return b.toString();
  }

  private String createTestString(int signs) {
    StringBuilder b = new StringBuilder();

    for (int i = 0; i < signs; i++) {
      int sign = (int) (65 + (Math.random() * 25));
      b.append((char) sign);
    }

    return b.toString();
  }
}
