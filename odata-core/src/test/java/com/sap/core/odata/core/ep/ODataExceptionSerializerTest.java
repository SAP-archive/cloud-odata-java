package com.sap.core.odata.core.ep;

import static junit.framework.Assert.assertEquals;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * @author SAP AG
 */
public class ODataExceptionSerializerTest extends BaseTest {

  @BeforeClass
  public static void setup() throws Exception {
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", Edm.NAMESPACE_M_2007_08);
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));
  }

  @Test
  public void testXMLSerializationWithoutInnerError() throws Exception {
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_XML, Locale.GERMAN);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_XML, Locale.ENGLISH);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_XML, Locale.CANADA);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_XML, Locale.FRANCE);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_XML, Locale.CHINA);

    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_ATOM_XML, Locale.GERMAN);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_ATOM_XML, Locale.ENGLISH);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_ATOM_XML, Locale.CANADA);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_ATOM_XML, Locale.FRANCE);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_ATOM_XML, Locale.CHINA);
  }

  @Test
  public void testXMLSerializationWithoutLocale() throws Exception {
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_XML, null);
  }

  @Test
  public void testXMLSerializationWithoutMessage() throws Exception {
    testSerializeXML("ErrorCode", null, ContentType.APPLICATION_XML, Locale.GERMAN);
  }

  @Test
  public void testXMLSerializationWithoutAll() throws Exception {
    testSerializeXML(null, null, ContentType.APPLICATION_XML, Locale.GERMAN);
  }

  @Test
  public void testXMLSerializationWithInnerError() throws Exception {
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_XML, Locale.GERMAN);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_XML, Locale.ENGLISH);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_XML, Locale.CANADA);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_XML, Locale.FRANCE);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_XML, Locale.CHINA);

    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_ATOM_XML, Locale.GERMAN);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_ATOM_XML, Locale.ENGLISH);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_ATOM_XML, Locale.CANADA);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_ATOM_XML, Locale.FRANCE);
    testSerializeXML("ErrorCode", "Message", ContentType.APPLICATION_ATOM_XML, Locale.CHINA);
  }

  @Test
  public void jsonSerialization() throws Exception {
    testSerializeJSON("ErrorCode", "Message", Locale.GERMANY);
    testSerializeJSON("ErrorCode", "Message", Locale.GERMAN);
  }

  @Test
  public void jsonSerializationWithoutLocale() throws Exception {
    testSerializeJSON("ErrorCode", "Message", null);
  }

  @Test
  public void jsonSerializationEmpty() throws Exception {
    testSerializeJSON(null, null, null);
  }

  // helper method
  private void testSerializeJSON(final String errorCode, final String message, final Locale locale) throws Exception {
    ODataResponse response = new ProviderFacadeImpl().writeErrorDocument(HttpContentType.APPLICATION_JSON, HttpStatusCodes.INTERNAL_SERVER_ERROR, errorCode, message, locale, null);
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());
    assertEquals(ODataServiceVersion.V10, response.getHeader(ODataHttpHeaders.DATASERVICEVERSION));
    final String jsonErrorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertEquals("{\"error\":{\"code\":" + (errorCode == null ? "null" : "\"" + errorCode + "\"") + ","
        + "\"message\":{\"lang\":"
        + (locale == null ? "null" : ("\"" + locale.getLanguage() + (locale.getCountry().isEmpty() ? "" : ("-" + locale.getCountry())) + "\""))
        + ",\"value\":" + (message == null ? "null" : "\"" + message + "\"") + "}}}",
        jsonErrorMessage);
  }

  //HelperMethod
  private void testSerializeXML(final String errorCode, final String message, final ContentType contentType, final Locale locale) throws Exception {
    ODataResponse response = new ProviderFacadeImpl().writeErrorDocument(contentType.toContentTypeString(), HttpStatusCodes.INTERNAL_SERVER_ERROR, errorCode, message, locale, null);
    String xmlErrorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());

    assertXpathExists("/a:error/a:code", xmlErrorMessage);
    assertXpathEvaluatesTo(errorCode == null ? "" : errorCode, "/a:error/a:code", xmlErrorMessage);

    assertXpathExists("/a:error/a:message", xmlErrorMessage);
    assertXpathEvaluatesTo(message == null ? "" : message, "/a:error/a:message", xmlErrorMessage);
    assertXpathEvaluatesTo(getLang(locale), "/a:error/a:message/@xml:lang", xmlErrorMessage);

    assertXpathNotExists("/a:error/a:innererror", xmlErrorMessage);
  }

  //HelperMethod
  private String getLang(final Locale locale) {
    if (locale == null) {
      return "";
    }
    if (locale.getCountry().isEmpty()) {
      return locale.getLanguage();
    } else {
      return locale.getLanguage() + "-" + locale.getCountry();
    }
  }
}
