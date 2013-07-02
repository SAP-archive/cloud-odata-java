package com.sap.core.odata.core.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

import com.sap.core.odata.api.ep.EntityProviderException;

public class AcceptParserTest {
  private static final String TAB = "\t";

  @Test
  public void testAcceptHeader() throws EntityProviderException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    assertNotNull(acceptHeaders);
    assertEquals(4, acceptHeaders.size());
    assertEquals("text/html", acceptHeaders.get(0));
    assertEquals("application/xhtml+xml", acceptHeaders.get(1));
    assertEquals("application/xml", acceptHeaders.get(2));
    assertEquals("*/*", acceptHeaders.get(3));
  }

  @Test
  public void testAcceptHeaderWithParameter() throws EntityProviderException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("application/json;odata=verbose;q=1.0, */*;q=0.1");
    assertNotNull(acceptHeaders);
    assertEquals(2, acceptHeaders.size());
    assertEquals("application/json;odata=verbose", acceptHeaders.get(0));
    ;
    assertEquals("*/*", acceptHeaders.get(1));
  }

  @Test
  public void testAcceptHeaderWithParameterAndLws() throws EntityProviderException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("application/json;  odata=verbose;q=1.0, */*;q=0.1");
    assertNotNull(acceptHeaders);
    assertEquals(2, acceptHeaders.size());
    assertEquals("application/json;  odata=verbose", acceptHeaders.get(0));
    ;
    assertEquals("*/*", acceptHeaders.get(1));
  }

  @Test
  public void testAcceptHeaderWithTabulator() throws EntityProviderException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("application/json;\todata=verbose;q=1.0, */*;q=0.1");
    assertNotNull(acceptHeaders);
    assertEquals(2, acceptHeaders.size());
    assertEquals("application/json;" + TAB + "odata=verbose", acceptHeaders.get(0));
    ;
    assertEquals("*/*", acceptHeaders.get(1));
  }

  @Test
  public void testAcceptHeaderWithTwoParameters() throws EntityProviderException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("application/xml;another=test ; param=alskdf, */*;q=0.1");
    assertNotNull(acceptHeaders);
    assertEquals(2, acceptHeaders.size());
    assertEquals("application/xml;another=test ; param=alskdf", acceptHeaders.get(0));
    ;
    assertEquals("*/*", acceptHeaders.get(1));
  }

  @Test
  public void testAcceptHeader2() throws EntityProviderException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("text/html;level=1, application/*, */*;q=0.1");
    assertNotNull(acceptHeaders);
    assertEquals(3, acceptHeaders.size());
    assertEquals("text/html;level=1", acceptHeaders.get(0));
    assertEquals("application/*", acceptHeaders.get(1));
    assertEquals("*/*", acceptHeaders.get(2));
  }

  @Test
  public void testMoreSpecificMediaType() throws EntityProviderException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("application/*, application/xml");
    assertNotNull(acceptHeaders);
    assertEquals(2, acceptHeaders.size());
    assertEquals("application/xml", acceptHeaders.get(0));
    assertEquals("application/*", acceptHeaders.get(1));
  }

  @Test
  public void testQualityParameter() throws EntityProviderException {
    List<String> acceptHeaders = AcceptParser.parseAcceptHeaders("application/*, */*; q=0.012");
    assertNotNull(acceptHeaders);
  }

  @Test(expected = EntityProviderException.class)
  public void testInvalidAcceptHeader() throws EntityProviderException {
    AcceptParser.parseAcceptHeaders("appi cation/*, */*;q=0.1");
  }

  @Test(expected = EntityProviderException.class)
  public void testInvalidQualityParameter() throws EntityProviderException {
    AcceptParser.parseAcceptHeaders("appication/*, */*;q=0,9");
  }

  @Test(expected = EntityProviderException.class)
  public void testInvalidQualityParameter2() throws EntityProviderException {
    AcceptParser.parseAcceptHeaders("appication/*, */*;q=1.0001");
  }

  @Test
  public void testAcceptLanguages() throws EntityProviderException {
    List<Locale> acceptLanguageHeaders = AcceptParser.parseAcceptableLanguages("en-US,en;q=0.7,en-UK;q=0.9");
    assertNotNull(acceptLanguageHeaders);
    assertEquals(3, acceptLanguageHeaders.size());
    assertEquals(new Locale("en", "US"), acceptLanguageHeaders.get(0));
    assertEquals(new Locale("en", "UK"), acceptLanguageHeaders.get(1));
    assertEquals(new Locale("en"), acceptLanguageHeaders.get(2));
  }

  @Test
  public void testAllAcceptLanguages() throws EntityProviderException {
    List<Locale> acceptLanguageHeaders = AcceptParser.parseAcceptableLanguages("*");
    assertNotNull(acceptLanguageHeaders);
    assertEquals(1, acceptLanguageHeaders.size());
  }

  @Test
  public void testLongAcceptLanguageValue() throws EntityProviderException {
    List<Locale> acceptLanguageHeaders = AcceptParser.parseAcceptableLanguages("english");
    assertNotNull(acceptLanguageHeaders);
    assertEquals(new Locale("english"), acceptLanguageHeaders.get(0));
  }

  @Test(expected = EntityProviderException.class)
  public void testInvalidAcceptLanguageValue() throws EntityProviderException {
    AcceptParser.parseAcceptableLanguages("en_US");
  }
}
