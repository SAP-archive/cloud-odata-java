package org.odata4j.format.json;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.odata4j.core.OEntity;
import org.odata4j.producer.EntitiesResponse;

/**
 *  Write an RSS Feed in JSON format
 */
public class JsonFeedFormatWriter extends JsonFormatWriter<EntitiesResponse> {

  public JsonFeedFormatWriter(String jsonpCallback) {
    super(jsonpCallback);
  }

  @Override
  public void writeContent(UriInfo uriInfo, JsonWriter jw, EntitiesResponse target) {

    jw.startObject();
    {
      jw.writeName("results");

      jw.startArray();
      {
        boolean isFirst = true;
        for (OEntity oe : target.getEntities()) {

          if (isFirst) {
            isFirst = false;
          } else {
            jw.writeSeparator();
          }

          writeOEntity(uriInfo, jw, oe, target.getEntitySet(), true);
        }

      }
      jw.endArray();

      if (target.getInlineCount() != null) {
        jw.writeSeparator();
        jw.writeName("__count");
        jw.writeString(target.getInlineCount().toString());
      }

      if (target.getSkipToken() != null) {

        // $skip only applies to the first page of results.
        // if $top was given, we have to reduce it by the number of entities
        // we are returning now.
        String tops = uriInfo.getQueryParameters().getFirst("$top");
        int top = -1;
        if (tops != null) {
          // query param value already validated
          top = Integer.parseInt(tops);
          top -= target.getEntities().size();
        }
        UriBuilder uri = uriInfo.getRequestUriBuilder();
        if (top > 0) {
          uri.replaceQueryParam("$top", top);
        } else {
          uri.replaceQueryParam("$top");
        }
        String nextHref = uri
            .replaceQueryParam("$skiptoken", target.getSkipToken())
            .replaceQueryParam("$skip").build().toString();

        jw.writeSeparator();
        jw.writeName("__next");
        jw.writeString(nextHref);
      }
    }
    jw.endObject();
  }
}

/*

// entities v2
{
"d" : {
"results": [
{
"__metadata": {
"uri": "http://services.odata.org/Website/odata.svc/ODataConsumers(1)", "type": "ODataServices.ODataConsumer"
}, "Id": 1, "Name": "Browsers", "Description": "Most modern browsers allow you to browse Atom based feeds. Simply point your browser at one of the OData Producers.", "ApplicationUrl": ""
}, {
"__metadata": {
"uri": "http://services.odata.org/Website/odata.svc/ODataConsumers(6)", "type": "ODataServices.ODataConsumer"
}, "Id": 6, "Name": "Sesame - OData Browser", "Description": "A preview version of Fabrice Marguerie\'s OData Browser.", "ApplicationUrl": "http://metasapiens.com/sesame/data-browser"
}
], "__count": "3", "__next": "http://odata.netflix.com/Catalog/Titles/?$filter=substringof('matrix',Name)&$skiptoken='IHKWS'"
}
}

// entities v1
{
"d" : [
{
"__metadata": {
"uri": "http://services.odata.org/Website/odata.svc/ODataConsumers(1)", "type": "ODataServices.ODataConsumer"
}, "Id": 1, "Name": "Browsers", "Description": "Most modern browsers allow you to browse Atom based feeds. Simply point your browser at one of the OData Producers.", "ApplicationUrl": ""
}, {
"__metadata": {
"uri": "http://services.odata.org/Website/odata.svc/ODataConsumers(6)", "type": "ODataServices.ODataConsumer"
}, "Id": 6, "Name": "Sesame - OData Browser", "Description": "A preview version of Fabrice Marguerie\'s OData Browser.", "ApplicationUrl": "http://metasapiens.com/sesame/data-browser"
}
]
}
 */
