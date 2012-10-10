package org.odata4j.format.json;

import java.io.Reader;

import org.odata4j.core.OSimpleObject;
import org.odata4j.core.OSimpleObjects;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.format.FormatParser;
import org.odata4j.format.Settings;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader.JsonEvent;

/**
 * parses a response from a service operation that returns EdmSimpleType
 */
public class JsonSimpleObjectFormatParser extends JsonFormatParser implements FormatParser<OSimpleObject<?>> {

  public JsonSimpleObjectFormatParser(Settings settings) {
    super(settings);
  }

  @Override
  public OSimpleObject<?> parse(Reader reader) {

    JsonStreamReaderFactory.JsonStreamReader jsr = JsonStreamReaderFactory.createJsonStreamReader(reader);
    JsonEvent endProp;

    // {
    ensureNext(jsr);
    ensureStartObject(jsr.nextEvent()); // the response object
    {
      // "d" : 
      ensureNext(jsr);
      ensureStartProperty(jsr.nextEvent(), DATA_PROPERTY);

      // { propname : <simple type> }

      ensureStartObject(jsr.nextEvent());
      {
        ensureStartProperty(jsr.nextEvent());
        endProp = jsr.nextEvent();
        ensureEndProperty(endProp);
        ensureEndObject(jsr.nextEvent());
      }

      // "d"
      ensureEndProperty(jsr.nextEvent());

      // }
      ensureEndObject(jsr.nextEvent());
    }

    return OSimpleObjects.parse((EdmSimpleType<?>) this.parseType, endProp.asEndProperty().getValue());
  }

}
