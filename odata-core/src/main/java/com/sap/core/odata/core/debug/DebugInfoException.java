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
package com.sap.core.odata.core.debug;

import java.io.IOException;
import java.util.Locale;

import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;
import com.sap.core.odata.core.exception.MessageService;

/**
 * @author SAP AG
 */
public class DebugInfoException implements DebugInfo {

  private final Exception exception;
  private final Locale locale;

  public DebugInfoException(final Exception exception, final Locale locale) {
    this.exception = exception;
    this.locale = locale;
  }

  @Override
  public String getName() {
    return "Stacktrace";
  }

  @Override
  public void appendJson(final JsonStreamWriter jsonStreamWriter) throws IOException {
    jsonStreamWriter.beginObject()
        .name("exceptions")
        .beginArray();
    Throwable throwable = exception;
    while (throwable != null) {
      jsonStreamWriter.beginObject()
          .namedStringValueRaw("class", throwable.getClass().getCanonicalName()).separator()
          .namedStringValue("message",
              throwable instanceof ODataMessageException ?
                  MessageService.getMessage(locale, ((ODataMessageException) throwable).getMessageReference()).getText() :
                  throwable.getLocalizedMessage())
          .separator();

      jsonStreamWriter.name("invocation");
      appendJsonStackTraceElement(jsonStreamWriter, throwable.getStackTrace()[0]);

      jsonStreamWriter.endObject();
      throwable = throwable.getCause();
      if (throwable != null) {
        jsonStreamWriter.separator();
      }
    }
    jsonStreamWriter.endArray();
    jsonStreamWriter.separator();

    jsonStreamWriter.name("stacktrace")
        .beginArray();
    boolean first = true;
    for (final StackTraceElement stackTraceElement : exception.getStackTrace()) {
      if (!first) {
        jsonStreamWriter.separator();
      }
      first = false;
      appendJsonStackTraceElement(jsonStreamWriter, stackTraceElement);
    }
    jsonStreamWriter.endArray();
    jsonStreamWriter.endObject();
  }

  private static void appendJsonStackTraceElement(final JsonStreamWriter jsonStreamWriter, final StackTraceElement stackTraceElement) throws IOException {
    jsonStreamWriter.beginObject()
        .namedStringValueRaw("class", stackTraceElement.getClassName()).separator()
        .namedStringValueRaw("method", stackTraceElement.getMethodName()).separator()
        .name("line").unquotedValue(Integer.toString(stackTraceElement.getLineNumber()))
        .endObject();
  }
}
