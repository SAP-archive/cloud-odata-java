package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.Edm;

/**
 * @author SAP AG
 */
public class ExceptionsTest extends AbstractRefTest {
  @Test
  public void exceptionBasicTest() throws Exception {
    final HttpResponse response = callUri("Employe", HttpStatusCodes.NOT_FOUND);
    final String payload = getBody(response);

    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", Edm.NAMESPACE_M_2007_08);
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));

    assertXpathExists("a:error", payload);
    assertXpathExists("/a:error/a:code", payload);
    assertXpathExists("/a:error/a:message[@xml:lang=\"en\"]", payload);
  }
}
