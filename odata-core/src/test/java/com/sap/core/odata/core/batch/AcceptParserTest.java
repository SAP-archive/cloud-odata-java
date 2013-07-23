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
package com.sap.core.odata.core.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.sap.core.odata.api.batch.BatchException;

public class AcceptParserTest {
  private static final String TAB = "\t";

  @Test
  public void testAcceptHeader() throws BatchException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    assertNotNull(acceptHeaders);
    assertEquals(4, acceptHeaders.size());
    assertEquals("text/html", acceptHeaders.get(0));
    assertEquals("application/xhtml+xml", acceptHeaders.get(1));
    assertEquals("application/xml", acceptHeaders.get(2));
    assertEquals("*/*", acceptHeaders.get(3));
  }

  @Test
  public void testAcceptHeaderWithParameter() throws BatchException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("application/json;odata=verbose;q=1.0, */*;q=0.1");
    assertNotNull(acceptHeaders);
    assertEquals(2, acceptHeaders.size());
    assertEquals("application/json;odata=verbose", acceptHeaders.get(0));
    ;
    assertEquals("*/*", acceptHeaders.get(1));
  }

  @Test
  public void testAcceptHeaderWithParameterAndLws() throws BatchException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("application/json;  odata=verbose;q=1.0, */*;q=0.1");
    assertNotNull(acceptHeaders);
    assertEquals(2, acceptHeaders.size());
    assertEquals("application/json;  odata=verbose", acceptHeaders.get(0));
    ;
    assertEquals("*/*", acceptHeaders.get(1));
  }

  @Test
  public void testAcceptHeaderWithTabulator() throws BatchException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("application/json;\todata=verbose;q=1.0, */*;q=0.1");
    assertNotNull(acceptHeaders);
    assertEquals(2, acceptHeaders.size());
    assertEquals("application/json;" + TAB + "odata=verbose", acceptHeaders.get(0));
    ;
    assertEquals("*/*", acceptHeaders.get(1));
  }

  @Test
  public void testAcceptHeaderWithTwoParameters() throws BatchException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("application/xml;another=test ; param=alskdf, */*;q=0.1");
    assertNotNull(acceptHeaders);
    assertEquals(2, acceptHeaders.size());
    assertEquals("application/xml;another=test ; param=alskdf", acceptHeaders.get(0));
    ;
    assertEquals("*/*", acceptHeaders.get(1));
  }

  @Test
  public void testAcceptHeader2() throws BatchException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("text/html;level=1, application/*, */*;q=0.1");
    assertNotNull(acceptHeaders);
    assertEquals(3, acceptHeaders.size());
    assertEquals("text/html;level=1", acceptHeaders.get(0));
    assertEquals("application/*", acceptHeaders.get(1));
    assertEquals("*/*", acceptHeaders.get(2));
  }

  @Test
  public void testMoreSpecificMediaType() throws BatchException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("application/*, application/xml");
    assertNotNull(acceptHeaders);
    assertEquals(2, acceptHeaders.size());
    assertEquals("application/xml", acceptHeaders.get(0));
    assertEquals("application/*", acceptHeaders.get(1));
  }

  @Test
  public void testQualityParameter() throws BatchException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("application/*, */*; q=0.012");
    assertNotNull(acceptHeaders);
  }

  @Test(expected = BatchException.class)
  public void testInvalidAcceptHeader() throws BatchException {
    AcceptParser.parseAcceptHeaders("appi cation/*, */*;q=0.1");
  }

  @Test(expected = BatchException.class)
  public void testInvalidQualityParameter() throws BatchException {
    AcceptParser.parseAcceptHeaders("appication/*, */*;q=0,9");
  }

  @Test(expected = BatchException.class)
  public void testInvalidQualityParameter2() throws BatchException {
    AcceptParser.parseAcceptHeaders("appication/*, */*;q=1.0001");
  }

  @Test
  public void testAcceptLanguages() throws BatchException {
    List<String> acceptLanguageHeaders = AcceptParser.parseAcceptableLanguages("en-US,en;q=0.7,en-UK;q=0.9");
    assertNotNull(acceptLanguageHeaders);
    assertEquals(3, acceptLanguageHeaders.size());
    assertEquals("en-US", acceptLanguageHeaders.get(0));
    assertEquals("en-UK", acceptLanguageHeaders.get(1));
    assertEquals("en", acceptLanguageHeaders.get(2));
  }

  @Test
  public void testAllAcceptLanguages() throws BatchException {
    List<String> acceptLanguageHeaders = AcceptParser.parseAcceptableLanguages("*");
    assertNotNull(acceptLanguageHeaders);
    assertEquals(1, acceptLanguageHeaders.size());
  }

  @Test
  public void testLongAcceptLanguageValue() throws BatchException {
    List<String> acceptLanguageHeaders = AcceptParser.parseAcceptableLanguages("english");
    assertNotNull(acceptLanguageHeaders);
    assertEquals("english", acceptLanguageHeaders.get(0));
  }

  @Test(expected = BatchException.class)
  public void testInvalidAcceptLanguageValue() throws BatchException {
    AcceptParser.parseAcceptableLanguages("en_US");
  }
}
