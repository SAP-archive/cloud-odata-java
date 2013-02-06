package com.sap.core.odata.fit.basic;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * @author SAP AG
 */
public class ExceptionsTest extends AbstractBasicTest {
  @Override
  protected ODataSingleProcessor createProcessor() throws ODataException {
    return mock(ODataSingleProcessor.class);
  }

  @Test
  public void exceptionBasicTest() throws Exception {
    final HttpResponse response = executeGetRequest("NoContainer.NoEntitySet()");
    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());

    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    final Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", Edm.NAMESPACE_M_2007_08);
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));

    assertXpathExists("a:error", payload);
    assertXpathExists("/a:error/a:code", payload);
    assertXpathExists("/a:error/a:message[@xml:lang=\"en\"]", payload);
  }
}
