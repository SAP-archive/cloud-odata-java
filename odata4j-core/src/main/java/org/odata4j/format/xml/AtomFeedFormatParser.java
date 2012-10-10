package org.odata4j.format.xml;

import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;

import org.core4j.Enumerable;
import org.core4j.Func1;
import org.odata4j.core.OEntities;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OLink;
import org.odata4j.core.OLinks;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmComplexType;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.edm.EdmNavigationProperty;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.edm.EdmStructuralType;
import org.odata4j.edm.EdmType;
import org.odata4j.format.Entry;
import org.odata4j.format.Feed;
import org.odata4j.format.FormatParser;
import org.odata4j.internal.FeedCustomizationMapping;
import org.odata4j.internal.InternalUtil;
import org.odata4j.stax2.Attribute2;
import org.odata4j.stax2.QName2;
import org.odata4j.stax2.StartElement2;
import org.odata4j.stax2.XMLEvent2;
import org.odata4j.stax2.XMLEventReader2;
import org.odata4j.stax2.XMLEventWriter2;
import org.odata4j.stax2.XMLFactoryProvider2;
import org.odata4j.stax2.util.StaxUtil;

public class AtomFeedFormatParser extends XmlFormatParser implements FormatParser<Feed> {

  protected EdmDataServices metadata;
  protected String entitySetName;
  protected OEntityKey entityKey;
  protected FeedCustomizationMapping fcMapping;

  public AtomFeedFormatParser(EdmDataServices metadata, String entitySetName, OEntityKey entityKey, FeedCustomizationMapping fcMapping) {
    this.metadata = metadata;
    this.entitySetName = entitySetName;
    this.entityKey = entityKey;
    this.fcMapping = fcMapping;
  }

  public static class AtomFeed implements Feed {
    public String next;
    public Iterable<Entry> entries;

    @Override
    public Iterable<Entry> getEntries() {
      return entries;
    }

    @Override
    public String getNext() {
      return next;
    }
  }

  abstract static class AtomEntry implements Entry {
    public String id;
    public String title;
    public String summary;
    public String updated;
    public String categoryTerm;
    public String categoryScheme;
    public String contentType;

    public List<AtomLink> atomLinks;

    public String getUri() {
      return null;
    }

    public String getETag() {
      return null;
    }

    public String getType() {
      return MediaType.APPLICATION_ATOM_XML;
    }
  }

  static class AtomLink {
    public String relation;
    public String title;
    public String type;
    public String href;
    public AtomFeed inlineFeed;
    public AtomEntry inlineEntry;
    public boolean inlineContentExpected;

    public String getNavProperty() {
      if (relation != null && relation.startsWith(XmlFormatWriter.related))
        return relation.substring(XmlFormatWriter.related.length());
      else
        return null;
    }
  }

  static class BasicAtomEntry extends AtomEntry {
    public String content;

    @Override
    public String toString() {
      return InternalUtil.reflectionToString(this);
    }

    @Override
    public OEntity getEntity() {
      return null;
    }
  }

  public static class DataServicesAtomEntry extends AtomEntry {
    public final String etag;
    public final List<OProperty<?>> properties;

    private OEntity entity;

    private DataServicesAtomEntry(String etag, List<OProperty<?>> properties) {
      this.etag = etag;
      this.properties = properties;
    }

    @Override
    public String toString() {
      return InternalUtil.reflectionToString(this);
    }

    @Override
    public OEntity getEntity() {
      return this.entity;
    }

    void setEntity(OEntity entity) {
      this.entity = entity;
    }
  }

  @Override
  public AtomFeed parse(Reader reader) {
    return parseFeed(StaxUtil.newXMLEventReader(reader), getEntitySet());
  }

  AtomFeed parseFeed(XMLEventReader2 reader, EdmEntitySet entitySet) {

    AtomFeed feed = new AtomFeed();
    List<AtomEntry> rt = new ArrayList<AtomEntry>();

    while (reader.hasNext()) {
      XMLEvent2 event = reader.nextEvent();

      if (isStartElement(event, ATOM_ENTRY)) {
        rt.add(parseEntry(reader, event.asStartElement(), entitySet));
      } else if (isStartElement(event, ATOM_LINK)) {
        if ("next".equals(event.asStartElement().getAttributeByName(new QName2("rel")).getValue())) {
          feed.next = event.asStartElement().getAttributeByName(new QName2("href")).getValue();
        }
      } else if (isEndElement(event, ATOM_FEED)) {
        // return from a sub feed, if we went down the hierarchy
        break;
      }

    }
    feed.entries = Enumerable.create(rt).cast(Entry.class);

    return feed;

  }

  public static Iterable<OProperty<?>> parseProperties(XMLEventReader2 reader, StartElement2 propertiesElement, EdmDataServices metadata, EdmStructuralType structuralType) {
    List<OProperty<?>> rt = new ArrayList<OProperty<?>>();

    while (reader.hasNext()) {
      XMLEvent2 event = reader.nextEvent();

      if (event.isEndElement() && event.asEndElement().getName().equals(propertiesElement.getName())) {
        return rt;
      }

      if (event.isStartElement() && event.asStartElement().getName().getNamespaceUri().equals(NS_DATASERVICES)) {

        String name = event.asStartElement().getName().getLocalPart();
        Attribute2 typeAttribute = event.asStartElement().getAttributeByName(M_TYPE);
        Attribute2 nullAttribute = event.asStartElement().getAttributeByName(M_NULL);
        boolean isNull = nullAttribute != null && "true".equals(nullAttribute.getValue());

        OProperty<?> op = null;

        EdmType et = null;
        if (typeAttribute != null) {
          String type = typeAttribute.getValue();
          et = metadata.resolveType(type);
          if (et == null) {
            // property arrived with an unknown type
            throw new RuntimeException("unknown property type: " + type);
          }
        } else {
          EdmProperty property = (EdmProperty) structuralType.findProperty(name);
          if (property != null)
            et = property.getType();
          else
            et = EdmSimpleType.STRING; // we must support open types
        }

        if (et != null && (!et.isSimple())) {
          EdmStructuralType est = (EdmStructuralType) et;
          op = OProperties.complex(name, (EdmComplexType) et, isNull ? null : Enumerable.create(parseProperties(reader, event.asStartElement(), metadata, est)).toList());
        } else {
          op = OProperties.parseSimple(name, (EdmSimpleType<?>) et, isNull ? null : reader.getElementText());
        }
        rt.add(op);
      }
    }

    throw new RuntimeException();
  }

  private AtomLink parseAtomLink(XMLEventReader2 reader, StartElement2 linkElement, EdmEntitySet entitySet) {
    AtomLink rt = new AtomLink();
    rt.relation = getAttributeValueIfExists(linkElement, "rel");
    rt.type = getAttributeValueIfExists(linkElement, "type");
    rt.title = getAttributeValueIfExists(linkElement, "title");
    rt.href = getAttributeValueIfExists(linkElement, "href");
    rt.inlineContentExpected = false;

    String navPropertyName = rt.getNavProperty();
    EdmNavigationProperty navProperty = null;
    if (entitySet != null && navPropertyName != null)
      navProperty = entitySet.getType().findNavigationProperty(navPropertyName);
    EdmEntitySet targetEntitySet = null;
    if (navProperty != null)
      targetEntitySet = metadata.getEdmEntitySet(navProperty.getToRole().getType());

    // expected cases:
    // 1.  </link>                  - no inlined content, i.e. deferred
    // 2.  <m:inline/></link>       - inlined content but null entity or empty feed
    // 3.  <m:inline><feed>...</m:inline></link> - inlined content with 1 or more items
    // 4.  <m:inline><entry>..</m:inline></link> - inlined content 1 an item

    while (reader.hasNext()) {
      XMLEvent2 event = reader.nextEvent();

      if (event.isEndElement() && event.asEndElement().getName().equals(linkElement.getName())) {
        break;
      } else if (isStartElement(event, XmlFormatParser.M_INLINE)) {
        rt.inlineContentExpected = true; // may be null content.
      } else if (isStartElement(event, ATOM_FEED)) {
        rt.inlineFeed = parseFeed(reader, targetEntitySet);
      } else if (isStartElement(event, ATOM_ENTRY)) {
        rt.inlineEntry = parseEntry(reader, event.asStartElement(), targetEntitySet);
      }
    }
    return rt;
  }

  private DataServicesAtomEntry parseDSAtomEntry(String etag, EdmEntityType entityType, XMLEventReader2 reader, XMLEvent2 event) {
    List<OProperty<?>> properties = Enumerable.create(parseProperties(reader, event.asStartElement(), metadata, entityType)).toList();
    return new DataServicesAtomEntry(etag, properties);
  }

  private static String innerText(XMLEventReader2 reader, StartElement2 element) {
    StringWriter sw = new StringWriter();
    XMLEventWriter2 writer = XMLFactoryProvider2.getInstance().newXMLOutputFactory2().createXMLEventWriter(sw);
    while (reader.hasNext()) {

      XMLEvent2 event = reader.nextEvent();
      if (event.isEndElement() && event.asEndElement().getName().equals(element.getName())) {

        return sw.toString();
      } else {
        writer.add(event);
      }

    }
    throw new RuntimeException();
  }

  private static final Pattern ENTITY_SET_NAME = Pattern.compile("\\/([^\\/\\(]+)\\(");

  public static OEntityKey parseEntityKey(String atomEntryId) {
    Matcher m = ENTITY_SET_NAME.matcher(atomEntryId);
    if (!m.find())
      throw new RuntimeException("Unable to parse the entity-key from atom entry id: " + atomEntryId);
    return OEntityKey.parse(atomEntryId.substring(m.end() - 1));
  }

  private EdmEntitySet getEntitySet() {
    EdmEntitySet entitySet = null;
    if (!metadata.getSchemas().isEmpty()) {
      entitySet = metadata.findEdmEntitySet(entitySetName);
      if (entitySet == null) {
        // panic! could not determine the entity-set, is it a function?
        EdmFunctionImport efi = metadata.findEdmFunctionImport(entitySetName);
        if (efi != null)
          entitySet = efi.getEntitySet();
      }
    }
    if (entitySet == null)
      throw new RuntimeException("Could not derive the entity-set " + entitySetName);
    return entitySet;
  }

  private AtomEntry parseEntry(XMLEventReader2 reader, StartElement2 entryElement, EdmEntitySet entitySet) {

    String id = null;
    String categoryTerm = null;
    String categoryScheme = null;
    String title = null;
    String summary = null;
    String updated = null;
    String contentType = null;
    List<AtomLink> atomLinks = new ArrayList<AtomLink>();

    String etag = getAttributeValueIfExists(entryElement, M_ETAG);

    AtomEntry rt = null;

    while (reader.hasNext()) {
      XMLEvent2 event = reader.nextEvent();

      if (event.isEndElement() && event.asEndElement().getName().equals(entryElement.getName())) {
        rt.id = id; //http://localhost:8810/Oneoff01.svc/Comment(1)
        rt.title = title;
        rt.summary = summary;
        rt.updated = updated;
        rt.categoryScheme = categoryScheme; //http://schemas.microsoft.com/ado/2007/08/dataservices/scheme
        rt.categoryTerm = categoryTerm; //NorthwindModel.Customer
        rt.contentType = contentType;
        rt.atomLinks = atomLinks;

        if (rt instanceof DataServicesAtomEntry) {
          DataServicesAtomEntry dsae = (DataServicesAtomEntry) rt;
          OEntity entity = entityFromAtomEntry(metadata, entitySet, dsae, fcMapping);
          dsae.setEntity(entity);
        }
        return rt;
      }

      if (isStartElement(event, ATOM_ID)) {
        id = reader.getElementText();
      } else if (isStartElement(event, ATOM_TITLE)) {
        title = reader.getElementText();
      } else if (isStartElement(event, ATOM_SUMMARY)) {
        summary = reader.getElementText();
      } else if (isStartElement(event, ATOM_UPDATED)) {
        updated = reader.getElementText();
      } else if (isStartElement(event, ATOM_CATEGORY)) {
        categoryTerm = getAttributeValueIfExists(event.asStartElement(), "term");
        categoryScheme = getAttributeValueIfExists(event.asStartElement(), "scheme");
        if (categoryTerm != null)
          entitySet = metadata.getEdmEntitySet((EdmEntityType) metadata.findEdmEntityType(categoryTerm));
      } else if (isStartElement(event, ATOM_LINK)) {
        AtomLink link = parseAtomLink(reader, event.asStartElement(), entitySet);
        atomLinks.add(link);
      } else if (isStartElement(event, M_PROPERTIES)) {
        rt = parseDSAtomEntry(etag, entitySet.getType(), reader, event);
      } else if (isStartElement(event, ATOM_CONTENT)) {
        contentType = getAttributeValueIfExists(event.asStartElement(), "type");
        if (MediaType.APPLICATION_XML.equals(contentType)) {
          StartElement2 contentElement = event.asStartElement();
          StartElement2 valueElement = null;
          while (reader.hasNext()) {
            XMLEvent2 event2 = reader.nextEvent();
            if (valueElement == null && event2.isStartElement()) {
              valueElement = event2.asStartElement();
              if (isStartElement(event2, M_PROPERTIES)) {
                rt = parseDSAtomEntry(etag, entitySet.getType(), reader, event2);
              } else {
                BasicAtomEntry bae = new BasicAtomEntry();
                bae.content = innerText(reader, event2.asStartElement());
                rt = bae;
              }
            }
            if (event2.isEndElement() && event2.asEndElement().getName().equals(contentElement.getName())) {
              break;
            }
          }
        } else {
          BasicAtomEntry bae = new BasicAtomEntry();
          bae.content = innerText(reader, event.asStartElement());
          rt = bae;
        }
      }
    }
    throw new RuntimeException();
  }

  private OEntity entityFromAtomEntry(
      EdmDataServices metadata,
      EdmEntitySet entitySet,
      DataServicesAtomEntry dsae,
      FeedCustomizationMapping mapping) {

    List<OProperty<?>> props = dsae.properties;
    if (mapping != null) {
      Enumerable<OProperty<?>> properties = Enumerable.create(dsae.properties);
      if (mapping.titlePropName != null)
        properties = properties.concat(OProperties.string(mapping.titlePropName, dsae.title));
      if (mapping.summaryPropName != null)
        properties = properties.concat(OProperties.string(mapping.summaryPropName, dsae.summary));

      props = properties.toList();
    }

    EdmEntityType entityType = entitySet.getType();
    if (dsae.categoryTerm != null) {
      // The type of an entity set is polymorphic...
      entityType = (EdmEntityType) metadata.findEdmEntityType(dsae.categoryTerm);
      if (entityType == null) {
        throw new RuntimeException("Unable to resolve entity type " + dsae.categoryTerm);
      }
    }
    // favor the key we just parsed.

    OEntityKey key = dsae.id != null
        ? (dsae.id.endsWith(")")
            ? parseEntityKey(dsae.id)
            : OEntityKey.infer(entitySet, props))
        : null;

    if (key == null) {
      key = entityKey;
    }

    if (key == null)
      return OEntities.createRequest(
          entitySet,
          props,
          toOLinks(metadata, entitySet, dsae.atomLinks, mapping),
          dsae.title,
          dsae.categoryTerm);

    return OEntities.create(
        entitySet,
        entityType,
        key,
        dsae.etag,
        props,
        toOLinks(metadata, entitySet, dsae.atomLinks, mapping),
        dsae.title,
        dsae.categoryTerm);
  }

  private List<OLink> toOLinks(
      final EdmDataServices metadata,
      EdmEntitySet fromRoleEntitySet,
      List<AtomLink> links,
      final FeedCustomizationMapping mapping) {
    List<OLink> rt = new ArrayList<OLink>(links.size());
    for (final AtomLink link : links) {

      if (link.relation.startsWith(XmlFormatWriter.related)) {
        if (link.type.equals(XmlFormatWriter.atom_feed_content_type)) {

          if (link.inlineContentExpected) {
            List<OEntity> relatedEntities = null;

            if (link.inlineFeed != null && link.inlineFeed.entries != null) {

              // get the entity set belonging to the from role type
              EdmNavigationProperty navProperty = fromRoleEntitySet != null
                  ? fromRoleEntitySet.getType().findNavigationProperty(link.getNavProperty())
                  : null;
              final EdmEntitySet toRoleEntitySet = metadata != null && navProperty != null
                  ? metadata.getEdmEntitySet(navProperty.getToRole().getType())
                  : null;

              // convert the atom feed entries to OEntitys
              relatedEntities = Enumerable
                  .create(link.inlineFeed.entries)
                  .cast(DataServicesAtomEntry.class)
                  .select(new Func1<DataServicesAtomEntry, OEntity>() {
                    @Override
                    public OEntity apply(
                        DataServicesAtomEntry input) {
                      return entityFromAtomEntry(metadata, toRoleEntitySet, input, mapping);
                    }
                  }).toList();
            } // else empty feed.
            rt.add(OLinks.relatedEntitiesInline(
                link.relation,
                link.title,
                link.href,
                relatedEntities));
          } else {
            // no inlined entities
            rt.add(OLinks.relatedEntities(link.relation, link.title, link.href));
          }
        } else if (link.type.equals(XmlFormatWriter.atom_entry_content_type))
          if (link.inlineContentExpected) {
            OEntity relatedEntity = null;
            if (link.inlineEntry != null) {
              EdmNavigationProperty navProperty = fromRoleEntitySet != null
                  ? fromRoleEntitySet.getType().findNavigationProperty(link.getNavProperty())
                  : null;
              EdmEntitySet toRoleEntitySet = metadata != null && navProperty != null
                  ? metadata.getEdmEntitySet(navProperty.getToRole().getType())
                  : null;
              relatedEntity = entityFromAtomEntry(metadata, toRoleEntitySet,
                  (DataServicesAtomEntry) link.inlineEntry,
                  mapping);
            }
            rt.add(OLinks.relatedEntityInline(link.relation,
                link.title, link.href, relatedEntity));
          } else {
            // no inlined entity
          rt.add(OLinks.relatedEntity(link.relation, link.title, link.href));
        }
      }
    }
    return rt;
  }

}
