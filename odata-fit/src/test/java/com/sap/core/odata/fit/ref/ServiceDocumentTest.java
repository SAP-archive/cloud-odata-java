package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;

/**
 * Tests employing the reference scenario reading the service document in XML format
 * @author SAP AG
 */
public class ServiceDocumentTest extends AbstractRefTest {

  @Test
  public void serviceDocument() throws Exception {
    HttpResponse response = callUri("");
    final String payload = getBody(response);

    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("atom", "http://www.w3.org/2005/Atom");
    prefixMap.put("a", "http://www.w3.org/2007/app");
    NamespaceContext ctx = new SimpleNamespaceContext(prefixMap);
    XMLUnit.setXpathNamespaceContext(ctx);

    assertXpathExists("/a:service", payload);
    assertXpathExists("/a:service/a:workspace", payload);
    assertXpathExists("/a:service/a:workspace/atom:title", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Employees\"]", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Employees\"]/atom:title", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Teams\"]", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Teams\"]/atom:title", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Rooms\"]", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Rooms\"]/atom:title", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Managers\"]", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Managers\"]/atom:title", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Buildings\"]", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Buildings\"]/atom:title", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Container2.Photos\"]", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Container2.Photos\"]/atom:title", payload);
  }

}
