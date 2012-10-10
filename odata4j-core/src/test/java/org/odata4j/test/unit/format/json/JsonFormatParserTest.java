package org.odata4j.test.unit.format.json;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.StringReader;

import org.junit.Test;
import org.odata4j.core.ODataVersion;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntityContainer;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmSchema;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.format.Feed;
import org.odata4j.format.FormatParser;
import org.odata4j.format.FormatParserFactory;
import org.odata4j.format.FormatType;
import org.odata4j.format.Settings;

public class JsonFormatParserTest {

  @Test
  public void ensureMetadataWithArrayOrObjectCanBeParsed() throws Exception {
    StringReader json = new StringReader("" +
        "{ \"d\": {" +
        "\"__metadata\": {" +
        "\"uri\": \"http://localhost:8180/databinding/odata.svc/Products(2)\"," +
        "\"uri_extensions\": []," + // (empty) array
        "\"edit\": \"http://localhost:8180/databinding/odata.svc/Products(2)\"," +
        "\"edit_link_extensions\": [ { \"name\": \"title\", \"namespaceURI\": null, \"value\": \"Products\" } ]," + // array
        "\"properties\": { \"id\": { \"type\": \"Edm.Int32\", \"extensions\": [] }, \"price\": { \"type\": \"Edm.Double\", \"extensions\": [] }, \"name\": { \"type\": \"Edm.String\", \"extensions\": [] }, }," + // object
        "\"type\": \"SampleAppModel.Products\"," +
        "\"type_extensions\": []" + // (empty) array
        "}," +
        "\"id\": 2, \"price\": \"235\", \"name\": \"Product ABC\" } }");

    // build metadata
    EdmProperty.Builder productIdProperty = EdmProperty.newBuilder("id").setType(EdmSimpleType.INT32);
    EdmProperty.Builder priceProperty = EdmProperty.newBuilder("price").setType(EdmSimpleType.DOUBLE);
    EdmProperty.Builder productNameProperty = EdmProperty.newBuilder("name").setType(EdmSimpleType.STRING);
    EdmEntityType.Builder productsEntityType = new EdmEntityType.Builder().setNamespace("SampleAppModel").setName("Products").addKeys("id").addProperties(productIdProperty, priceProperty, productNameProperty);
    EdmEntitySet.Builder productsEntitySet = new EdmEntitySet.Builder().setName("Products").setEntityType(productsEntityType);
    EdmEntityContainer.Builder container = new EdmEntityContainer.Builder().addEntitySets(productsEntitySet);
    EdmSchema.Builder schema = new EdmSchema.Builder().addEntityContainers(container).addEntityTypes(productsEntityType);
    EdmDataServices metadata = new EdmDataServices.Builder().addSchemas(schema).build();

    FormatParser<Feed> formatParser = FormatParserFactory.getParser(Feed.class, FormatType.JSON, new Settings(ODataVersion.V1, metadata, "Products", null, null));
    assertThat(formatParser.parse(json), notNullValue());
  }
}
