package org.odata4j.test.unit.issues;

import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.Assert;

import org.junit.Test;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntityContainer;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmSchema;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.format.xml.AtomFeedFormatParser;
import org.odata4j.format.xml.AtomFeedFormatParser.AtomFeed;

// http://code.google.com/p/odata4j/issues/detail?id=142
public class Issue142Test {

  @Test
  public void issue142() {
    InputStream xml = getClass().getResourceAsStream("/META-INF/no_content_type.xml");
    EdmDataServices metadata = getMetadata();
    AtomFeed feed = new AtomFeedFormatParser(metadata, "WorkflowTaskCollection", null, null).parse(new InputStreamReader(xml));
    Assert.assertNotNull(feed);
  }

  private static EdmDataServices getMetadata() {
    EdmDataServices.Builder metadata = new EdmDataServices.Builder();
    EdmSchema.Builder schema = new EdmSchema.Builder();
    EdmEntityContainer.Builder container = new EdmEntityContainer.Builder();
    EdmEntityType.Builder entityType = new EdmEntityType.Builder().addKeys("workitem_id").setNamespace("WFSERVICE").setName("WorkflowTask").addProperties(
        EdmProperty.newBuilder("workitem_id").setType(EdmSimpleType.STRING),
        EdmProperty.newBuilder("subject").setType(EdmSimpleType.STRING),
        EdmProperty.newBuilder("created_at").setType(EdmSimpleType.DATETIME));
    EdmEntitySet.Builder entitySet = new EdmEntitySet.Builder().setName("WorkflowTaskCollection").setEntityType(entityType);
    container.addEntitySets(entitySet);
    schema.addEntityContainers(container);
    schema.addEntityTypes(entityType);
    metadata.addSchemas(schema);
    return metadata.build();
  }

}
