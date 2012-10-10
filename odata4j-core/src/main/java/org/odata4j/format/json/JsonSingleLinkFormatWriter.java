package org.odata4j.format.json;

import javax.ws.rs.core.UriInfo;

import org.odata4j.format.SingleLink;

public class JsonSingleLinkFormatWriter extends JsonFormatWriter<SingleLink> {

  public JsonSingleLinkFormatWriter(String jsonpCallback) {
    super(jsonpCallback);
  }

  @Override
  protected void writeContent(UriInfo uriInfo, JsonWriter jw, SingleLink link) {
    writeUri(jw, link);
  }

  static void writeUri(JsonWriter jw, SingleLink link) {
    jw.startObject();
    {
      jw.writeName("uri");
      jw.writeString(link.getUri());
    }
    jw.endObject();
  }

}

/*
{
"d" : {
"uri": "http://services.odata.org/northwind/Northwind.svc/Categories(1)"
}
}
*/
