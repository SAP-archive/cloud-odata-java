package com.sap.core.odata.testutils.helper;

import java.util.Map;

import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;

public class XMLUnitHelper {

  /**
   * initilaze XML namespaces of XPathEngine
   * @param namespaces prefix - namespace mapping 
   */
  public static void registerXmlNs(Map<String, String> namespaces) {
    NamespaceContext ctx = new SimpleNamespaceContext(namespaces);
    XMLUnit.setXpathNamespaceContext(ctx);
    XpathEngine engine = XMLUnit.newXpathEngine();
    engine.setNamespaceContext(ctx);
  }

}
