package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;

public class ExceptionsTest extends AbstractRefTest {
  @Test
  public void exceptionBasicTest() throws Exception {

    final HttpGet request = new HttpGet(this.getEndpoint() + "Employe");
    final HttpResponse response = this.getHttpClient().execute(request);
    final String payload = getBody(response);
    System.out.println(payload);

    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata");
    NamespaceContext ctx = new SimpleNamespaceContext(prefixMap);
    XMLUnit.setXpathNamespaceContext(ctx);

    assertXpathExists("a:error", payload);
    assertXpathExists("/a:error/a:code", payload);
    assertXpathExists("/a:error/a:message[@xml:lang=\"en\"]", payload);
  }
}
