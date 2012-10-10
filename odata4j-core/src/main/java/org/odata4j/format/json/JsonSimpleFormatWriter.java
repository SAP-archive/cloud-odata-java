package org.odata4j.format.json;

import javax.ws.rs.core.UriInfo;

import org.odata4j.producer.SimpleResponse;

/**
 * write a single value that has an EdmSimpleType type
 */
public class JsonSimpleFormatWriter extends JsonFormatWriter<SimpleResponse> {

  public JsonSimpleFormatWriter(String jsonpCallback) {
    super(jsonpCallback);
  }

  @Override
  protected void writeContent(UriInfo uriInfo, JsonWriter jw, SimpleResponse target) {
    jw.startObject();
    jw.writeName(target.getName());
    this.writeValue(jw, target.getType(), target.getValue());
    jw.endObject();
  }
}
