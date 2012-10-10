package org.odata4j.test.unit.issues;

import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.Assert;

import org.junit.Test;
import org.odata4j.core.ODataVersion;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.format.Feed;
import org.odata4j.format.Settings;
import org.odata4j.format.json.JsonFeedFormatParser;
import org.odata4j.format.xml.EdmxFormatParser;
import org.odata4j.stax2.XMLEventReader2;
import org.odata4j.stax2.XMLFactoryProvider2;

public class Issue98Test {
  @Test
  public void issue98() {
    InputStream metadataStream = getClass().getResourceAsStream("/META-INF/issue98_metadata.xml");
    XMLEventReader2 reader = XMLFactoryProvider2.getInstance().newXMLInputFactory2().createXMLEventReader(new InputStreamReader(metadataStream));
    EdmDataServices metadata = new EdmxFormatParser().parseMetadata(reader);

    InputStream titlesStream = getClass().getResourceAsStream("/META-INF/issue98_payload.json");
    Settings settings = new Settings(ODataVersion.V1, metadata, "People", null, null);
    Feed feed = new JsonFeedFormatParser(settings).parse(new InputStreamReader(titlesStream));
    Assert.assertNotNull(feed);
  }

}
