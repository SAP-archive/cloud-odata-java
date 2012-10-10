package org.odata4j.test.unit.format.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;
import org.odata4j.edm.EdmAnnotationElement;
import org.odata4j.edm.EdmAssociation;
import org.odata4j.edm.EdmAssociationSet;
import org.odata4j.edm.EdmComplexType;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntityContainer;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.edm.EdmProperty;
import org.odata4j.format.xml.EdmxFormatParser;
import org.odata4j.format.xml.EdmxFormatWriter;
import org.odata4j.stax2.XMLEventReader2;
import org.odata4j.stax2.util.StaxUtil;

public class EdmxFormatParserTest {

  public EdmxFormatParserTest() {}

  // a "unit" edmx
  private String edmxFile = "/META-INF/edmx.xml";
  // an SAP Data Services sample edmx
  private String sapDsSampleEdmxFile = "/META-INF/sap_ds_sample_edmx.xml";
  private final String MYNS = "bla";
  private final String MYNAMESPACE = "http://tempuri.org/hello";

  @Test
  public void testInheritance() throws FileNotFoundException, InterruptedException {

    // do the raw xml first...
    XMLEventReader2 reader = StaxUtil.newXMLEventReader(new BufferedReader(
        new InputStreamReader(getClass().getResourceAsStream(edmxFile))));
    EdmDataServices d = new EdmxFormatParser().parseMetadata(reader);
    assertTrue("parsed", d != null);

    checkTypeHierarchy(d);

    // now take the parsed result, back to xml, re-parse, check that...
    StringWriter sw = new StringWriter();
    EdmxFormatWriter.write(d, sw);

    EdmDataServices d2 = new EdmxFormatParser().parseMetadata(StaxUtil.newXMLEventReader(new StringReader(sw.toString())));
    assertTrue("parsed", d2 != null);

    checkTypeHierarchy(d2);
  }

  @Test
  public void parseSapDsSample() {
    XMLEventReader2 reader = StaxUtil.newXMLEventReader(new BufferedReader(
        new InputStreamReader(getClass().getResourceAsStream(sapDsSampleEdmxFile))));
    EdmDataServices d = new EdmxFormatParser().parseMetadata(reader);
    assertTrue("parsed", d != null);
  }

  private void checkTypeHierarchy(EdmDataServices d) {
    EdmEntitySet airportSet = d.findEdmEntitySet("Airport");
    EdmEntityType airport = airportSet.getType();
    //EdmEntityType airport = d.findEdmEntitySet("Airport").getType();
    EdmEntityType badAirport = d.findEdmEntitySet("BadAirport").getType();
    assertTrue(badAirport.getBaseType().equals(airport));
    assertTrue("" + badAirport.getKeys() + ":" + airport.getKeys(), badAirport.getKeys().equals(airport.getKeys()));
    assertTrue(badAirport.getDeclaredNavigationProperties().count() == 0);
    assertEquals(badAirport.getNavigationProperties().count(),
        airport.getDeclaredNavigationProperties().count() +
            badAirport.getDeclaredNavigationProperties().count());
    assertTrue(badAirport.getDeclaredProperties().count() == 2);
    assertTrue(badAirport.findDeclaredProperty("rating") != null);
    assertTrue(badAirport.findDeclaredProperty("prop2") != null);
    assertTrue(badAirport.getProperties().count() == airport.getDeclaredProperties().count() +
        badAirport.getDeclaredProperties().count());
    assertTrue(badAirport.findProperty("name") != null);
    assertTrue(badAirport.findProperty("code") != null);
    assertTrue(badAirport.findProperty("country") != null);
    assertTrue(badAirport.findProperty("rating") != null);
    assertTrue(badAirport.findProperty("prop2") != null);
    assertTrue(airport.findAnnotation("http://tempuri.org/hello", "AnnotationAttribute") != null);

    EdmEntityType schedule = d.findEdmEntitySet("FlightSchedule").getType();
    EdmEntityType subSchedule = d.findEdmEntitySet("SubFlightSchedule").getType();
    assertTrue(subSchedule.getBaseType().equals(schedule));
    assertTrue(subSchedule.getKeys().equals(schedule.getKeys()));
    assertTrue(subSchedule.getDeclaredNavigationProperties().count() == 0);
    assertEquals(2, subSchedule.getNavigationProperties().count());
    assertTrue(subSchedule.getNavigationProperties().count() == schedule.getDeclaredNavigationProperties().count() +
        subSchedule.getDeclaredNavigationProperties().count());

    assertTrue(subSchedule.getDeclaredProperties().count() == 3);
    assertTrue(subSchedule.findDeclaredProperty("prop3") != null);
    assertTrue(subSchedule.findDeclaredProperty("prop4") != null);
    assertTrue(subSchedule.findDeclaredProperty("prop5") != null);
    assertTrue(subSchedule.getProperties().count() == schedule.getDeclaredProperties().count() +
        subSchedule.getDeclaredProperties().count());
    assertTrue(subSchedule.findProperty("arrivalAirportCode") != null);
    assertTrue(subSchedule.findProperty("flightScheduleID") != null);
    assertTrue(subSchedule.findProperty("arrivalTime") != null);
    assertTrue(subSchedule.findProperty("flightNo") != null);
    assertTrue(subSchedule.findProperty("firstDeparture") != null);
    assertTrue(subSchedule.findProperty("departureTime") != null);
    assertTrue(subSchedule.findProperty("departureAirportCode") != null);
    assertTrue(subSchedule.findProperty("lastDeparture") != null);
    assertTrue(subSchedule.findProperty("prop3") != null);
    assertTrue(subSchedule.findProperty("prop4") != null);
    assertTrue(subSchedule.findProperty("prop5") != null);

    EdmEntityType subsubSchedule = d.findEdmEntitySet("SubSubFlightSchedule").getType();
    assertTrue(subsubSchedule.getBaseType().equals(subSchedule));
    assertTrue(subsubSchedule.getKeys().equals(subSchedule.getKeys()));
    assertTrue(subsubSchedule.getDeclaredNavigationProperties().count() == 1);
    assertTrue(subsubSchedule.getNavigationProperties().count() == schedule.getDeclaredNavigationProperties().count() +
        subSchedule.getDeclaredNavigationProperties().count() +
        subsubSchedule.getDeclaredNavigationProperties().count());

    assertTrue(subsubSchedule.getDeclaredProperties().count() == 4);
    assertTrue(subsubSchedule.getProperties().count() == subsubSchedule.getDeclaredProperties().count() +
        subSchedule.getDeclaredProperties().count() +
        schedule.getDeclaredProperties().count());
  }

  @Test
  public void testProperty() {
    XMLEventReader2 reader = StaxUtil.newXMLEventReader(new BufferedReader(
        new InputStreamReader(getClass().getResourceAsStream("/META-INF/property.xml"))));
    EdmDataServices d = new EdmxFormatParser().parseMetadata(reader);
    assertTrue("parsed", d != null);

    EdmEntityType product = d.findEdmEntitySet("Products").getType();
    EdmProperty property = product.findProperty("Name");
    assertTrue(property != null);
    assertTrue(property.getCollation() != null);
    assertTrue(property.getUnicode() != null);
    assertTrue(property.getFixedLength() != null);

    assertNotNull(property.findAnnotationElement("bla", "MyElement"));
    assertEquals("MyElement", property.findAnnotationElement("bla", "MyElement").getName());
  }

  @Test
  public void testAssociationEnd() {
    XMLEventReader2 reader = StaxUtil.newXMLEventReader(new BufferedReader(
        new InputStreamReader(getClass().getResourceAsStream("/META-INF/associationEnd.xml"))));
    EdmDataServices d = new EdmxFormatParser().parseMetadata(reader);
    assertTrue("parsed", d != null);
    for (EdmAssociation association : d.getAssociations()) {
      if (association.getName().equals("ProductCategory")) {
        if (association.getEnd1().getRole().equals("Category")) {
          assertTrue(association.getEnd1().getOnDeleteAction() != null);
        } else {
          assertTrue(association.getEnd2().getOnDeleteAction() != null);
        }
      }
    }
  }

  @Test
  public void testContainer() {
    XMLEventReader2 reader = StaxUtil.newXMLEventReader(new BufferedReader(
        new InputStreamReader(getClass().getResourceAsStream("/META-INF/container.xml"))));
    EdmDataServices d = new EdmxFormatParser().parseMetadata(reader);
    assertTrue("parsed", d != null);
    EdmEntityContainer container2 = d.findSchema("Example").findEntityContainer("Container2");
    assertTrue(container2 != null);
    assertTrue(container2.getExtendz() != null);
    assertTrue(container2.getLazyLoadingEnabled() != null);
  }

  @Test
  public void testReferentialConstraint() {
    XMLEventReader2 reader = StaxUtil.newXMLEventReader(new BufferedReader(
        new InputStreamReader(getClass().getResourceAsStream("/META-INF/Constraint.xml"))));
    EdmDataServices d = new EdmxFormatParser().parseMetadata(reader);
    assertTrue("parsed", d != null);
    for (EdmAssociation association : d.getAssociations()) {
      if (association.getName().equals("EmployeeManager")) {
        assertTrue(association.getRefConstraint() != null);
        assertEquals("Manager", association.getRefConstraint().getDependentRole());
        assertEquals("Employee", association.getRefConstraint().getPrincipalRole());
      }
    }
  }

  @Test
  public void testAnnotationElement() {
    XMLEventReader2 reader = StaxUtil.newXMLEventReader(new BufferedReader(
        new InputStreamReader(getClass().getResourceAsStream("/META-INF/annotation.xml"))));
    EdmDataServices d = new EdmxFormatParser().parseMetadata(reader);
    assertTrue("parsed", d != null);

    EdmEntityContainer container = d.findSchema("Example").findEntityContainer("Container");
    EdmEntitySet products = d.findEdmEntitySet("Products");
    EdmEntityType product = products.getType();
    EdmProperty property = product.findProperty("Name");
    EdmAssociation association = d.findEdmAssociation("ProductCategory");
    EdmComplexType complexType = d.findEdmComplexType("Example.City");
    EdmAssociationSet associationSet = d.findEdmAssociationSet("ProductCategory");
    EdmFunctionImport functionImport = d.findEdmFunctionImport("ProductSearch");

    // ...............Schema ............//
    assertNotNull(d.findSchema("Example").findAnnotationElement(MYNS, "MyContainer"));
    assertEquals("AnnotElement", d.findSchema("Example").findAnnotationElement(MYNAMESPACE, "AnnotElement").getName());
    assertEquals("MyContainer", d.findSchema("Example").findAnnotationElement(MYNS, "MyContainer").getName());

    // ............... Container ............//
    assertNotNull(container.findAnnotationElement(MYNS, "MyEntitySet"));
    assertEquals("MyEntitySet", container.findAnnotationElement(MYNS, "MyEntitySet").getName());

    // ............... EntitySet ............//
    assertNotNull(products.findAnnotationElement(MYNS, "MySubSet"));

    // ............... EntityType ............//
    assertNotNull(product.findAnnotationElement(MYNAMESPACE, "MyEntityTypeProperty"));
    assertEquals("MyEntityTypeProperty", product.findAnnotationElement(MYNAMESPACE, "MyEntityTypeProperty").getName());

    // ............... in Property ............//
    assertNotNull(property.findAnnotationElement(MYNS, "mySubproperty"));
    assertEquals("mySubproperty", property.findAnnotationElement(MYNS, "mySubproperty").getName());

    // ...............Association ............// 
    assertNotNull(association.findAnnotationElement(MYNAMESPACE, "MyAssociation"));

    // ...............AssociationEnd ............//
    if (association.getEnd1().getRole().equals("r_Product")) {
      assertNotNull(association.getEnd1().findAnnotationElement(MYNS, "MyAssociationEnd"));
    } else {
      assertNotNull(association.getEnd2().findAnnotationElement(MYNS, "MyAssociationEnd"));
    }

    assertNotNull(complexType.findAnnotationElement(MYNS, "MyComplexTypeProperty"));
    assertNotNull(product.findNavigationProperty("n_Category").findAnnotationElement(MYNS, "MyNavigation"));

    // ............... FunctionImport ............//
    assertEquals("MyFunctionImport", functionImport.findAnnotationElement(MYNS, "MyFunctionImport").getName());

    if (functionImport.getParameters().get(0).getName().equals("q")) {
      assertNotNull(functionImport.getParameters().get(0).findAnnotationElement(MYNS, "MyAnnotation"));
    }

    // ............... AssociationSet ............//
    assertEquals("MyAssociationSet", associationSet.findAnnotationElement(MYNAMESPACE, "MyAssociationSet").getName());

    // ............... AssociationSetEnd ........//
    if (associationSet.getEnd1().getRole().getRole().equals("r_Product")) {
      assertNotNull(associationSet.getEnd1().findAnnotationElement(MYNS, "MyEndElement"));
      assertEquals("MyEndElement", associationSet.getEnd1().findAnnotationElement(MYNS, "MyEndElement").getName());
    } else {
      assertNotNull(associationSet.getEnd2().findAnnotationElement(MYNS, "MyEndElement"));
      assertEquals("MyEndElement", associationSet.getEnd2().findAnnotationElement(MYNS, "MyEndElement").getName());
    }
  }

  @Test
  public void testForRecursion() {
    XMLEventReader2 reader = StaxUtil.newXMLEventReader(new BufferedReader(
        new InputStreamReader(getClass().getResourceAsStream("/META-INF/annotation.xml"))));
    EdmDataServices d = new EdmxFormatParser().parseMetadata(reader);
    assertTrue("parsed", d != null);

    EdmAnnotationElement<?> annot = (EdmAnnotationElement<?>) d.findSchema("Example").findAnnotationElement(MYNAMESPACE, "AnnotElement");

    assertNotNull(annot);
    assertNotNull(annot.findAnnotationElement(MYNAMESPACE, "testing"));
    assertNotNull(annot.findAnnotationElement(MYNAMESPACE, "another"));

    EdmAnnotationElement<?> annot2 = (EdmAnnotationElement<?>) annot.findAnnotationElement(MYNAMESPACE, "another");
    assertEquals("bar", annot2.findAnnotation(MYNAMESPACE, "foo").getValue());
    assertNotNull(annot2.findAnnotationElement(MYNAMESPACE, "yetanother"));
    assertEquals("yetanother", annot2.findAnnotationElement(MYNAMESPACE, "yetanother").getValue().toString().trim());
    assertEquals("mynamespace", annot2.findAnnotationElement(MYNAMESPACE, "yetanother").getNamespace().getPrefix());
    assertEquals(MYNAMESPACE, annot2.findAnnotationElement(MYNAMESPACE, "yetanother").getNamespace().getUri());
  }

  @Test
  public void testForAnnotationAttribute() {
    XMLEventReader2 reader = StaxUtil.newXMLEventReader(new BufferedReader(
        new InputStreamReader(getClass().getResourceAsStream("/META-INF/annotationAttribute.xml"))));
    EdmDataServices d = new EdmxFormatParser().parseMetadata(reader);
    assertTrue("parsed", d != null);

    EdmEntitySet products = d.findEdmEntitySet("Products");
    EdmEntityType product = products.getType();
    EdmProperty property = product.findProperty("Name");
    assertEquals("001", property.findAnnotation(MYNS, "myattr").getValue());
    assertNotNull(property.findAnnotationElement(MYNS, "myElement"));
  }

}