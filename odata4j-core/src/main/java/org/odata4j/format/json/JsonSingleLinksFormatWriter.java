package org.odata4j.format.json;

import javax.ws.rs.core.UriInfo;

import org.odata4j.format.SingleLink;
import org.odata4j.format.SingleLinks;

public class JsonSingleLinksFormatWriter extends JsonFormatWriter<SingleLinks> {

  public JsonSingleLinksFormatWriter(String jsonpCallback) {
    super(jsonpCallback);
  }

  @Override
  protected void writeContent(UriInfo uriInfo, JsonWriter jw, SingleLinks links) {
    jw.startObject();
    {
      jw.writeName("results");
      jw.startArray();
      boolean isFirst = true;
      for (SingleLink link : links) {
        if (!isFirst)
          jw.writeSeparator();
        else
          isFirst = false;
        JsonSingleLinkFormatWriter.writeUri(jw, link);
      }
      jw.endArray();
    }
    jw.endObject();
  }

}

/*
{
"d" : {
"results": [
{
"uri": "http://services.odata.org/northwind/Northwind.svc/Order_Details(OrderID=10285,ProductID=1)"
}, {
"uri": "http://services.odata.org/northwind/Northwind.svc/Order_Details(OrderID=10294,ProductID=1)"
}
]
}
}
*/
