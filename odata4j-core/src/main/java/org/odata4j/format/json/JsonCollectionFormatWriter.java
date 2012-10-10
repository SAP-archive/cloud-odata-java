package org.odata4j.format.json;

import javax.ws.rs.core.UriInfo;

import org.odata4j.core.OCollection;
import org.odata4j.core.OEntity;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmType;
import org.odata4j.producer.CollectionResponse;

/**
 * Writer for OCollections in JSON
 */
public class JsonCollectionFormatWriter extends JsonFormatWriter<CollectionResponse<?>> {

  public JsonCollectionFormatWriter(String jsonpCallback) {
    super(jsonpCallback);
  }

  @Override
  protected void writeContent(UriInfo uriInfo, JsonWriter jw, CollectionResponse<?> target) {
    OCollection<?> c = target.getCollection();
    EdmType ctype = c.getType();
    jw.startArray();
    {
      boolean isFirst = true;
      for (Object o : c) {
        if (!isFirst) {
          jw.writeSeparator();
        }
        else {
          isFirst = false;
        }
        if (ctype instanceof EdmEntityType) {
          OEntity entity = (OEntity) o;
          super.writeOEntity(uriInfo, jw, entity, entity.getEntitySet(), true); // its a response.
        } else {
          super.writeValue(jw, ctype, o);
        }
      }
    }
    jw.endArray();
  }

}
