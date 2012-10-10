package org.odata4j.format.json;

import javax.ws.rs.core.UriInfo;

import org.odata4j.producer.PropertyResponse;

public class JsonPropertyFormatWriter extends JsonFormatWriter<PropertyResponse> {

  public JsonPropertyFormatWriter(String jsonpCallback) {
    super(jsonpCallback);
  }

  @Override
  protected void writeContent(UriInfo uriInfo, JsonWriter jw, PropertyResponse target) {
    jw.startObject();
    {
      writeProperty(jw, target.getProperty());
    }
    jw.endObject();
  }

}

/*
// property
{
"d" : {
"CategoryName": "Beverages"
}
}
*/
