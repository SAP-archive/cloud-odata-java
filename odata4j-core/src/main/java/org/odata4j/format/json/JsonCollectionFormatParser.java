package org.odata4j.format.json;

import java.io.Reader;

import org.odata4j.core.OCollection;
import org.odata4j.core.OCollections;
import org.odata4j.core.OComplexObject;
import org.odata4j.core.ODataVersion;
import org.odata4j.core.OObject;
import org.odata4j.core.OSimpleObjects;
import org.odata4j.edm.EdmCollectionType;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.edm.EdmType;
import org.odata4j.exceptions.NotImplementedException;
import org.odata4j.format.Entry;
import org.odata4j.format.FormatParser;
import org.odata4j.format.FormatParserFactory;
import org.odata4j.format.FormatType;
import org.odata4j.format.Settings;
import org.odata4j.format.json.JsonFeedFormatParser.JsonFeed;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader.JsonEvent;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader.JsonValueEvent;

/**
 * Parses an OCollection in JSON format.
 *
 * <pre>Collection types handled so far:
 * - OComplexObject
 *
 * TODO:
 * - all other types
 * </pre>
 */
public class JsonCollectionFormatParser extends JsonFormatParser implements FormatParser<OCollection<? extends OObject>> {

  private final EdmCollectionType returnType;

  public JsonCollectionFormatParser(Settings s) {
    super(s);
    returnType = (EdmCollectionType) (s == null ? null : s.parseType);
  }

  public JsonCollectionFormatParser(EdmCollectionType collectionType, EdmDataServices md) {
    super(null);
    this.metadata = md;
    returnType = collectionType;
  }

  @Override
  public OCollection<? extends OObject> parse(Reader reader) {

    if (this.returnType.getItemType().getClass().isAssignableFrom(EdmEntityType.class)) {
      return parseFunctionFeed(reader);
    }

    JsonStreamReader jsr = JsonStreamReaderFactory.createJsonStreamReader(reader);
    try {
      if (isResponse) {
        ensureNext(jsr);
        ensureStartObject(jsr.nextEvent()); // the response object

        // "d" property
        ensureNext(jsr);
        ensureStartProperty(jsr.nextEvent(), DATA_PROPERTY);

        // "aresult" for DataServiceVersion > 1.0
        if (version.compareTo(ODataVersion.V1) > 0) {
          ensureNext(jsr);
          ensureStartObject(jsr.nextEvent());
          ensureNext(jsr);
          ensureStartProperty(jsr.nextEvent(), RESULTS_PROPERTY);
        }
      }

      // parse the entry
      OCollection<? extends OObject> o = parseCollection(jsr);

      if (isResponse) {

        // the "d" property was our object...it is also a property.
        ensureNext(jsr);
        ensureEndProperty(jsr.nextEvent());

        if (version.compareTo(ODataVersion.V1) > 0) {
          ensureNext(jsr);
          ensureEndObject(jsr.nextEvent());
          ensureNext(jsr);
          ensureEndProperty(jsr.nextEvent()); // "results"
        }
        ensureNext(jsr);
        ensureEndObject(jsr.nextEvent()); // the response object
      }

      return o;

    } finally {
      jsr.close();
    }
  }

  protected OCollection<? extends OObject> parseFunctionFeed(Reader reader) {

    // entitySetName is a function
    // this really reveals a fundamental flaw in the parsers being driven by EdmEntitySets
    // instead of EdmEntityTypes.

    EdmEntitySet entitySet = this.metadata.getEdmEntitySet((EdmEntityType) returnType.getItemType());

    Settings settings = new Settings(
        // someone really needs to spend some time on service version negotiation....
        ODataVersion.V2,
        this.metadata,
        entitySet.getName(),
        this.entityKey,
        null, // feed customization mapping
        this.isResponse,
        this.returnType.getItemType());

    JsonFeedFormatParser parser = new JsonFeedFormatParser(settings);
    JsonFeed feed = parser.parse(reader);
    OCollection.Builder<OObject> c = newCollectionBuilder();
    for (Entry e : feed.getEntries()) {
      c.add(e.getEntity());
    }
    return c.build();
  }

  protected OCollection<? extends OObject> parseCollection(JsonStreamReader jsr) {
    // an array of objects:
    ensureNext(jsr);
    ensureStartArray(jsr.nextEvent());

    OCollection.Builder<OObject> c = newCollectionBuilder();

    if (this.returnType.getItemType().isSimple()) {
      parseCollectionOfSimple(c, jsr);
    } else {
      FormatParser<? extends OObject> parser = createItemParser(this.returnType.getItemType());

      while (jsr.hasNext()) {
        // this is what I really want to do next:
        // OObject o = parser.parse(jsr);
        // however, the FormatParser api would have to be genericized, we would need an interface for
        // the event-oriented parsers (JsonStreamReader, XMLStreamReader).
        // I just don't have the time at this momement...

        if (parser instanceof JsonComplexObjectFormatParser) {
          OComplexObject obj = ((JsonComplexObjectFormatParser) parser).parseSingleObject(jsr);
          // null if not there
          if (obj != null) {
            c = c.add(obj);
          } else {
            break;
          }
        } else {
          throw new NotImplementedException("collections of type: " + this.returnType.getItemType().getFullyQualifiedTypeName() + " not implemented");
        }
      }
    }

    // we should see the end of the array
    ensureEndArray(jsr.previousEvent());

    return c.build();
  }

  protected void parseCollectionOfSimple(OCollection.Builder<OObject> builder, JsonStreamReader jsr) {
    while (jsr.hasNext()) {
      JsonEvent e = jsr.nextEvent();
      if (e.isValue()) {
        JsonValueEvent ve = e.asValue();
        builder.add(OSimpleObjects.parse((EdmSimpleType<?>) this.returnType.getItemType(), ve.getValue()));
      } else if (e.isEndArray()) {
        break;
      } else {
        throw new RuntimeException("invalid JSON content");
      }
    }
  }

  protected OCollection.Builder<OObject> newCollectionBuilder() {
    return OCollections.<OObject> newBuilder(this.returnType.getItemType());
  }

  protected FormatParser<? extends OObject> createItemParser(EdmType edmType) {
    // each item is parsed as a standalone item, not a response item
    Settings s = new Settings(
        this.version,
        this.metadata,
        this.entitySetName,
        this.entityKey,
        null, // FeedCustomizationMapping fcMapping,
        false, // boolean isResponse);
        edmType); // expected type

    return FormatParserFactory.getParser(EdmType.getInstanceType(edmType), FormatType.JSON, s);
  }
}
