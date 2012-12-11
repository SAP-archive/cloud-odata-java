package com.sap.core.odata.core.ep;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.testutils.mocks.MockFacade;

public class PerformanceTest extends AbstractProviderTest {

  private static final long TIMES = 500L;

  private AtomEntryEntityProvider provider;
  private EdmEntitySet edmEntitySet;
  private ByteArrayOutputStream outStream = null;
  private XMLStreamWriter writer;

  @Before
  public void before() throws Exception {
    super.before();

    provider = new AtomEntryEntityProvider(createContextMock());
    edmEntitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");

    outStream = new ByteArrayOutputStream();
    writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, "utf-8");

    writer.writeStartElement("junit");
    writer.writeDefaultNamespace(Edm.NAMESPACE_ATOM_2005);
    writer.writeNamespace(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08);
    writer.writeNamespace(Edm.PREFIX_D, Edm.NAMESPACE_D_2007_08);
    writer.writeAttribute(Edm.PREFIX_XML, Edm.NAMESPACE_XML_1998, "base", "xxx");
  }

  @After
  public void after() throws Exception {
    writer.writeEndElement();
    writer.flush();
    outStream.flush();

    assertNotNull(outStream.toByteArray());

    try {

    } finally {
      outStream.close();
    }
  }

  @Test
  public void readAtomEntry() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    long t = startTimer();

    for (int i = 0; i < TIMES; i++) {
      EntityInfoAggregator eia = EntityInfoAggregator.create(edmEntitySet);
      provider.append(writer, eia, roomData, false, null);
    }
    stopTimer(t, "readAtomEntry");
  }

  @Test
  public void readAtomEntryOptimized() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    long t = startTimer();

    EntityInfoAggregator eia = EntityInfoAggregator.create(edmEntitySet);
    for (int i = 0; i < TIMES; i++) {
      provider.append(writer, eia, roomData, false, null);
    }
    stopTimer(t, "readAtomEntryOptimized");
  }

  private void stopTimer(long t, String msg) {
    t = (System.nanoTime() - t) / TIMES;

    long millis = t / (1000L * 1000L);
    long micros = t % (1000L * 1000L);

    long sum = (t * TIMES) / (1000L * 1000L);

    log.debug(msg + ": " + millis + "." + micros / 1000L + "[ms] (" + TIMES + " in " + sum + " [ms])");
  }

  private long startTimer() {
    long t = System.nanoTime();
    return t;
  }

}
