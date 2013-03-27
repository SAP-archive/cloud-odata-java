package com.sap.core.odata.core.ep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.stream.XMLOutputFactory;

import org.junit.Test;

/**
 * @author SAP AG
 */
public class LoadXMLFactoryTest {

  @Test
  public void loadWoodstockFactory() throws Exception {
    System.setProperty("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    assertNotNull(factory);
    assertEquals("com.ctc.wstx.stax.WstxOutputFactory", factory.getClass().getName());
  }

  @Test
  public void loadSunFactory() throws Exception {
    System.setProperty("javax.xml.stream.XMLOutputFactory", "com.sun.xml.internal.stream.XMLOutputFactoryImpl");
    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    assertNotNull(factory);
    assertEquals("com.sun.xml.internal.stream.XMLOutputFactoryImpl", factory.getClass().getName());
  }
}
