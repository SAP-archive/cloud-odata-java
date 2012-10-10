package org.odata4j.test.unit.format.json;

import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityIds;
import org.odata4j.format.FormatType;
import org.odata4j.format.FormatWriter;
import org.odata4j.format.FormatWriterFactory;
import org.odata4j.format.SingleLinks;

public class JsonSingleLinksFormatWriterTest {

  protected static final String SERVICE_ROOT = "http://localhost/";

  protected static FormatWriter<SingleLinks> formatWriter;
  protected static List<OEntityId> entityIds;
  protected StringWriter stringWriter;

  @BeforeClass
  public static void setupClass() throws Exception {
    formatWriter = FormatWriterFactory.getFormatWriter(SingleLinks.class, null, FormatType.JSON.toString(), null);
    entityIds = new ArrayList<OEntityId>();
    entityIds.add(OEntityIds.create("test", "a"));
    entityIds.add(OEntityIds.create("test", "b"));
    entityIds.add(OEntityIds.create("test", "c"));
  }

  @Before
  public void setup() throws Exception {
    stringWriter = new StringWriter();
  }

  @Test
  public void noLink() throws Exception {
    formatWriter.write(null, stringWriter, SingleLinks.create(SERVICE_ROOT, entityIds.subList(0, 0)));
    assertTrue(Pattern.compile(".+\\[\\s*\\].+", Pattern.DOTALL)
        .matcher(stringWriter.toString()).matches());
  }

  @Test
  public void oneLink() throws Exception {
    formatWriter.write(null, stringWriter, SingleLinks.create(SERVICE_ROOT, entityIds.subList(0, 1)));
    assertTrue(Pattern.compile(".+\\[\\s*\\{\\s*\"uri\"\\s*:\\s*\"" + SERVICE_ROOT + "test\\('a'\\)\"\\s*\\}\\s*\\].+", Pattern.DOTALL)
        .matcher(stringWriter.toString()).matches());
  }

  @Test
  public void twoLinks() throws Exception {
    formatWriter.write(null, stringWriter, SingleLinks.create(SERVICE_ROOT, entityIds.subList(0, 2)));
    assertTrue(Pattern.compile(".+\\[\\s*"
        + "\\{\\s*\"uri\"\\s*:\\s*\"" + SERVICE_ROOT + "test\\('a'\\)\"\\s*\\}\\s*,\\s*"
        + "\\{\\s*\"uri\"\\s*:\\s*\"" + SERVICE_ROOT + "test\\('b'\\)\"\\s*\\}\\s*"
        + "\\].+", Pattern.DOTALL)
        .matcher(stringWriter.toString()).matches());
  }

  @Test
  public void threeLinks() throws Exception {
    formatWriter.write(null, stringWriter, SingleLinks.create(SERVICE_ROOT, entityIds.subList(0, 3)));
    assertTrue(Pattern.compile(".+\\[\\s*"
        + "\\{\\s*\"uri\"\\s*:\\s*\"" + SERVICE_ROOT + "test\\('a'\\)\"\\s*\\}\\s*,\\s*"
        + "\\{\\s*\"uri\"\\s*:\\s*\"" + SERVICE_ROOT + "test\\('b'\\)\"\\s*\\}\\s*,\\s*"
        + "\\{\\s*\"uri\"\\s*:\\s*\"" + SERVICE_ROOT + "test\\('c'\\)\"\\s*\\}\\s*"
        + "\\].+", Pattern.DOTALL)
        .matcher(stringWriter.toString()).matches());
  }
}