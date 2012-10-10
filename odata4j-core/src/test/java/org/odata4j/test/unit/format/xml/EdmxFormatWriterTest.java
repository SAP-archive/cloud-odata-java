package org.odata4j.test.unit.format.xml;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Before;
import org.junit.Test;
import org.odata4j.edm.EdmAnnotation;
import org.odata4j.edm.EdmAnnotationAttribute;
import org.odata4j.edm.EdmAnnotationElement;
import org.odata4j.edm.EdmAssociation;
import org.odata4j.edm.EdmAssociationEnd;
import org.odata4j.edm.EdmAssociationSet;
import org.odata4j.edm.EdmAssociationSetEnd;
import org.odata4j.edm.EdmCollectionType;
import org.odata4j.edm.EdmComplexType;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntityContainer;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.edm.EdmFunctionParameter;
import org.odata4j.edm.EdmMultiplicity;
import org.odata4j.edm.EdmNavigationProperty;
import org.odata4j.edm.EdmOnDeleteAction;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmProperty.CollectionKind;
import org.odata4j.edm.EdmReferentialConstraint;
import org.odata4j.edm.EdmSchema;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.format.xml.EdmxFormatWriter;
import org.xml.sax.SAXException;

public class EdmxFormatWriterTest {

  private static final String NAMESPACE = "Example";
  private static final String TEMPURI = "http://tempuri.org/hello";
  private static final Boolean OPENTYPE_TRUE = true;
  private static final String SCHEMA = "Schema";
  private static final String COLLATING_SEQUENCE = "DIN 5007-1";

  private EdmSchema.Builder schema;
  private EdmEntitySet.Builder productSet;
  private EdmEntitySet.Builder categorySet;
  private EdmEntityContainer.Builder container;

  @Before
  public void before() {
    HashMap<String, String> m = new HashMap<String, String>();
    m.put("edmx", "http://schemas.microsoft.com/ado/2007/06/edmx");
    m.put("d", "http://schemas.microsoft.com/ado/2007/08/dataservices");
    m.put("m", "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata");
    m.put("edm", "http://schemas.microsoft.com/ado/2008/09/edm");
    m.put("annotation", "http://schemas.microsoft.com/ado/2009/02/edm/annotation");
    m.put("myns", "bla");
    m.put("myns2", "blabla");
    m.put("mynamespace", "http://tempuri.org/hello");
    SimpleNamespaceContext ctx = new SimpleNamespaceContext(m);
    XMLUnit.setXpathNamespaceContext(ctx);
    schema = EdmSchema.newBuilder().setNamespace(NAMESPACE);
    productSet = EdmEntitySet.newBuilder().setName("Products");
    categorySet = EdmEntitySet.newBuilder().setName("Categories");
    container = EdmEntityContainer.newBuilder().setName("Container");
  }

  @Test
  public void testReferentialConstraint() throws XpathException, IOException, SAXException {

    EdmEntityType.Builder employee = EdmEntityType.newBuilder()
        .setName("Employee")
        .setNamespace(NAMESPACE)
        .addKeys("EmployeeID");
    EdmEntityType.Builder manager = EdmEntityType.newBuilder()
        .setName("Manager")
        .setNamespace(NAMESPACE)
        .addKeys("ManagerID");
    List<EdmAssociation.Builder> associations = new ArrayList<EdmAssociation.Builder>();
    EdmAssociationEnd.Builder from = EdmAssociationEnd.newBuilder()
        .setRole("Employees")
        .setType(employee)
        .setMultiplicity(EdmMultiplicity.ONE);
    EdmAssociationEnd.Builder to = EdmAssociationEnd.newBuilder()
        .setRole("Manager")
        .setType(manager)
        .setMultiplicity(EdmMultiplicity.ZERO_TO_ONE);

    EdmReferentialConstraint.Builder constraint = EdmReferentialConstraint.newBuilder().setPrincipalRole("Employee").setDependentRole("Manager");
    constraint = constraint.addPrincipalReferences("EmployeeID").addDependentReferences("ManagerID");
    associations.add(EdmAssociation.newBuilder().setName("EmployeeManager").setEnds(from, to).setRefConstraint(constraint));
    schema.addAssociations(associations);
    EdmDataServices.Builder serviceBuilder = EdmDataServices.newBuilder().addSchemas(schema);
    EdmDataServices edmService = serviceBuilder.build();

    StringWriter writer = new StringWriter();
    EdmxFormatWriter.write(edmService, writer);
    String xml = writer.toString();

    assertThat(xml, containsString("<ReferentialConstraint>"));
    assertThat(xml, containsString("<Principal Role=\"Employee\">"));
    assertThat(xml, containsString("<Dependent Role=\"Manager\">"));
    assertThat(xml, containsString("<PropertyRef Name=\"EmployeeID\">"));
    assertThat(xml, containsString("<PropertyRef Name=\"ManagerID\">"));

    assertXpathExists("//edm:Schema/edm:Association/edm:ReferentialConstraint", xml);
    assertXpathExists("//edm:Schema/edm:Association/edm:ReferentialConstraint/edm:Principal/@Role", xml);
    assertXpathExists("//edm:Schema/edm:Association/edm:ReferentialConstraint/edm:Dependent/@Role", xml);
    assertXpathExists("//edm:Schema/edm:Association/edm:ReferentialConstraint/edm:Dependent/edm:PropertyRef/@Name", xml);

  }

  @Test
  public void testEntityType() throws XpathException, IOException, SAXException {

    EdmEntityType.Builder productBuilder = EdmEntityType.newBuilder();
    productBuilder = productBuilder.setName("Product").setOpenType(OPENTYPE_TRUE).addKeys("ProductId");
    EdmSchema.Builder schema = EdmSchema.newBuilder().setNamespace(SCHEMA);
    schema.addEntityTypes(productBuilder);

    EdmDataServices.Builder serviceBuilder = EdmDataServices.newBuilder().addSchemas(schema);
    EdmDataServices edmService = serviceBuilder.build();

    StringWriter writer = new StringWriter();
    EdmxFormatWriter.write(edmService, writer);
    String xml2 = writer.toString();

    assertThat(xml2, containsString("<EntityType OpenType=\"true\" Name=\"Product\">"));

    assertXpathExists("//edm:Schema/edm:EntityType/@OpenType", xml2);
  }

  @Test
  public void testProperty() throws XpathException, IOException, SAXException {
    List<EdmProperty.Builder> properties = new ArrayList<EdmProperty.Builder>();
    EdmEntityType.Builder product = EdmEntityType.newBuilder().setNamespace(NAMESPACE);
    product = product.setName("Product").addKeys("ProductId");
    EdmProperty.Builder property = EdmProperty.newBuilder("ProductId").setType(EdmSimpleType.INT32).setNullable(false);
    properties.add(property);

    property = EdmProperty.newBuilder("Name").setType(EdmSimpleType.STRING).setFixedLength(true);
    property = property.setUnicode(true).setCollation(COLLATING_SEQUENCE);
    properties.add(property);
    product.addProperties(properties);

    // .........................................................................
    EdmAnnotationElement<String> elem = EdmAnnotation.element("bla", "myns", "myProperty", String.class, "hello");

    EdmAnnotationAttribute attr = EdmAnnotation.attribute("blabla", "myns2", "Name", "hello");
    List<EdmAnnotation<?>> annotationAttributes = new ArrayList<EdmAnnotation<?>>();
    annotationAttributes.add(attr);
    elem.setAnnotations(annotationAttributes);

    List<EdmAnnotation<?>> annotations2 = new ArrayList<EdmAnnotation<?>>();
    annotations2.add(elem);

    product.setAnnotationElements(annotations2);
    // .............................................................................
    productSet.setEntityType(product);
    container.addEntitySets(productSet);
    schema.addEntityTypes(product);
    schema.addEntityContainers(container);

    EdmDataServices.Builder serviceBuilder = EdmDataServices.newBuilder().addSchemas(schema);
    EdmDataServices edmService = serviceBuilder.build();

    StringWriter writer = new StringWriter();
    EdmxFormatWriter.write(edmService, writer);
    String xml = writer.toString();

    assertThat(xml, containsString("FixedLength=\"true\""));
    assertThat(xml, containsString("Unicode=\"true\""));
    assertThat(xml, containsString("Collation=\"DIN 5007-1\""));

    assertXpathExists("//edm:Schema/edm:EntityType/edm:Property/@FixedLength", xml);
    assertXpathExists("//edm:Schema/edm:EntityType/edm:Property/@Unicode", xml);
    assertXpathExists("//edm:Schema/edm:EntityType/edm:Property/@Collation", xml);
  }

  @Test
  public void testAssociationEnd() throws XpathException, IOException, SAXException {
    EdmEntityType.Builder product = EdmEntityType.newBuilder()
        .setName("Product")
        .setNamespace(NAMESPACE)
        .addKeys("ProductID");
    EdmEntityType.Builder category = EdmEntityType.newBuilder()
        .setName("Category")
        .setNamespace(NAMESPACE)
        .addKeys("CategoryID");

    List<EdmAssociation.Builder> associations = new ArrayList<EdmAssociation.Builder>();

    EdmAssociationEnd.Builder fromRole = EdmAssociationEnd.newBuilder().setRole("Product");
    fromRole = fromRole.setType(product);
    fromRole = fromRole.setMultiplicity(EdmMultiplicity.MANY);

    EdmAssociationEnd.Builder toRole = EdmAssociationEnd.newBuilder().setRole("Category");
    toRole = toRole.setType(category);
    toRole = toRole.setMultiplicity(EdmMultiplicity.ZERO_TO_ONE);
    toRole = toRole.setOnDeleteAction(EdmOnDeleteAction.CASCADE);
    associations.add(EdmAssociation.newBuilder().setName("ProductCategory").setEnds(fromRole, toRole));

    productSet.setEntityType(product);
    categorySet.setEntityType(category);
    container.addEntitySets(productSet);
    schema.addEntityTypes(product);
    schema.addEntityContainers(container);
    schema.addAssociations(associations);
    EdmDataServices.Builder serviceBuilder = EdmDataServices.newBuilder().addSchemas(schema);
    EdmDataServices edmService = serviceBuilder.build();

    StringWriter writer = new StringWriter();
    EdmxFormatWriter.write(edmService, writer);
    String xml = writer.toString();

    assertThat(xml, containsString("<OnDelete Action=\"Cascade\">"));

    assertXpathExists("//edm:Schema/edm:Association/edm:End/edm:OnDelete/@Action", xml);

  }

  @Test
  public void testContainer() throws XpathException, IOException, SAXException {
    EdmEntityContainer.Builder entityContainer2 = EdmEntityContainer.newBuilder()
        .setName("Container2")
        .setExtendz("Container")
        .setLazyLoadingEnabled(true);

    schema.addEntityContainers(container, entityContainer2);

    EdmDataServices.Builder builder = EdmDataServices.newBuilder().addSchemas(schema);
    EdmDataServices edmService = builder.build();
    StringWriter writer = new StringWriter();
    EdmxFormatWriter.write(edmService, writer);
    String xml = writer.toString();

    assertThat(xml, containsString("Extends=\"Container\""));
    assertThat(xml, containsString("LazyLoadingEnabled=\"true\""));

    assertXpathExists("//edm:Schema/edm:EntityContainer/@Extends", xml);
    assertXpathExists("//edm:Schema/edm:EntityContainer/@annotation:LazyLoadingEnabled", xml);

  }

  @Test
  public void testForAnnotation() throws XpathException, IOException, SAXException {
    String xml = createXML();
    assertXpathExists("//edm:EntityType/edm:Property/myns:mySubproperty", xml);
    assertXpathExists("//edm:EntityType/edm:NavigationProperty/myns:MyNavigation", xml);
    assertXpathExists("//edm:EntityType/mynamespace:MyEntityTypeProperty/mynamespace:MySubProperty", xml);
    assertXpathExists("//edm:ComplexType/myns:MyComplexTypeProperty", xml);
    assertXpathExists("//edm:Association/mynamespace:MyAssociation", xml);
    assertXpathExists("//edm:Association/edm:End/myns:MyAssociationEnd", xml);
    assertXpathExists("//edm:EntityContainer/myns:MyEntitySet", xml);
    assertXpathExists("//edm:EntityContainer/edm:EntitySet/myns:MySubSet", xml);
    assertXpathExists("//edm:EntityContainer/edm:AssociationSet/mynamespace:MyAssociationSet", xml);
    assertXpathExists("//edm:EntityContainer/edm:AssociationSet/edm:End/myns:MyEndElement", xml);
    assertXpathExists("//edm:EntityContainer/edm:FunctionImport/myns:MyFunctionImport", xml);
    assertXpathExists("//edm:EntityContainer/edm:FunctionImport/edm:Parameter/myns:MyAnnotation", xml);
    assertXpathExists("//edm:Schema/myns:MyContainer", xml);
    assertXpathExists("//edm:Schema/mynamespace:AnnotElement/mynamespace:testing", xml);
    assertXpathExists("//edm:Schema/mynamespace:AnnotElement/mynamespace:another", xml);
    assertXpathExists("//edm:Schema/mynamespace:AnnotElement/mynamespace:another/mynamespace:yetanother", xml);
    assertXpathExists("//edm:Schema/mynamespace:AnnotElement/mynamespace:another/@mynamespace:foo", xml);
  }

  private String createXML() {
    List<EdmProperty.Builder> properties = new ArrayList<EdmProperty.Builder>();
    List<EdmAssociationSet.Builder> associationSets = new ArrayList<EdmAssociationSet.Builder>();
    List<EdmAssociation.Builder> associations = new ArrayList<EdmAssociation.Builder>();
    List<EdmComplexType.Builder> complexTypes = new ArrayList<EdmComplexType.Builder>();

    // ----------------- EntityType ------------------//
    EdmEntityType.Builder product = EdmEntityType.newBuilder()
        .setName("Product")
        .setNamespace(NAMESPACE)
        .addKeys("ProductID");
    EdmProperty.Builder property = EdmProperty.newBuilder("ProductId").setType(EdmSimpleType.INT32).setNullable(false);
    properties.add(property);

    property = EdmProperty.newBuilder("Name").setType(EdmSimpleType.STRING);
    property.setAnnotationElements(createAnnotationElement("myns", "bla", "mySubproperty", "hello"));
    properties.add(property);
    product.addProperties(properties);
    // ----------------- Annotations ------------------//
    List<EdmAnnotation<?>> annotations = new ArrayList<EdmAnnotation<?>>();
    List<EdmAnnotation<?>> annotations2 = new ArrayList<EdmAnnotation<?>>();
    List<EdmAnnotation<?>> annotationSubEl = new ArrayList<EdmAnnotation<?>>();
    EdmAnnotationElement<String> elem = EdmAnnotation.element(TEMPURI, "mynamespace", "MyEntityTypeProperty", String.class, "");
    EdmAnnotationElement<String> subelem = EdmAnnotation.element(TEMPURI, "mynamespace", "MySubProperty", String.class, "another");
    annotationSubEl.add(subelem);

    EdmAnnotationAttribute attr = EdmAnnotation.attribute(TEMPURI, "mynamespace", "myattr", "123");
    annotations.add(attr);

    elem.setAnnotations(annotations);
    elem.setAnnotationElements(annotationSubEl);
    annotations2.add(elem);
    product.setAnnotationElements(annotations2);

    productSet.setEntityType(product);
    productSet.setAnnotationElements(createAnnotationElement("myns", "bla", "MySubSet", "testEntitySet"));

    // ----------------- EntityType ------------------//
    properties.clear();
    EdmEntityType.Builder category = EdmEntityType.newBuilder()
        .setName("Category")
        .setNamespace(NAMESPACE)
        .addKeys("CategoryID");
    property = EdmProperty.newBuilder("CategoryID").setType(EdmSimpleType.INT32).setNullable(false);
    properties.add(property);
    property = EdmProperty.newBuilder("Name").setType(EdmSimpleType.STRING);
    properties.add(property);
    category.addProperties(properties);

    categorySet.setEntityType(category);

    // ----------------- Complex Type ------------------//
    properties.clear();
    EdmComplexType.Builder complexTypeCity = EdmComplexType.newBuilder().setName("City").setNamespace(NAMESPACE);

    property = EdmProperty.newBuilder("PostalCode").setType(EdmSimpleType.STRING).setNullable(true);
    properties.add(property);

    property = EdmProperty.newBuilder("CityName").setType(EdmSimpleType.STRING).setNullable(true);
    properties.add(property);

    complexTypeCity.addProperties(properties);
    complexTypeCity.setAnnotationElements(createAnnotationElement("myns", "bla", "MyComplexTypeProperty", "complextype"));
    complexTypes.add(complexTypeCity);

    // ----------------- Association ------------------//
    EdmAssociationEnd.Builder fromRole = EdmAssociationEnd.newBuilder().setRole("r_Product");
    fromRole = fromRole.setType(product);
    fromRole = fromRole.setMultiplicity(EdmMultiplicity.MANY);
    fromRole = fromRole.setAnnotationElements(createAnnotationElement("myns", "bla", "MyAssociationEnd", "test"));

    EdmAssociationEnd.Builder toRole = EdmAssociationEnd.newBuilder().setRole("r_Category");
    toRole = toRole.setType(category);
    toRole = toRole.setMultiplicity(EdmMultiplicity.ZERO_TO_ONE);
    EdmAssociation.Builder association = EdmAssociation.newBuilder().setName("ProductCategory")
        .setEnds(fromRole, toRole).setNamespace(NAMESPACE);
    association.setAnnotationElements(createAnnotationElement("mynamespace", TEMPURI, "MyAssociation", ""));
    associations.add(association);

    // ----------------- NavigationProperty ------------------//
    EdmNavigationProperty.Builder navigationProperty = EdmNavigationProperty.newBuilder("n_Category");
    navigationProperty.setFromTo(fromRole, toRole);

    navigationProperty.setRelationship(association);

    navigationProperty.setAnnotationElements(createAnnotationElement("myns", "bla", "MyNavigation", "x0x"));

    product.addNavigationProperties(navigationProperty);

    // ----------------- AssociationSet ------------------//
    EdmAssociationSet.Builder associationSet = EdmAssociationSet.newBuilder();
    associationSet.setName("ProductCategory");
    associationSet.setAssociation(association);
    associationSet.setAnnotationElements(createAnnotationElement("mynamespace", TEMPURI, "MyAssociationSet", "test"));

    EdmAssociationSetEnd.Builder associationSetEnd1 = EdmAssociationSetEnd.newBuilder().setEntitySet(productSet).setRole(fromRole);
    associationSetEnd1.setAnnotationElements(createAnnotationElement("myns", "bla", "MyEndElement", ""));
    EdmAssociationSetEnd.Builder associationSetEnd2 = EdmAssociationSetEnd.newBuilder().setEntitySet(categorySet).setRole(toRole);
    associationSet.setEnds(associationSetEnd1, associationSetEnd2);
    associationSets.add(associationSet);

    // ------ Function Product Search ------ //
    EdmFunctionImport.Builder functionImport = EdmFunctionImport.newBuilder().setName("ProductSearch");
    functionImport = functionImport.setEntitySet(productSet).setHttpMethod("GET");
    functionImport = functionImport.setReturnType(EdmCollectionType.newBuilder().setKind(CollectionKind.Collection).setCollectionType(product));
    functionImport.setAnnotationElements(createAnnotationElement("myns", "bla", "MyFunctionImport", "testFunctionImport"));
    EdmFunctionParameter.Builder functionImportParameter = EdmFunctionParameter.newBuilder().setName("q").setType(EdmSimpleType.STRING);
    functionImportParameter.setAnnotationElements(createAnnotationElement("myns", "bla", "MyAnnotation", "test"));
    functionImport.addParameters(functionImportParameter);

    // ----------------- Container ------------------//
    container.setAnnotationElements(createAnnotationElement("myns", "bla", "MyEntitySet", "testContainer"));
    container.addEntitySets(productSet, categorySet);
    container.addAssociationSets(associationSets);
    container.addFunctionImports(functionImport);

    // ----------------- Schema ------------------//
    schema.addEntityTypes(product, category);
    schema.addComplexTypes(complexTypes);
    schema.addAssociations(associations);
    schema.addEntityContainers(container);

    // ------------- Annotations -------------//
    List<EdmAnnotation<?>> annot = new ArrayList<EdmAnnotation<?>>();
    List<EdmAnnotation<?>> annotElem = new ArrayList<EdmAnnotation<?>>();
    List<EdmAnnotation<?>> annotSubElem = new ArrayList<EdmAnnotation<?>>();
    List<EdmAnnotation<?>> annotSubElem2 = new ArrayList<EdmAnnotation<?>>();
    EdmAnnotationElement<String> element = EdmAnnotation.element(TEMPURI, "mynamespace", "AnnotElement", String.class, "");
    EdmAnnotationElement<String> subelement = EdmAnnotation.element(TEMPURI, "mynamespace", "testing", String.class, "");
    EdmAnnotationElement<String> subelement2 = EdmAnnotation.element(TEMPURI, "mynamespace", "another", String.class, "");
    EdmAnnotationElement<String> subelement3 = EdmAnnotation.element(TEMPURI, "mynamespace", "yetanother", String.class, "yetanother");

    annotSubElem2.add(subelement3);
    subelement2.setAnnotationElements(annotSubElem2);

    annotSubElem.add(subelement);
    annotSubElem.add(subelement2);

    EdmAnnotationAttribute attribute = EdmAnnotation.attribute(TEMPURI, "mynamespace", "foo", "bar");
    annot.add(attribute);

    subelement2.setAnnotations(annot);
    element.setAnnotationElements(annotSubElem);
    annotElem.addAll(createAnnotationElement("myns", "bla", "MyContainer", "testSchema"));
    annotElem.add(element);

    schema.setAnnotationElements(annotElem);
    // ----------------- DataService ------------------//
    EdmDataServices.Builder serviceBuilder = EdmDataServices.newBuilder().addSchemas(schema);
    EdmDataServices edmService = serviceBuilder.build();

    StringWriter writer = new StringWriter();
    EdmxFormatWriter.write(edmService, writer);
    String xml = writer.toString();
    return xml;
  }

  private List<EdmAnnotation<?>> createAnnotationElement(String namespace, String uri, String name, String value) {
    List<EdmAnnotation<?>> annotations2 = new ArrayList<EdmAnnotation<?>>();

    EdmAnnotationElement<String> elem = EdmAnnotation.element(uri, namespace, name, String.class, value);
    annotations2.add(elem);

    return annotations2;
  }
}
