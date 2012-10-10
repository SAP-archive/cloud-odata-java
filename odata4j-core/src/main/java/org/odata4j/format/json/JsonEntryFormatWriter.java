package org.odata4j.format.json;

import javax.ws.rs.core.UriInfo;

import org.odata4j.producer.EntityResponse;

public class JsonEntryFormatWriter extends JsonFormatWriter<EntityResponse> {

  public JsonEntryFormatWriter(String jsonpCallback) {
    super(jsonpCallback);
  }

  @Override
  protected void writeContent(UriInfo uriInfo, JsonWriter jw, EntityResponse target) {
    writeOEntity(uriInfo, jw, target.getEntity(), target.getEntity().getEntitySet(), true);
  }

}
