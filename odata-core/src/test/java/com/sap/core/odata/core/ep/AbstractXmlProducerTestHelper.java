package com.sap.core.odata.core.ep;

import java.util.Arrays;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.sap.core.odata.testutil.fit.BaseTest;

@RunWith(Parameterized.class)
public abstract class AbstractXmlProducerTestHelper extends BaseTest {

  public enum StreamWriterImplType {
    WOODSTOCKIMPL, SUNINTERNALIMPL;
  }

  public AbstractXmlProducerTestHelper(StreamWriterImplType type) {
    switch (type) {
    case WOODSTOCKIMPL:
      System.setProperty("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
      break;
    case SUNINTERNALIMPL:
      System.setProperty("javax.xml.stream.XMLOutputFactory", "com.sun.xml.internal.stream.XMLOutputFactoryImpl");
      break;
    default:
      System.setProperty("javax.xml.stream.XMLOutputFactory", "com.sun.xml.internal.stream.XMLOutputFactoryImpl");
      break;
    }
  }

  @Parameterized.Parameters
  public static List<Object[]> data() {
    // If desired this can be made dependent on runtime variables
    Object[][] a;
    a = new Object[2][1];
    a[0][0] = StreamWriterImplType.WOODSTOCKIMPL;
    a[1][0] = StreamWriterImplType.SUNINTERNALIMPL;

    return Arrays.asList(a);
  }

}
