package org.odata4j.test.integration.format.xml;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.core4j.Func;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.ElementNameAndTextQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.odata4j.core.NamespacedAnnotation;
import org.odata4j.core.OProperty;
import org.odata4j.core.PrefixedNamespace;
import org.odata4j.edm.EdmAnnotation;
import org.odata4j.edm.EdmAnnotationAttribute;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmDecorator;
import org.odata4j.edm.EdmDocumentation;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmItem;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmStructuralType;
import org.odata4j.examples.producer.jpa.airline.Airport;
import org.odata4j.format.xml.EdmxFormatParser;
import org.odata4j.producer.PropertyPath;
import org.odata4j.producer.inmemory.InMemoryProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.producer.server.ODataServer;
import org.odata4j.stax2.util.StaxUtil;
import org.odata4j.test.integration.AbstractRuntimeTest;
import org.odata4j.test.integration.producer.jpa.northwind.NorthwindTestUtils;
import org.xml.sax.SAXException;

/**
 * a simple test for writing annotations and documentation in the edmx.
 *
 * This test also demonstrates the use of {@link EdmDecorator}.
 */
public class EdmxFormatWriterTest extends AbstractRuntimeTest implements EdmDecorator {

  public EdmxFormatWriterTest(RuntimeFacadeType type) {
    super(type);
  }

  private ODataServer server = null;
  @SuppressWarnings("unused")
  private EdmDataServices ds = null;

  @Before
  public void setUp() {
    ds = buildModel();
  }

  @After
  public void tearDown() {
    if (server != null) {
      server.stop();
    }
  }

  @Ignore("Re-enable when it passes")
  @Test
  public void testExtensions() throws InterruptedException, SAXException, IOException {
    // test to see that documentation and annotations get written correctly

    String metadata = this.rtFacade.getWebResource(endpointUri + "$metadata").getEntity();

    //System.out.println(metadata);

    String expected = NorthwindTestUtils.readFileToString("/META-INF/uri-conventions/xml/DocAnnotTest.xml");

    XMLUnit.setIgnoreWhitespace(true);
    Diff myDiff = new Diff(expected, metadata);
    myDiff.overrideElementQualifier(new ElementNameAndTextQualifier());

    assertXMLEqual("bad $metadata", myDiff, true);

    EdmDataServices pds = new EdmxFormatParser().parseMetadata(StaxUtil.newXMLEventReader(new StringReader(metadata)));
    Assert.assertTrue(pds != null); // we parsed it!

    // TODO: once EdmxFormatParser supports doc and annotations we can check pds
    // for the expected objects.

    // TODO: once we support doc and annots for all EdmItem types we should
    // include them.

    // also, this test doesn't ensure the placement of annotations and documenation in the edmx,
    // for example, Documentation is always the first sub-element.
  }

  public static final String endpointUri = "http://localhost:8887/flights.svc/";

  private EdmDataServices buildModel() {

    InMemoryProducer p = new InMemoryProducer("flights", null, 50, this, null);

    final List<Airport> airports = new ArrayList<Airport>();
    Airport denver = new Airport();
    denver.setCode("DIA");
    denver.setCountry("USA");
    denver.setName("Denver International Airport");
    airports.add(denver);

    Airport honolulu = new Airport();
    honolulu.setCode("HNL");
    honolulu.setCountry("USA");
    honolulu.setName("Honolulu International Airport");
    airports.add(honolulu);

    p.register(Airport.class, "Airports", new Func<Iterable<Airport>>() {

      @Override
      public Iterable<Airport> apply() {
        return airports;
      }

    }, "Code");

    // register the producer as the static instance, then launch the http server
    DefaultODataProducerProvider.setInstance(p);
    server = this.rtFacade.startODataServer(endpointUri);
    return p.getMetadata();
  }

  public static final String namespaceUri = "http://tempuri.org/test";
  public static final String prefix = "od4j";

  @Override
  public List<PrefixedNamespace> getNamespaces() {
    List<PrefixedNamespace> l = new ArrayList<PrefixedNamespace>();
    l.add(new PrefixedNamespace(namespaceUri, prefix));
    return l;
  }

  @Override
  public EdmDocumentation getDocumentationForEntityType(String namespace, String typeName) {
    if (typeName.equals("Airports")) {
      return new EdmDocumentation("This is the summary documentation for Airport",
          "This is the long description for Airport.");
    }
    return null;
  }

  @Override
  public Object resolveStructuralTypeProperty(EdmStructuralType st, PropertyPath path) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Object resolvePropertyProperty(EdmProperty st, PropertyPath path) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Object getAnnotationValueOverride(EdmItem item, NamespacedAnnotation<?> annot, boolean flatten, Locale locale, Map<String, String> options) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void decorateEntity(EdmEntitySet entitySet, EdmItem item, EdmItem originalQueryItem,
      List<OProperty<?>> props, boolean flatten, Locale locale, Map<String, String> options) {}

  @Override
  public EdmDocumentation getDocumentationForSchema(String namespace) {
    return null;
  }

  @Override
  public List<EdmAnnotation<?>> getAnnotationsForSchema(String namespace) {
    return null;
  }

  public static class ComplexAnnot {
    public ComplexAnnot(String a1, String a2) {
      this.annotprop1 = a1;
      this.annotprop2 = a2;
    }

    public String annotprop1;
    public String annotprop2;
  }

  @Override
  public List<EdmAnnotation<?>> getAnnotationsForEntityType(String namespace, String typeName) {
    List<EdmAnnotation<?>> a = null;
    if (typeName.equals("Airports")) {
      a = new ArrayList<EdmAnnotation<?>>();
      a.add(new EdmAnnotationAttribute(namespaceUri, prefix, "writable", "false"));
      a.add(EdmAnnotation.element(namespaceUri, prefix, "foo", ComplexAnnot.class, new ComplexAnnot("annotation one", "annotation two")));
    }
    return a;
  }

  @Override
  public EdmDocumentation getDocumentationForProperty(String namespace, String typename, String propName) {
    if (typename.equals("Airports")) {
      if (propName.equals("Code")) {
        return new EdmDocumentation("The FAA airport code", "the code..blah...blah");
      }
    }
    return null;
  }

  @Override
  public List<EdmAnnotation<?>> getAnnotationsForProperty(String namespace, String typeName, String propName) {
    List<EdmAnnotation<?>> a = null;
    if (typeName.equals("Airports")) {
      if (propName.equals("Code")) {
        a = new ArrayList<EdmAnnotation<?>>();
        a.add(new EdmAnnotationAttribute(namespaceUri, prefix, "localizedName", "Kode"));
        a.add(EdmAnnotation.element(namespaceUri, prefix, "perms", ComplexAnnot.class, new ComplexAnnot("prop annotation one", "prop annotation two")));
      }
    }
    return a;
  }
}
