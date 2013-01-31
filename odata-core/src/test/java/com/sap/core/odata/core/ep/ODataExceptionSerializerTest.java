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

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * @author SAP AG
 *
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
    testSerializeXML("ErrorCode", "Message", null, ContentType.APPLICATION_XML, Locale.GERMAN);
    testSerializeXML("ErrorCode", "Message", null, ContentType.APPLICATION_XML, Locale.ENGLISH);
    testSerializeXML("ErrorCode", "Message", null, ContentType.APPLICATION_XML, Locale.CANADA);
    testSerializeXML("ErrorCode", "Message", null, ContentType.APPLICATION_XML, Locale.FRANCE);
    testSerializeXML("ErrorCode", "Message", null, ContentType.APPLICATION_XML, Locale.CHINA);
    
    testSerializeXML("ErrorCode", "Message", null, ContentType.APPLICATION_ATOM_XML, Locale.GERMAN);
    testSerializeXML("ErrorCode", "Message", null, ContentType.APPLICATION_ATOM_XML, Locale.ENGLISH);
    testSerializeXML("ErrorCode", "Message", null, ContentType.APPLICATION_ATOM_XML, Locale.CANADA);
    testSerializeXML("ErrorCode", "Message", null, ContentType.APPLICATION_ATOM_XML, Locale.FRANCE);
    testSerializeXML("ErrorCode", "Message", null, ContentType.APPLICATION_ATOM_XML, Locale.CHINA);
  }
  
//  @Test
//  public void testXMLSerializationWithoutLocale() throws Exception {
//    testSerializeXML("ErrorCode", "Message", null, ContentType.APPLICATION_XML, null);
//  }
  
  @Test
  public void testXMLSerializationWithoutMessage() throws Exception {
    testSerializeXML("ErrorCode", null, null, ContentType.APPLICATION_XML, Locale.GERMAN);
  }
  
  @Test
  public void testXMLSerializationWithoutAll() throws Exception {
    testSerializeXML(null, null, null, ContentType.APPLICATION_XML, Locale.GERMAN);
  }
  
  @Test
  public void testXMLSerializationWithInnerError() throws Exception {
    testSerializeXML("ErrorCode", "Message", "InnerError", ContentType.APPLICATION_XML, Locale.GERMAN);
    testSerializeXML("ErrorCode", "Message", "InnerError", ContentType.APPLICATION_XML, Locale.ENGLISH);
    testSerializeXML("ErrorCode", "Message", "InnerError", ContentType.APPLICATION_XML, Locale.CANADA);
    testSerializeXML("ErrorCode", "Message", "InnerError", ContentType.APPLICATION_XML, Locale.FRANCE);
    testSerializeXML("ErrorCode", "Message", "InnerError", ContentType.APPLICATION_XML, Locale.CHINA);
    
    testSerializeXML("ErrorCode", "Message", "InnerError", ContentType.APPLICATION_ATOM_XML, Locale.GERMAN);
    testSerializeXML("ErrorCode", "Message", "InnerError", ContentType.APPLICATION_ATOM_XML, Locale.ENGLISH);
    testSerializeXML("ErrorCode", "Message", "InnerError", ContentType.APPLICATION_ATOM_XML, Locale.CANADA);
    testSerializeXML("ErrorCode", "Message", "InnerError", ContentType.APPLICATION_ATOM_XML, Locale.FRANCE);
    testSerializeXML("ErrorCode", "Message", "InnerError", ContentType.APPLICATION_ATOM_XML, Locale.CHINA);
  }
  
  @Test
  public void testJsonSerializationWithoutInnerError() throws Exception {
    testSerializeJSON("ErrorCode", "Message", null, ContentType.APPLICATION_JSON, Locale.GERMAN);
  }
  
  @Test
  public void testJsonSerializationWithInnerError() throws Exception {
    testSerializeJSON("ErrorCode", "Message", null, ContentType.APPLICATION_JSON, Locale.GERMAN);
  }
  
  //HelperMethod
  private void testSerializeJSON(String errorCode, String message, String innerError, ContentType contentType, Locale locale) throws Exception {
    InputStream inputStream = ODataExceptionSerializer.serialize(errorCode, message, innerError, contentType, locale);
    String jsonErrorMessage = StringHelper.inputStreamToString(inputStream);
    assertEquals( "not supported error format JSON; " + errorCode + ", " + message, jsonErrorMessage);
  }

  //HelperMethod
  private void testSerializeXML(String errorCode, String message, String innerError, ContentType contentType, Locale locale) throws Exception {
    InputStream inputStream = ODataExceptionSerializer.serialize(errorCode, message, innerError, contentType, locale);
    String xmlErrorMessage = StringHelper.inputStreamToString(inputStream);
    if(errorCode != null){
      assertXpathEvaluatesTo(errorCode, "/a:error/a:code", xmlErrorMessage);
    }else {
      assertXpathExists("/a:error/a:code", xmlErrorMessage);
    }    
    if(message != null){
      assertXpathEvaluatesTo(message, "/a:error/a:message", xmlErrorMessage);
      assertXpathExists("/a:error/a:message[@xml:lang=\"" + getLang(locale) + "\"]", xmlErrorMessage);
    }else{
      assertXpathExists("/a:error/a:message", xmlErrorMessage);
    } 
    if (innerError != null) {
      assertXpathEvaluatesTo(innerError, "/a:error/a:innererror", xmlErrorMessage);
    } else {
      assertXpathNotExists("/a:error/a:innererror", xmlErrorMessage);
    }

  }

  //HelperMethod
  private String getLang(Locale locale) {
    if(locale == null){
      return "";
    }
    if (locale.getCountry().isEmpty())
      return locale.getLanguage();
    else
      return locale.getLanguage() + "-" + locale.getCountry();
  }
}
