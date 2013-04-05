package com.sap.core.odata.testutil.fit;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.Test;

public class Log4JConfigurationTest {

  @Test
  public void testConfiguration() {
    final URL url = Log4JConfigurationTest.class.getResource("/log4j.xml");
    assertNotNull(url);
  }

}
