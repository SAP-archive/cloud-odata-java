/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.ep.producer;

import static junit.framework.Assert.assertEquals;

import java.io.InputStream;
import java.util.Locale;

import org.junit.Test;

import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.ep.ProviderFacadeImpl;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * @author SAP AG
 */
public class JsonErrorProducerTest {

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
}
