package org.odata4j.format.json;

import java.util.ArrayList;
import java.util.List;

import org.core4j.Enumerable;
import org.core4j.Func1;
import org.odata4j.core.OCollection;
import org.odata4j.core.OComplexObject;
import org.odata4j.core.ODataVersion;
import org.odata4j.core.OEntities;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OLink;
import org.odata4j.core.OLinks;
import org.odata4j.core.OObject;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmCollectionType;
import org.odata4j.edm.EdmComplexType;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmMultiplicity;
import org.odata4j.edm.EdmNavigationProperty;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmProperty.CollectionKind;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.edm.EdmType;
import org.odata4j.format.Entry;
import org.odata4j.format.Feed;
import org.odata4j.format.Settings;
import org.odata4j.format.json.JsonFeedFormatParser.JsonEntry;
import org.odata4j.format.json.JsonFeedFormatParser.JsonFeed;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader.JsonEvent;

public class JsonFormatParser {

  static class JsonEntryMetaData {
    String uri;
    String type;
    String etag;
  }

  static class JsonObjectPropertyValue {
    String uri;
    OEntity entity;
    List<OEntity> entities;
    OCollection<? extends OObject> collection;
    EdmCollectionType collectionType;
    OComplexObject complexObject;
  }

  protected static final String METADATA_PROPERTY = "__metadata";
  protected static final String DEFERRED_PROPERTY = "__deferred";
  protected static final String NEXT_PROPERTY = "__next";
  protected static final String COUNT_PROPERTY = "__count";

  protected static final String URI_PROPERTY = "uri";
  protected static final String TYPE_PROPERTY = "type";
  protected static final String ETAG_PROPERTY = "etag";
  protected static final String RESULTS_PROPERTY = "results";
  protected static final String DATA_PROPERTY = "d";

  protected ODataVersion version;
  protected EdmDataServices metadata;
  protected String entitySetName;
  protected OEntityKey entityKey;
  protected boolean isResponse;
  protected EdmType parseType;

  protected JsonFormatParser(Settings settings) {
    this.version = settings == null ? null : settings.version;
    this.metadata = settings == null ? null : settings.metadata;
    this.entitySetName = settings == null ? null : settings.entitySetName;
    this.entityKey = settings == null ? null : settings.entityKey;
    this.isResponse = settings == null ? false : settings.isResponse;
    this.parseType = settings == null ? null : settings.parseType;
  }

  protected JsonFeed parseFeed(EdmEntitySet ees, JsonStreamReader jsr) {
    JsonFeed feed = new JsonFeed();
    feed.entries = new ArrayList<Entry>();

    while (jsr.hasNext()) {
      JsonEvent event = jsr.nextEvent();

      if (event.isStartObject()) {
        JsonEntry entry = parseEntry(ees, jsr);
        feed.entries.add(entry);
      } else if (event.isEndArray()) {
        break;
      }
    }

    return feed;
  }

  protected void resolveEntityType(JsonEntry entry) {
    // does the metadata refine the type of entity to expect?
    if (entry.jemd != null && entry.jemd.type != null && !entry.getEntitySet().getType().getFullyQualifiedTypeName().equals(entry.jemd.type)) {
      // yes it does.

      entry.setEntityType((EdmEntityType) this.metadata.findEdmEntityType(entry.jemd.type));
      if (entry.getEntityType() == null) {
        throw new IllegalArgumentException("failed resolving type: " + entry.jemd.type);
      }
    } else {
      entry.setEntityType(entry.getEntitySet().getType());
    }
  }

  protected JsonEntry parseEntry(JsonEntryMetaData jemd, EdmEntitySet ees, JsonStreamReader jsr) {
    JsonEntry entry = new JsonEntry(ees, jemd);
    if (jemd != null)
      resolveEntityType(entry);
    entry.properties = new ArrayList<OProperty<?>>();
    entry.links = new ArrayList<OLink>();
    while (jsr.hasNext()) {
      JsonEvent event = jsr.nextEvent();
      if (event.isStartProperty()) {
        addProperty(entry, ees, event.asStartProperty().getName(), jsr);
      } else if (event.isEndObject()) {
        break;
      }
    }
    entry.oentity = toOEntity(ees, entry.getEntityType(), entry.getEntityKey(), entry.getETag(), entry.properties, entry.links);
    return entry;
  }

  protected JsonEntry parseEntry(EdmEntitySet ees, JsonStreamReader jsr) {
    return parseEntry(null, ees, jsr);
  }

  private OEntity toOEntity(EdmEntitySet entitySet, EdmEntityType entityType, OEntityKey key, String entityTag, List<OProperty<?>> properties, List<OLink> links) {

    // key is what we pulled out of the _metadata, use it first.
    if (key != null) {
      return OEntities.create(entitySet, entityType, key, entityTag, properties, links);
    }

    if (entityKey != null) {
      return OEntities.create(entitySet, entityType, entityKey, entityTag, properties, links);
    }

    return OEntities.createRequest(entitySet, properties, links);
  }

  protected JsonEntryMetaData parseMetadata(JsonStreamReader jsr) {
    JsonEntryMetaData jemd = new JsonEntryMetaData();
    ensureStartObject(jsr.nextEvent());

    while (jsr.hasNext()) {
      JsonEvent event = jsr.nextEvent();
      ensureNext(jsr);

      if (event.isStartProperty()
          && URI_PROPERTY.equals(event.asStartProperty().getName())) {
        ensureEndProperty(event = jsr.nextEvent());
        jemd.uri = event.asEndProperty().getValue();
      } else if (event.isStartProperty()
          && TYPE_PROPERTY.equals(event.asStartProperty().getName())) {
        ensureEndProperty(event = jsr.nextEvent());
        jemd.type = event.asEndProperty().getValue();
      } else if (event.isStartProperty()
          && ETAG_PROPERTY.equals(event.asStartProperty().getName())) {
        ensureEndProperty(event = jsr.nextEvent());
        jemd.etag = event.asEndProperty().getValue();
      } else if (event.isStartProperty() || event.isStartObject() || event.isStartArray()) {
        // ignore unsupported metadata, i.e. everything besides uri, type and etag
        jsr.skipNestedEvents();
      } else if (event.isEndObject()) {
        break;
      }
    }
    // eat the EndProperty event
    ensureEndProperty(jsr.nextEvent());

    return jemd;
  }

  /**
   * adds the property. This property can be a navigation property too. In this
   * case a link will be added. If it's the meta data the information will be
   * added to the entry too.
   */
  protected EdmEntitySet addProperty(JsonEntry entry, EdmEntitySet ees, String name, JsonStreamReader jsr) {

    if (METADATA_PROPERTY.equals(name)) {
      JsonEntryMetaData jemd = parseMetadata(jsr);
      entry.jemd = jemd;
      JsonEvent event = jsr.nextEvent();
      ensureStartProperty(event);
      name = event.asStartProperty().getName();
      this.resolveEntityType(entry);
    }

    JsonEvent event = jsr.nextEvent();

    if (event.isEndProperty()) {
      // scalar property
      EdmProperty ep = entry.getEntityType().findProperty(name);
      if (ep == null) {
        // a navigation property with multiplicity 1 and a null associated element looks
        // like a scalar property here with a null value
        if (event.asEndProperty().getValue() == null) {
          EdmNavigationProperty navProp = entry.getEntityType().findNavigationProperty(name);
          if (navProp != null) {
            // aha
            entry.links.add(OLinks.relatedEntityInline(name, name, entry.getUri() + "/" + name, null));
            return ees;
          }
        }
        throw new IllegalArgumentException("unknown property " + name + " for " + entry.getEntityType().getName());
      }
      if (!ep.getType().isSimple()) {
        // the only context that lands us here is a null value for a complex property
        if (event.asEndProperty().getValue() == null) {
          entry.properties.add(OProperties.complex(name, (EdmComplexType) ep.getType(), null));
        } else {
          throw new UnsupportedOperationException("complex property unknown parse state");
        }
      } else {
        entry.properties.add(JsonTypeConverter.parse(name, (EdmSimpleType<?>) ep.getType(), event.asEndProperty().getValue(), event.asEndProperty().getValueTokenType()));
      }
    } else if (event.isStartObject()) {
      // reference deferred or inlined

      JsonObjectPropertyValue val = getValue(event, ees, name, jsr, entry);

      if (val.uri != null) {
        // lookup the cardinality of the relationship so we can insert the correct
        // link type.
        EdmNavigationProperty navProp = entry.getEntityType().findNavigationProperty(name);
        if (navProp.getToRole().getMultiplicity() == EdmMultiplicity.MANY) {
          entry.links.add(OLinks.relatedEntities(name, name, val.uri));
        } else {
          entry.links.add(OLinks.relatedEntity(name, name, val.uri));
        }
      } else if (val.entity != null) {
        entry.links.add(OLinks.relatedEntityInline(name, name, entry.getUri() + "/" + name,
            val.entity));
      } else if (val.entities != null) {
        entry.links.add(OLinks.relatedEntitiesInline(name, name, entry.getUri() + "/" + name,
            val.entities));
      } else if (val.collection != null) {
        entry.properties.add(OProperties.collection(name, val.collectionType, val.collection));
      } else if (val.complexObject != null) {
        entry.properties.add(OProperties.complex(name, (EdmComplexType) val.complexObject.getType(),
            val.complexObject.getProperties()));
      }
    } else if (event.isStartArray()) {
      ensureNext(jsr);
      event = jsr.nextEvent();

      if (event.isValue()) {
        throw new IllegalArgumentException("arrays of primitive types not supported! property " + entry.getEntityType().getName() + "." + name);
      } else if (event.isStartObject()) {
        EdmNavigationProperty navProp = entry.getEntityType().findNavigationProperty(name);
        ees = metadata.getEdmEntitySet(navProp.getToRole().getType());
        List<OEntity> entities = new ArrayList<OEntity>();
        do {
          entities.add(parseEntry(ees, jsr).getEntity());
          event = jsr.nextEvent();
        } while (!event.isEndArray());
        entry.links.add(OLinks.relatedEntitiesInline(name, name, entry.getUri() + "/" + name,
            entities));
      } else {
        throw new IllegalArgumentException("What's that?");
      }

      ensureEndProperty(jsr.nextEvent());
    }
    return ees;
  }

  protected JsonObjectPropertyValue getValue(JsonEvent event, EdmEntitySet ees, String name, JsonStreamReader jsr, JsonEntry entry) {
    JsonObjectPropertyValue rt = new JsonObjectPropertyValue();

    ensureStartObject(event);

    event = jsr.nextEvent();
    ensureStartProperty(event);

    // "__deferred":
    if (DEFERRED_PROPERTY.equals(event.asStartProperty().getName())) {
      // deferred feed or entity

      // {
      ensureStartObject(jsr.nextEvent());

      // "uri" :
      ensureStartProperty(jsr.nextEvent(), URI_PROPERTY);
      // "uri" property value
      String uri = jsr.nextEvent().asEndProperty().getValue();

      rt.uri = uri;

      // }
      ensureEndObject(jsr.nextEvent());

      // eat EndObject event and EndProperty event for "__deferred"
      // }
      ensureEndProperty(jsr.nextEvent());
      ensureEndObject(jsr.nextEvent());

      // "results" :
    } else if (RESULTS_PROPERTY.equals(event.asStartProperty().getName())) {

      // if we support V1, put this in again
      /*
      if (version == ODataVersion.V1) {
        throw new IllegalArgumentException("no valid OData JSON format results not expected");
      }*/

      // inlined feed or a collection property
      EdmNavigationProperty navProp = entry.getEntityType().findNavigationProperty(name);

      if (navProp != null) {
        // [
        ensureStartArray(jsr.nextEvent());

        Feed feed = parseFeed(metadata.getEdmEntitySet(navProp.getToRole().getType()), jsr);

        rt.entities = Enumerable.create(feed.getEntries())
            .cast(JsonEntry.class)
            .select(new Func1<JsonEntry, OEntity>() {
              @Override
              public OEntity apply(JsonEntry input) {
                return input.getEntity();
              }
            }).toList();
      } else {
        EdmProperty eprop = entry.getEntityType().findProperty(name);
        if (eprop != null && eprop.getCollectionKind() != CollectionKind.NONE) {
          rt.collectionType = new EdmCollectionType(eprop.getCollectionKind(), eprop.getType());
          JsonCollectionFormatParser cfp = new JsonCollectionFormatParser(rt.collectionType, this.metadata);
          rt.collection = cfp.parseCollection(jsr);
        } else {
          throw new RuntimeException("unhandled property: " + name);
        }
      }

      ensureEndProperty(jsr.nextEvent());
      ensureEndObject(jsr.nextEvent());

    } else if (METADATA_PROPERTY.equals(event.asStartProperty().getName())) {
      // inlined entity or link starting with meta data
      EdmNavigationProperty navProp = entry.getEntityType().findNavigationProperty(name);
      JsonEntryMetaData jemd = parseMetadata(jsr);
      JsonEntry refentry = parseEntry(jemd, metadata.getEdmEntitySet(navProp.getToRole().getType()), jsr);

      // if we are parsing a request, the links to existing
      //  entities are represented as the inline representation
      //  of an entity with only the __metadata and no properties
      if (isResponse) {
        rt.entity = refentry.getEntity();
      } else {
        boolean isInlined = !refentry.properties.isEmpty() || !refentry.links.isEmpty();
        if (isInlined) {
          rt.entity = refentry.getEntity();
        } else {
          rt.uri = jemd.uri;
        }
      }
    } else if (event.isStartProperty()) {
      // inlined entity or complex object

      EdmNavigationProperty navProp = entry.getEntityType().findNavigationProperty(name);
      if (navProp != null) {
        ees = metadata.getEdmEntitySet(navProp.getToRole().getType());

        JsonEntry refEntry = new JsonEntry(ees, null);
        refEntry.properties = new ArrayList<OProperty<?>>();
        refEntry.links = new ArrayList<OLink>();
        do {
          addProperty(refEntry, ees, event.asStartProperty().getName(), jsr);
          event = jsr.nextEvent();
        } while (!event.isEndObject());
        rt.entity = toOEntity(ees, refEntry.getEntityType(), refEntry.getEntityKey(), refEntry.getETag(), refEntry.properties, refEntry.links);
      } else {
        EdmProperty prop = entry.getEntityType().findProperty(name);
        if (prop == null) {
          throw new RuntimeException("can't find property: " + name + " on type: " + entry.getEntityType().getName());
        } else {
          // why the lookup?  well, during metadata parsing, currently, EdmProperties with type=EdmComplexType are created
          // by using EdmType.get(typname).  This results in a useless instance of EdmNonSimpleType.  To fix,
          // someone is going to have to make EdmxFormatParser resolve property types at parse time.
          EdmComplexType ct = (prop.getType() instanceof EdmComplexType) ? ((EdmComplexType) prop.getType())
              : metadata.findEdmComplexType(prop.getType().getFullyQualifiedTypeName());

          if (ct != null) {
            JsonComplexObjectFormatParser cofp = new JsonComplexObjectFormatParser(ct);
            rt.complexObject = cofp.parseSingleObject(jsr, event);
          } else {
            throw new RuntimeException("unhandled property type: " + prop.getType().getFullyQualifiedTypeName());
          }
        }
      }
    } else {
      throw new IllegalArgumentException("What's that?");
    }

    ensureEndProperty(jsr.nextEvent());

    return rt;
  }

  protected void ensureNext(JsonStreamReader jsr) {
    if (!jsr.hasNext()) {
      throw new IllegalArgumentException("no valid JSON format exepected at least one more event");
    }
  }

  protected void ensureStartProperty(JsonEvent event) {
    if (!event.isStartProperty()) {
      throw new IllegalArgumentException("no valid OData JSON format (expected StartProperty got " + event + ")");
    }
  }

  protected void ensureStartProperty(JsonEvent event, String name) {
    if (!(event.isStartProperty()
    && name.equals(event.asStartProperty().getName()))) {
      throw new IllegalArgumentException("no valid OData JSON format (expected StartProperty " + name + " got " + event + ")");
    }
  }

  protected void ensureEndProperty(JsonEvent event) {
    if (!event.isEndProperty()) {
      throw new IllegalArgumentException("no valid OData JSON format (expected EndProperty got " + event + ")");
    }
  }

  protected void ensureStartObject(JsonEvent event) {
    if (!event.isStartObject()) {
      throw new IllegalArgumentException("no valid OData JSON format expected StartObject got " + event + ")");
    }
  }

  protected void ensureEndObject(JsonEvent event) {
    if (!event.isEndObject()) {
      throw new IllegalArgumentException("no valid OData JSON format expected EndObject got " + event + ")");
    }
  }

  protected void ensureStartArray(JsonEvent event) {
    if (!event.isStartArray()) {
      throw new IllegalArgumentException("no valid OData JSON format expected StartArray got " + event + ")");
    }
  }

  protected void ensureEndArray(JsonEvent event) {
    if (!event.isEndArray()) {
      throw new IllegalArgumentException("no valid OData JSON format expected EndArray got " + event + ")");
    }
  }

}
