/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Before;
import org.xml.sax.SAXException;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.Edm;

/**
 * Abstract base class for tests employing the reference scenario reading or writing XML.
 * @author SAP AG
 */
public class AbstractRefXmlTest extends AbstractRefTest {
  @Before
  public void setXmlNamespacePrefixes() {
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put(Edm.PREFIX_ATOM, Edm.NAMESPACE_ATOM_2005);
    prefixMap.put(Edm.PREFIX_APP, Edm.NAMESPACE_APP_2007);
    prefixMap.put(Edm.PREFIX_D, Edm.NAMESPACE_D_2007_08);
    prefixMap.put(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08);
    prefixMap.put(Edm.PREFIX_EDM, Edm.NAMESPACE_EDM_2008_09);
    prefixMap.put(Edm.PREFIX_EDMX, Edm.NAMESPACE_EDMX_2007_06);
    prefixMap.put(Edm.PREFIX_XML, Edm.NAMESPACE_XML_1998);
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));
  }

  @Override
  protected void badRequest(final String uri) throws Exception {
    final HttpResponse response = callUri(uri, HttpStatusCodes.BAD_REQUEST);
    validateXmlError(getBody(response));
  }

  @Override
  protected void notFound(final String uri) throws Exception {
    final HttpResponse response = callUri(uri, HttpStatusCodes.NOT_FOUND);
    validateXmlError(getBody(response));
  }

  protected void validateXmlError(final String xml) throws XpathException, IOException, SAXException {
    assertXpathExists("/m:error", xml);
    assertXpathExists("/m:error/m:code", xml);
    assertXpathExists("/m:error/m:message[@xml:lang=\"en\"]", xml);
  }

  protected String readFile(final String filename) throws IOException {
    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
    if (in == null) {
      throw new IOException("Requested file '" + filename + "' was not found.");
    }

    byte[] tmp = new byte[8192];
    int count = in.read(tmp);
    StringBuffer b = new StringBuffer();
    while (count >= 0) {
      b.append(new String(tmp, 0, count));
      count = in.read(tmp);
    }

    return b.toString();
  }
}
