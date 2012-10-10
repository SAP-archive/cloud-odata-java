package org.odata4j.format.json;

import java.io.Reader;

import org.odata4j.core.OError;
import org.odata4j.core.OErrors;
import org.odata4j.format.FormatParser;
import org.odata4j.format.Settings;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader.JsonEvent;

public class JsonErrorFormatParser extends JsonFormatParser implements FormatParser<OError> {

  public JsonErrorFormatParser(Settings settings) {
    super(settings);
  }

  @Override
  public OError parse(Reader reader) {
    String code = null;
    String message = null;
    String innerError = null;
    JsonStreamReader jsr = JsonStreamReaderFactory.createJsonStreamReader(reader);
    JsonEvent event = jsr.nextEvent();
    try {
      ensureStartObject(event);
      ensureStartProperty(jsr.nextEvent(), "error");
      ensureStartObject(jsr.nextEvent());

      while (jsr.hasNext() && !event.isEndObject()) {
        event = jsr.nextEvent();
        ensureNext(jsr);

        if (event.isStartProperty()
            && "code".equals(event.asStartProperty().getName())) {
          ensureEndProperty(event = jsr.nextEvent());
          code = event.asEndProperty().getValue();
        } else if (event.isStartProperty()
            && "message".equals(event.asStartProperty().getName())) {
          ensureStartObject(jsr.nextEvent());
          while (jsr.hasNext() && !event.isEndObject()) {
            event = jsr.nextEvent();
            ensureNext(jsr);
            if (event.isStartProperty()
                && "lang".equals(event.asStartProperty().getName())) {
              ensureEndProperty(event = jsr.nextEvent());
            } else if (event.isStartProperty()
                && "value".equals(event.asStartProperty().getName())) {
              ensureEndProperty(event = jsr.nextEvent());
              message = event.asEndProperty().getValue();
            } else {
              ensureEndObject(event);
            }
          }
          ensureEndProperty(event = jsr.nextEvent());
        } else if (event.isStartProperty()
            && "innererror".equals(event.asStartProperty().getName())) {
          ensureEndProperty(event = jsr.nextEvent());
          innerError = event.asEndProperty().getValue();
        } else {
          ensureEndObject(event);
        }
      }
      ensureEndProperty(jsr.nextEvent());
      ensureEndObject(jsr.nextEvent());
    } finally {
      jsr.close();
    }
    return OErrors.error(code, message, innerError);
  }
}
