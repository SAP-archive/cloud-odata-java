package org.odata4j.format.xml;

import java.io.Reader;

import org.odata4j.core.OEntityKey;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.format.Entry;
import org.odata4j.format.FormatParser;
import org.odata4j.internal.FeedCustomizationMapping;

public class AtomEntryFormatParser implements FormatParser<Entry> {

  protected EdmDataServices metadata;
  protected String entitySetName;
  protected OEntityKey entityKey;
  protected FeedCustomizationMapping fcMapping;

  public AtomEntryFormatParser(EdmDataServices metadata, String entitySetName, OEntityKey entityKey, FeedCustomizationMapping fcMapping) {
    this.metadata = metadata;
    this.entitySetName = entitySetName;
    this.entityKey = entityKey;
    this.fcMapping = fcMapping;
  }

  @Override
  public Entry parse(Reader reader) {
    return new AtomFeedFormatParser(metadata, entitySetName, entityKey, fcMapping)
        .parse(reader).entries.iterator().next();
  }

}
