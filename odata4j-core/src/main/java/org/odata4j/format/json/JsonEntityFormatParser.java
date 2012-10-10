package org.odata4j.format.json;

import java.io.Reader;

import org.odata4j.core.OEntity;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.format.FormatParser;
import org.odata4j.format.Settings;

/**
 * A FormatParser that can be used for parsing the result of functions that
 * return an OEntity
 * 
 * Design Note:
 * 
 * This class does basically the same thing as JsonEntryFormatParser with two exceptions:
 * - it returns an object that derives from OObject because that is what functions return.
 * - it resolves the EdmEntitySet by looking up the function by name in the metadata.
 * 
 * I'm not sure if this indicates something not quite right with the type system + parser factory
 * or if I'm just not seeing something....talk amongst yourselves... :)
 * 
 */
public class JsonEntityFormatParser extends JsonFormatParser implements FormatParser<OEntity> {

  public JsonEntityFormatParser(Settings settings) {
    super(settings);
  }

  @Override
  public OEntity parse(Reader reader) {
    JsonStreamReaderFactory.JsonStreamReader jsr = JsonStreamReaderFactory.createJsonStreamReader(reader);
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
      EdmFunctionImport fi = metadata.findEdmFunctionImport(entitySetName);

      return parseEntry(fi.getEntitySet(), jsr).getEntity();

      // no interest in the closing events
    } finally {
      jsr.close();
    }
  }

}
