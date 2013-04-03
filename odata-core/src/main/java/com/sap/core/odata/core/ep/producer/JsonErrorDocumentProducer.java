package com.sap.core.odata.core.ep.producer;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * @author SAP AG
 */
public class JsonErrorDocumentProducer {

  public void writeErrorDocument(Writer writer, final String errorCode, final String message, final Locale locale, final String innerError) throws IOException {
    JsonStreamWriter.beginObject(writer);
    JsonStreamWriter.name(writer, FormatJson.ERROR);
    JsonStreamWriter.beginObject(writer);
    JsonStreamWriter.namedStringValue(writer, FormatJson.CODE, errorCode);
    JsonStreamWriter.separator(writer);
    JsonStreamWriter.name(writer, FormatJson.MESSAGE);
    JsonStreamWriter.beginObject(writer);
    JsonStreamWriter.namedStringValue(writer, FormatJson.LANG,
        locale == null || locale.getLanguage() == null ? null :
            locale.getLanguage() + (locale.getCountry() == null || locale.getCountry().isEmpty() ? "" : ("-" + locale.getCountry())));
    JsonStreamWriter.separator(writer);
    JsonStreamWriter.namedStringValue(writer, FormatJson.VALUE, message);
    JsonStreamWriter.endObject(writer);
    if (innerError != null) {
      JsonStreamWriter.separator(writer);
      JsonStreamWriter.namedStringValue(writer, FormatJson.INNER_ERROR, innerError);
    }
    JsonStreamWriter.endObject(writer);
    JsonStreamWriter.endObject(writer);
  }
}
