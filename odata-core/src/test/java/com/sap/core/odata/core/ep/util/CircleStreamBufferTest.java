/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.core.ep.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class CircleStreamBufferTest extends BaseTest {

  private static final boolean LOG_ON = false;

  public CircleStreamBufferTest() {}

  @Before
  public void setUp() {}

  @Test
  public void testSimpleWriteReadSignBySign() throws Exception {
    CircleStreamBuffer csb = new CircleStreamBuffer();

    OutputStream write = csb.getOutputStream();
    byte[] writeData = "Test".getBytes("UTF-8");
    for (byte element : writeData) {
      write.write(element);
    }

    InputStream inStream = csb.getInputStream();
    byte[] buffer = new byte[4];
    for (int i = 0; i < buffer.length; i++) {
      buffer[i] = (byte) inStream.read();
    }

    String result = new String(buffer);
    log("Test result = [" + result + "]");

    assertEquals("Test", result);
  }

  @Test
  public void testSimpleWriteReadSignBySignMoreThenBufferSize() throws Exception {
    CircleStreamBuffer csb = new CircleStreamBuffer(128);

    OutputStream write = csb.getOutputStream();
    int signs = 1024;
    String testData = createTestString(signs);
    byte[] writeData = testData.getBytes("UTF-8");
    for (byte element : writeData) {
      write.write(element);
    }

    InputStream inStream = csb.getInputStream();
    byte[] buffer = new byte[signs];
    for (int i = 0; i < buffer.length; i++) {
      buffer[i] = (byte) inStream.read();
    }

    String result = new String(buffer);
    log("Test result = [" + result + "]");

    assertEquals(testData, result);
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
    log("Test result = [" + result + "]");

    assertEquals(4, count);
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

    log("Test result = [" + firstResult + "]");
    assertEquals("Test_1", firstResult);

    // second writeInternal/read cyclus
    outStream.write("Test_2".getBytes());
    String secondResult = readFrom(inStream);

    log("Test result = [" + secondResult + "]");
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

    log("Test result = [" + result + "]");
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

    log("Test result = [" + result + "]");
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
    // log("Test result = [" + result + "]");

    assertEquals(signs, result.length());
    assertEquals(testData, result);
  }

  @Test(expected = IOException.class)
  public void testCloseInputStream() throws Exception {
    CircleStreamBuffer csb = new CircleStreamBuffer();

    OutputStream write = csb.getOutputStream();
    write.write("Test".getBytes(), 0, 4);

    InputStream inStream = csb.getInputStream();
    inStream.close();
    byte[] buffer = new byte[4];
    int count = inStream.read(buffer);
    assertEquals(4, count);
  }

  @Test(expected = IOException.class)
  public void testCloseOutputStream() throws Exception {
    CircleStreamBuffer csb = new CircleStreamBuffer();

    OutputStream write = csb.getOutputStream();
    write.close();
    write.write("Test".getBytes(), 0, 4);
  }

  // ###################################################
  // #
  // # Below here are test helper methods
  // #
  // ###################################################

  private String readFrom(final InputStream stream) throws IOException {
    return readFrom(stream, 128);
  }

  private String readFrom(final InputStream stream, final int bufferSize) throws IOException {
    StringBuilder b = new StringBuilder();
    int count;
    byte[] buffer = new byte[bufferSize];
    while ((count = stream.read(buffer)) >= 0) {
      b.append(new String(buffer, 0, count));
    }
    return b.toString();
  }

  private String createTestString(final int signs) {
    StringBuilder b = new StringBuilder();

    for (int i = 0; i < signs; i++) {
      int sign = (int) (65 + (Math.random() * 25));
      b.append((char) sign);
    }

    return b.toString();
  }

  private void log(final String toLog) {
    if (LOG_ON) {
      System.out.println(toLog);
    }
  }
}
