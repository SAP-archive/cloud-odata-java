package org.odata4j.format.json;

import java.io.Writer;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.odata4j.core.Guid;
import org.odata4j.core.OCollection;
import org.odata4j.core.OComplexObject;
import org.odata4j.core.ODataConstants;
import org.odata4j.core.OEntity;
import org.odata4j.core.OLink;
import org.odata4j.core.OObject;
import org.odata4j.core.OProperty;
import org.odata4j.core.ORelatedEntitiesLinkInline;
import org.odata4j.core.ORelatedEntityLinkInline;
import org.odata4j.core.OSimpleObject;
import org.odata4j.core.UnsignedByte;
import org.odata4j.edm.EdmCollectionType;
import org.odata4j.edm.EdmComplexType;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.edm.EdmType;
import org.odata4j.format.FormatWriter;
import org.odata4j.internal.InternalUtil;
import org.odata4j.repack.org.apache.commons.codec.binary.Base64;

/**
 * Write content to an output stream in JSON format.
 *
 * <p>This class is abstract because it delegates the strategy pattern of writing
 * actual content elements to its (various) subclasses.
 *
 * <p>Each element in the array to be written can be wrapped in a function call
 * on the JavaScript side by specifying the name of a function to call to the
 * constructor.
 *
 * @param <T> the type of the content elements to be written to the stream.
 */
public abstract class JsonFormatWriter<T> implements FormatWriter<T> {

  private final String jsonpCallback;

  /**
   * Creates a new JSON writer.
   *
   * @param jsonpCallback a function to call on the javascript side to act
   * on the data provided in the content.
   */
  public JsonFormatWriter(String jsonpCallback) {
    this.jsonpCallback = jsonpCallback;
  }

  /**
   * A strategy method to actually write content objects
   *
   * @param uriInfo the base URI that indicates where in the schema we are
   * @param jw the JSON writer object
   * @param target the content value to be written
   */
  abstract protected void writeContent(UriInfo uriInfo, JsonWriter jw, T target);

  @Override
  public String getContentType() {
    return jsonpCallback == null
        ? ODataConstants.APPLICATION_JAVASCRIPT_CHARSET_UTF8
        : ODataConstants.TEXT_JAVASCRIPT_CHARSET_UTF8;
  }

  protected String getJsonpCallback() {
    return jsonpCallback;
  }

  @Override
  public void write(UriInfo uriInfo, Writer w, T target) {

    JsonWriter jw = new JsonWriter(w);
    if (getJsonpCallback() != null) {
      jw.startCallback(getJsonpCallback());
    }

    jw.startObject();
    {
      jw.writeName("d");
      writeContent(uriInfo, jw, target);
    }
    jw.endObject();

    if (getJsonpCallback() != null) {
      jw.endCallback();
    }

  }

  protected void writeProperty(JsonWriter jw, OProperty<?> prop) {
    jw.writeName(prop.getName());
    writeValue(jw, prop.getType(), prop.getValue());
  }

  @SuppressWarnings("unchecked")
  protected void writeValue(JsonWriter jw, EdmType type, Object pvalue) {
    if (pvalue == null) {
      jw.writeNull();
    } else if (type.equals(EdmSimpleType.BINARY)) {
      jw.writeString(Base64.encodeBase64String((byte[]) pvalue));
    } else if (type.equals(EdmSimpleType.BOOLEAN)) {
      jw.writeBoolean((Boolean) pvalue);
    } else if (type.equals(EdmSimpleType.BYTE)) {
      jw.writeNumber(((UnsignedByte) pvalue).intValue());
    } else if (type.equals(EdmSimpleType.SBYTE)) {
      jw.writeNumber((Byte) pvalue);
    } else if (type.equals(EdmSimpleType.DATETIME)) {
      jw.writeRaw(InternalUtil.formatDateTimeForJson((LocalDateTime) pvalue));
    } else if (type.equals(EdmSimpleType.DECIMAL)) {
      jw.writeString(((BigDecimal) pvalue).toPlainString());
    } else if (type.equals(EdmSimpleType.DOUBLE)) {
      jw.writeString(pvalue.toString());
    } else if (type.equals(EdmSimpleType.GUID)) {
      jw.writeString(((Guid) pvalue).toString());
    } else if (type.equals(EdmSimpleType.INT16)) {
      jw.writeNumber((Short) pvalue);
    } else if (type.equals(EdmSimpleType.INT32)) {
      jw.writeNumber((Integer) pvalue);
    } else if (type.equals(EdmSimpleType.INT64)) {
      jw.writeString(pvalue.toString());
    } else if (type.equals(EdmSimpleType.SINGLE)) {
      jw.writeNumber((Float) pvalue);
    } else if (type.equals(EdmSimpleType.TIME)) {
      jw.writeRaw(InternalUtil.formatTimeForJson((LocalTime) pvalue));
    } else if (type.equals(EdmSimpleType.DATETIMEOFFSET)) {
      jw.writeRaw(InternalUtil.formatDateTimeOffsetForJson((DateTime) pvalue));
    } else if (type instanceof EdmComplexType || (type instanceof EdmSimpleType && (!((EdmSimpleType<?>) type).isSimple()))) {
      // the OComplexObject value type is not in use everywhere yet, fix TODO
      if (pvalue instanceof OComplexObject) {
        pvalue = ((OComplexObject) pvalue).getProperties();
      }
      writeComplexObject(jw, null, type.getFullyQualifiedTypeName(), (List<OProperty<?>>) pvalue);
    } else if (type instanceof EdmCollectionType) {
      writeCollection(jw, (EdmCollectionType) type, (OCollection<? extends OObject>) pvalue);
    } else {
      String value = pvalue.toString();
      jw.writeString(value);
    }
  }

  @SuppressWarnings("rawtypes")
  protected void writeCollection(JsonWriter jw, EdmCollectionType type, OCollection<? extends OObject> coll) {
    jw.startObject();
    {
      jw.writeName("results");

      jw.startArray();
      {
        boolean isFirst = true;
        Iterator<? extends OObject> iter = coll.iterator();
        while (iter.hasNext()) {
          OObject obj = iter.next();
          if (isFirst) {
            isFirst = false;
          } else {
            jw.writeSeparator();
          }
          if (obj instanceof OComplexObject) {
            writeComplexObject(jw, null, obj.getType().getFullyQualifiedTypeName(), ((OComplexObject) obj).getProperties());
          } else if (obj instanceof OSimpleObject) {
            writeValue(jw, obj.getType(), ((OSimpleObject) obj).getValue());
          }
          //else if (obj instanceof OEntity) {
          //  I think the FormatWriter sig is going to have to change:
          //  2.  why does JSON write absolute uris (http://blah/blah) for every entity?  The Atom
          //      equivalent parts have the relative uri in many places.  Hmmh, a JSON feed representation
          //      doesn't carry the xml:base uri like in Atom...weird...protocol seems inconsistent.
          //  this.writeOEntity(null, jw, null, null, isFirst);
          //}
          // others for later: ORowType
        }

      }
      jw.endArray();
    }
    jw.endObject();
  }

  protected void writeComplexObject(JsonWriter jw, String complexObjectName, String fullyQualifiedTypeName, List<OProperty<?>> props) {
    jw.startObject();
    {
      /* Confused:  The live OData producers that have complex types (ebay, netflix)
       * both write this __metadata object for each complex object.  I can't find
       * this in the OData spec though...
      jw.writeName("__metadata");
      jw.startObject();
      {
      jw.writeName("type");
      jw.writeString(fullyQualifiedTypeName);
      }
      jw.endObject();
      jw.writeSeparator();
       */
      if (complexObjectName != null) {
        jw.writeName(complexObjectName);
        jw.startObject();
      }
      writeOProperties(jw, props);
      if (complexObjectName != null) {
        jw.endObject();
      }
    }
    jw.endObject();
  }

  protected void writeOEntity(UriInfo uriInfo, JsonWriter jw, OEntity oe, EdmEntitySet ees, boolean isResponse) {

    jw.startObject();
    {
      String baseUri = null;

      // TODO: I'm keeping this pattern of writing the __metadata if we have a non-null type..it seems like we could still
      //       write the uri even if we don't have a type.  Also, are there any scenarios where the entity type would be null?  Not sure.
      if (isResponse && oe.getEntityType() != null) {
        baseUri = uriInfo.getBaseUri().toString();

        jw.writeName("__metadata");
        jw.startObject();
        {
          String absId = baseUri + InternalUtil.getEntityRelId(oe);
          jw.writeName("uri");
          jw.writeString(absId);
          jw.writeSeparator();
          jw.writeName("type");
          jw.writeString(oe.getEntityType().getFullyQualifiedTypeName());
        }
        jw.endObject();
        jw.writeSeparator();
      }

      writeOProperties(jw, oe.getProperties());
      writeLinks(jw, oe, uriInfo, isResponse);
    }
    jw.endObject();
  }

  protected void writeLinks(JsonWriter jw, OEntity oe, UriInfo uriInfo, boolean isResponse) {

    if (oe.getLinks() != null) {
      for (OLink link : oe.getLinks()) {
        if (isResponse) {
          writeResponseLink(jw, link, oe, uriInfo);
        } else {
          writeRequestLink(jw, link, oe, uriInfo);
        }
      }
    }
  }

  protected void writeResponseLink(JsonWriter jw, OLink link, OEntity oe, UriInfo uriInfo) {
    jw.writeSeparator();
    jw.writeName(link.getTitle());
    if (link.isInline()) {
      if (link.isCollection()) {

        // the version check will only make sense when this library properly
        // supports version negotiation.  For now we write v2 only
        if (true) { //  || ODataVersion.isVersionGreaterThan(settings.version, ODataVersion.V1)) {
          jw.startObject();
          jw.writeName(JsonFormatParser.RESULTS_PROPERTY);
        }

        jw.startArray();
        {
          if (((ORelatedEntitiesLinkInline) link).getRelatedEntities() != null) {
            boolean isFirstInlinedEntity = true;
            for (OEntity re : ((ORelatedEntitiesLinkInline) link).getRelatedEntities()) {

              if (isFirstInlinedEntity) {
                isFirstInlinedEntity = false;
              } else {
                jw.writeSeparator();
              }

              writeOEntity(uriInfo, jw, re, re.getEntitySet(), true);
            }

          }
        }
        jw.endArray();

        // maybe later
        if (true) { // ODataVersion.isVersionGreaterThan(settings.version, ODataVersion.V1)) {
          jw.endObject();
        }

      } else {
        OEntity re = ((ORelatedEntityLinkInline) link).getRelatedEntity();
        if (re == null) {
          jw.writeNull();
        } else {
          writeOEntity(uriInfo, jw, re, re.getEntitySet(), true);
        }
      }
    } else {
      // deferred
      jw.startObject();
      {
        jw.writeName("__deferred");
        jw.startObject();
        {
          String absId = uriInfo.getBaseUri().toString() + InternalUtil.getEntityRelId(oe);
          jw.writeName("uri");
          jw.writeString(absId + "/" + link.getTitle());
        }
        jw.endObject();
      }
      jw.endObject();
    }
  }

  protected void writeRequestLink(JsonWriter jw, OLink link, OEntity oe, UriInfo uriInfo) {
    jw.writeSeparator();

    jw.writeName(link.getTitle());

    if (link.isInline()) {
      if (link.isCollection()) {

        jw.startArray();
        if (((ORelatedEntitiesLinkInline) link).getRelatedEntities() != null) {
          List<OEntity> relEntities = ((ORelatedEntitiesLinkInline) link).getRelatedEntities();
          for (int i = 0, size = relEntities.size(); i < size; i++) {
            OEntity relEntity = relEntities.get(i);
            writeOEntity(uriInfo, jw, relEntity, relEntity.getEntitySet(), false);
            if (i < size - 1) {
              jw.writeSeparator();
            }
          }
        }
        jw.endArray();
      } else {
        // single entity
        OEntity relEntity = ((ORelatedEntityLinkInline) link).getRelatedEntity();
        if (relEntity == null) {
          jw.writeNull();
        } else {
          writeOEntity(uriInfo, jw, relEntity, relEntity.getEntitySet(), false);
        }
      }
    } else {
      writeInlineLink(jw, link);
    }
  }

  private void writeInlineLink(JsonWriter jw, OLink link) {
    // in requests, this represents a reference to an existing entity
    jw.startObject();
    jw.writeName("__metadata");
    jw.startObject();
    {
      jw.writeName("uri");
      jw.writeString(link.getHref());
    }
    jw.endObject();
    jw.endObject();
  }

  protected void writeOProperties(JsonWriter jw, List<OProperty<?>> properties) {
    boolean isFirst = true;
    for (OProperty<?> prop : properties) {
      if (isFirst) {
        isFirst = false;
      } else {
        jw.writeSeparator();
      }

      writeProperty(jw, prop);
    }
  }
}
