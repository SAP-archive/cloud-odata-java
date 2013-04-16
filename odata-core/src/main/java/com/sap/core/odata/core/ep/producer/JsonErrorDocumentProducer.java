/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

  public void writeErrorDocument(final Writer writer, final String errorCode, final String message, final Locale locale, final String innerError) throws IOException {
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);

    jsonStreamWriter.beginObject();
    jsonStreamWriter.name(FormatJson.ERROR);
    jsonStreamWriter.beginObject();
    jsonStreamWriter.namedStringValue(FormatJson.CODE, errorCode);
    jsonStreamWriter.separator();
    jsonStreamWriter.name(FormatJson.MESSAGE);
    jsonStreamWriter.beginObject();
    jsonStreamWriter.namedStringValueRaw(FormatJson.LANG,
        locale == null || locale.getLanguage() == null ? null :
            locale.getLanguage() + (locale.getCountry() == null || locale.getCountry().isEmpty() ? "" : ("-" + locale.getCountry())));
    jsonStreamWriter.separator();
    jsonStreamWriter.namedStringValue(FormatJson.VALUE, message);
    jsonStreamWriter.endObject();
    if (innerError != null) {
      jsonStreamWriter.separator();
      jsonStreamWriter.namedStringValue(FormatJson.INNER_ERROR, innerError);
    }
    jsonStreamWriter.endObject();
    jsonStreamWriter.endObject();
  }
}
