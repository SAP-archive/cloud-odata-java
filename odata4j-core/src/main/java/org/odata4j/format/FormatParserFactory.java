package org.odata4j.format;

import javax.ws.rs.core.MediaType;

import org.odata4j.core.OCollection;
import org.odata4j.core.OComplexObject;
import org.odata4j.core.OEntity;
import org.odata4j.core.OError;
import org.odata4j.core.OObject;
import org.odata4j.core.OSimpleObject;
import org.odata4j.exceptions.UnsupportedMediaTypeException;
import org.odata4j.format.json.JsonCollectionFormatParser;
import org.odata4j.format.json.JsonComplexObjectFormatParser;
import org.odata4j.format.json.JsonEntityFormatParser;
import org.odata4j.format.json.JsonEntryFormatParser;
import org.odata4j.format.json.JsonErrorFormatParser;
import org.odata4j.format.json.JsonFeedFormatParser;
import org.odata4j.format.json.JsonSimpleObjectFormatParser;
import org.odata4j.format.json.JsonSingleLinkFormatParser;
import org.odata4j.format.xml.AtomEntryFormatParser;
import org.odata4j.format.xml.AtomErrorFormatParser;
import org.odata4j.format.xml.AtomFeedFormatParser;
import org.odata4j.format.xml.AtomSimpleObjectFormatParser;
import org.odata4j.format.xml.AtomSingleLinkFormatParser;

public class FormatParserFactory {

  private FormatParserFactory() {}

  private static interface FormatParsers {
    FormatParser<Feed> getFeedFormatParser(Settings settings);

    FormatParser<Entry> getEntryFormatParser(Settings settings);

    FormatParser<SingleLink> getSingleLinkFormatParser(Settings settings);

    FormatParser<OComplexObject> getComplexObjectFormatParser(Settings settings);

    FormatParser<OCollection<? extends OObject>> getCollectionFormatParser(Settings settings);

    FormatParser<OSimpleObject<?>> getSimpleObjectFormatParser(Settings settings);

    FormatParser<OError> getErrorFormatParser(Settings settings);

    FormatParser<OEntity> getEntityFormatParser(Settings settings);
  }

  @SuppressWarnings("unchecked")
  public static <T> FormatParser<T> getParser(Class<T> targetType,
      FormatType type, Settings settings) {
    FormatParsers formatParsers = type.equals(FormatType.JSON)
        ? new JsonParsers()
        : new AtomParsers();

    if (Feed.class.isAssignableFrom(targetType)) {
      return (FormatParser<T>) formatParsers.getFeedFormatParser(settings);
    } else if (Entry.class.isAssignableFrom(targetType)) {
      return (FormatParser<T>) formatParsers.getEntryFormatParser(settings);
    } else if (SingleLink.class.isAssignableFrom(targetType)) {
      return (FormatParser<T>) formatParsers.getSingleLinkFormatParser(settings);
    } else if (OComplexObject.class.isAssignableFrom(targetType)) {
      return (FormatParser<T>) formatParsers.getComplexObjectFormatParser(settings);
    } else if (OCollection.class.isAssignableFrom(targetType)) {
      return (FormatParser<T>) formatParsers.getCollectionFormatParser(settings);
    } else if (OSimpleObject.class.isAssignableFrom(targetType)) {
      return (FormatParser<T>) formatParsers.getSimpleObjectFormatParser(settings);
    } else if (OError.class.isAssignableFrom(targetType)) {
      return (FormatParser<T>) formatParsers.getErrorFormatParser(settings);
    } else if (OEntity.class.isAssignableFrom(targetType)) {
      return (FormatParser<T>) formatParsers.getEntityFormatParser(settings);
    }
    throw new IllegalArgumentException("Unable to locate format parser for " + targetType.getName() + " and format " + type);
  }

  public static <T> FormatParser<T> getParser(Class<T> targetType, MediaType contentType, Settings settings) {

    FormatType type;
    if (contentType.isCompatible(MediaType.APPLICATION_JSON_TYPE))
      type = FormatType.JSON;
    else if (contentType.isCompatible(MediaType.APPLICATION_ATOM_XML_TYPE) && (Feed.class.isAssignableFrom(targetType) || Entry.class.isAssignableFrom(targetType))
        || contentType.isCompatible(MediaType.APPLICATION_XML_TYPE))
      type = FormatType.ATOM;
    else
      throw new UnsupportedMediaTypeException("Unknown content type " + contentType);

    return getParser(targetType, type, settings);
  }

  public static class JsonParsers implements FormatParsers {

    @Override
    public FormatParser<Feed> getFeedFormatParser(Settings settings) {
      return new JsonFeedFormatParser(settings);
    }

    @Override
    public FormatParser<Entry> getEntryFormatParser(Settings settings) {
      return new JsonEntryFormatParser(settings);
    }

    @Override
    public FormatParser<SingleLink> getSingleLinkFormatParser(Settings settings) {
      return new JsonSingleLinkFormatParser(settings);
    }

    @Override
    public FormatParser<OComplexObject> getComplexObjectFormatParser(Settings settings) {
      return new JsonComplexObjectFormatParser(settings);
    }

    @Override
    public FormatParser<OCollection<? extends OObject>> getCollectionFormatParser(Settings settings) {
      return new JsonCollectionFormatParser(settings);
    }

    @Override
    public FormatParser<OSimpleObject<?>> getSimpleObjectFormatParser(Settings settings) {
      return new JsonSimpleObjectFormatParser(settings);
    }

    @Override
    public FormatParser<OError> getErrorFormatParser(Settings settings) {
      return new JsonErrorFormatParser(settings);
    }

    @Override
    public FormatParser<OEntity> getEntityFormatParser(Settings settings) {
      return new JsonEntityFormatParser(settings);
    }

  }

  public static class AtomParsers implements FormatParsers {

    @Override
    public FormatParser<Feed> getFeedFormatParser(Settings settings) {
      return new AtomFeedFormatParser(settings.metadata, settings.entitySetName, settings.entityKey, settings.fcMapping);
    }

    @Override
    public FormatParser<Entry> getEntryFormatParser(Settings settings) {
      return new AtomEntryFormatParser(settings.metadata, settings.entitySetName, settings.entityKey, settings.fcMapping);
    }

    @Override
    public FormatParser<SingleLink> getSingleLinkFormatParser(Settings settings) {
      return new AtomSingleLinkFormatParser();
    }

    @Override
    public FormatParser<OComplexObject> getComplexObjectFormatParser(Settings settings) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FormatParser<OCollection<? extends OObject>> getCollectionFormatParser(Settings settings) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FormatParser<OSimpleObject<?>> getSimpleObjectFormatParser(Settings settings) {
      return new AtomSimpleObjectFormatParser(settings);
    }

    @Override
    public FormatParser<OError> getErrorFormatParser(Settings settings) {
      return new AtomErrorFormatParser();
    }

    @Override
    public FormatParser<OEntity> getEntityFormatParser(Settings settings) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

  }
}
