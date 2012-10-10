package org.odata4j.test.unit.issues;

import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmSchema;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.format.xml.EdmxFormatWriter;

// http://code.google.com/p/odata4j/issues/detail?id=177
public class Issue177Test {

  @Test
  public void testPrecisionAndScale() {

    // build simplest edm containing one decimal property
    EdmProperty.Builder edmPropertyBuilder = EdmProperty.newBuilder("DecimalProperty").setType(EdmSimpleType.DECIMAL).setPrecision(2).setScale(10);
    List<EdmProperty.Builder> builderProperties = new ArrayList<EdmProperty.Builder>();
    builderProperties.add(edmPropertyBuilder);

    List<String> keys = new ArrayList<String>();
    keys.add("DecimalProperty");

    EdmEntityType.Builder entityTypeBuilder = EdmEntityType.newBuilder().setName("TypeName");
    entityTypeBuilder.addProperties(builderProperties);
    entityTypeBuilder.addKeys(keys);

    ArrayList<EdmEntityType.Builder> builderEntityTypes = new ArrayList<EdmEntityType.Builder>();
    builderEntityTypes.add(entityTypeBuilder);

    EdmSchema.Builder edmSchemaBuilder = EdmSchema.newBuilder().setNamespace("Namespace");
    edmSchemaBuilder.addEntityTypes(builderEntityTypes);

    EdmDataServices.Builder edmDataServiceBuilder = EdmDataServices.newBuilder();
    edmDataServiceBuilder.addSchemas(edmSchemaBuilder);

    // print xml
    StringWriter sw = new StringWriter();
    EdmxFormatWriter.write(edmDataServiceBuilder.build(), sw);
    String edmAsString = sw.toString();

    // check it
    assertTrue(edmAsString.contains("<Property Name=\"DecimalProperty\" Type=\"Edm.Decimal\" Nullable=\"false\" Precision=\"2\" Scale=\"10\"></Property>"));
  }
}
