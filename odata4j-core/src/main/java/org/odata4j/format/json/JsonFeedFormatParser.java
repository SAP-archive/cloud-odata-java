package org.odata4j.format.json;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.odata4j.core.ODataVersion;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OLink;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.format.Entry;
import org.odata4j.format.Feed;
import org.odata4j.format.FormatParser;
import org.odata4j.format.Settings;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamReader.JsonEvent;

public class JsonFeedFormatParser extends JsonFormatParser implements FormatParser<Feed> {

  static class JsonFeed implements Feed {
    List<Entry> entries;
    String next;
    Integer inlineCount;

    @Override
    public String getNext() {
      return next;
    }

    @Override
    public Iterable<Entry> getEntries() {
      return entries;
    }

  }

  static class JsonEntry implements Entry {

    private EdmEntitySet entitySet;
    private EdmEntityType entityType;

    JsonEntryMetaData jemd;
    List<OProperty<?>> properties;
    List<OLink> links;
    OEntity oentity;

    public JsonEntry(EdmEntitySet eset, JsonEntryMetaData jemd) {
      this.entitySet = eset;
      this.entityType = eset != null ? eset.getType() : null;
      this.jemd = jemd;
    }

    public String getContentType() {
      return MediaType.APPLICATION_JSON;
    }

    public JsonEntryMetaData getJemd() {
      return this.jemd;
    }

    public EdmEntitySet getEntitySet() {
      return this.entitySet;
    }

    public EdmEntityType getEntityType() {
      return this.entityType;
    }

    public void setEntityType(EdmEntityType value) {
      this.entityType = value;
    }

    @Override
    public String getUri() {
      return jemd == null ? null : jemd.uri;
    }

    public String getETag() {
      return jemd == null ? null : jemd.etag;
    }

    @Override
    public OEntity getEntity() {
      return oentity;
    }

    public OEntityKey getEntityKey() {
      String uri = getUri();
      if (uri == null)
        return null;
      return OEntityKey.parse(uri.substring(uri.lastIndexOf('(')));
    }

  }

  public JsonFeedFormatParser(Settings settings) {
    super(settings);
  }

  @Override
  public JsonFeed parse(Reader reader) {
    JsonStreamReader jsr = JsonStreamReaderFactory.createJsonStreamReader(reader);
    try {
      // {
      ensureStartObject(jsr.nextEvent());

      // "d" :
      ensureStartProperty(jsr.nextEvent(), DATA_PROPERTY);

      if (version.compareTo(ODataVersion.V1) > 0) {
        // {
        ensureStartObject(jsr.nextEvent());
        // "results" :
        ensureStartProperty(jsr.nextEvent(), RESULTS_PROPERTY);
      }

      // skip [ or {
      JsonEvent event = jsr.nextEvent();
      JsonFeed feed;
      if (event.isStartArray()) {
        feed = parseFeed(metadata.getEdmEntitySet(entitySetName), jsr);
        // ] already processed by parseFeed
      } else {
        feed = new JsonFeed();
        feed.entries = new ArrayList<Entry>();
        feed.entries.add(parseEntry(metadata.getEdmEntitySet(entitySetName), jsr));
        // } already processed by parseEntry
      }

      if (version.compareTo(ODataVersion.V1) > 0) {
        // EndProperty of "results" :
        ensureEndProperty(jsr.nextEvent());
      }

      event = jsr.nextEvent();

      while (event.isStartProperty()) {
        String pname = event.asStartProperty().getName();
        ensureNext(jsr);
        ensureEndProperty(event = jsr.nextEvent());
        if (NEXT_PROPERTY.equals(pname)) {
          feed.next = event.asEndProperty().getValue();
        } else if (COUNT_PROPERTY.equals(pname)) {
          feed.inlineCount = Integer.parseInt(event.asEndProperty().getValue());
        }
        ensureNext(jsr);
        event = jsr.nextEvent();
      }

      if (version.compareTo(ODataVersion.V1) > 0) {
        // EndObject and EndProperty of "result" :
        ensureEndObject(event);
        ensureEndProperty(jsr.nextEvent());
      }

      ensureEndObject(jsr.nextEvent());

      if (jsr.hasNext())
        throw new IllegalArgumentException("garbage after the feed");

      return feed;

    } finally {
      jsr.close();
    }
  }

}
