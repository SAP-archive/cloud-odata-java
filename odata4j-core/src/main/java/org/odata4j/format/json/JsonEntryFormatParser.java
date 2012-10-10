package org.odata4j.format.json;

import java.io.Reader;

import org.odata4j.format.Entry;
import org.odata4j.format.FormatParser;
import org.odata4j.format.Settings;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader;

public class JsonEntryFormatParser extends JsonFormatParser implements FormatParser<Entry> {

  public JsonEntryFormatParser(Settings settings) {
    super(settings);
  }

  @Override
  public Entry parse(Reader reader) {
    JsonStreamReader jsr = JsonStreamReaderFactory.createJsonStreamReader(reader);
    try {
      ensureNext(jsr);

      // skip the StartObject event
      ensureStartObject(jsr.nextEvent());

      if (isResponse) {
        // "d" property
        ensureNext(jsr);
        ensureStartProperty(jsr.nextEvent(), DATA_PROPERTY);

        // skip the StartObject event
        ensureStartObject(jsr.nextEvent());
      }

      // parse the entry
      return parseEntry(metadata.getEdmEntitySet(entitySetName), jsr);

      // no interest in the closing events
    } finally {
      jsr.close();
    }
  }

}
