package org.odata4j.test.integration.producer.custom;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

import org.odata4j.core.OCollection.Builder;
import org.odata4j.core.OCollections;
import org.odata4j.core.OComplexObject;
import org.odata4j.core.OComplexObjects;
import org.odata4j.core.OEntities;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OExtension;
import org.odata4j.core.OFunctionParameter;
import org.odata4j.core.OLink;
import org.odata4j.core.OLinks;
import org.odata4j.core.OObject;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;
import org.odata4j.core.OSimpleObjects;
import org.odata4j.edm.EdmCollectionType;
import org.odata4j.edm.EdmComplexType;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.edm.EdmProperty.CollectionKind;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.exceptions.BadRequestException;
import org.odata4j.exceptions.NotFoundException;
import org.odata4j.exceptions.NotImplementedException;
import org.odata4j.producer.BaseResponse;
import org.odata4j.producer.CountResponse;
import org.odata4j.producer.EntitiesResponse;
import org.odata4j.producer.EntityIdResponse;
import org.odata4j.producer.EntityQueryInfo;
import org.odata4j.producer.EntityResponse;
import org.odata4j.producer.ODataContext;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.OMediaLinkExtension;
import org.odata4j.producer.OMediaLinkExtensions;
import org.odata4j.producer.PropertyPathHelper;
import org.odata4j.producer.QueryInfo;
import org.odata4j.producer.Responses;
import org.odata4j.producer.edm.MetadataProducer;

/**
 * A custom producer for various test scenarios that aren't possible with
 * stock producers
 */
public class CustomProducer implements ODataProducer {

  private final EdmDataServices edm = new CustomEdm().generateEdm(null).build();
  private final MetadataProducer metadataProducer;

  public CustomProducer() {
    this.metadataProducer = new MetadataProducer(this, null);
    this.initResources();
  }

  @Override
  public EdmDataServices getMetadata() {
    return edm;
  }

  @Override
  public MetadataProducer getMetadataProducer() {
    return metadataProducer;
  }

  @Override
  public EntitiesResponse getEntities(ODataContext context, String entitySetName, QueryInfo queryInfo) {
    if (entitySetName.equals("Type1s")) {
      return Responses.entities(getType1s(), edm.findEdmEntitySet(entitySetName), null, null);
    } else if (entitySetName.equals("FileSystemItems")) {
      return Responses.entities(getFileSystemItems(queryInfo), edm.findEdmEntitySet(entitySetName), null, null);
    } else if (entitySetName.equals("Directories")) {
      return Responses.entities(getDirectories(queryInfo), edm.findEdmEntitySet(entitySetName), null, null);
    } else if (entitySetName.equals("Files")) {
      return Responses.entities(getFiles(queryInfo), edm.findEdmEntitySet(entitySetName), null, null);
    } else {
      throw new NotFoundException("Unknown entity set: " + entitySetName);
    }
  }

  @Override
  public CountResponse getEntitiesCount(ODataContext context, String entitySetName, QueryInfo queryInfo) {
    throw new NotImplementedException();
  }

  private int nDirs = 5;
  private Map<String, OEntity> dirs = new HashMap<String, OEntity>();

  private List<OEntity> getDirectories(QueryInfo queryInfo) {

    LinkedList<OEntity> l = new LinkedList<OEntity>();
    for (int i = 0; i < nDirs; i++) {
      getDirectory(i, queryInfo);
    }
    l.addAll(dirs.values());
    return l;
  }

  @Override
  public CountResponse getNavPropertyCount(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
    throw new NotImplementedException();
  }

  private boolean isExpanded(String navprop, QueryInfo q) {
    if (q == null || q.expand == null) {
      return false;
    }
    PropertyPathHelper h = new PropertyPathHelper(q.select, q.expand);
    return h.isExpanded(navprop);
  }

  private OEntity getDirectory(int n, QueryInfo queryInfo) {

    String name = "Dir-" + Integer.toString(n);

    List<OProperty<?>> props = new ArrayList<OProperty<?>>(2);
    props.add(OProperties.string("Name", name));
    props.add(OProperties.string("DirProp1", name + "-DirProp1Value"));
    List<OLink> links = new ArrayList<OLink>();

    List<OEntity> items = null;

    if (isExpanded("Items", queryInfo)) {
      items = getFiles(name, queryInfo);
      int subdir = n * 2 + 1;
      items.add(getDirectory(subdir, null));
      items.add(getDirectory(subdir + 1, null));
      links.add(OLinks.relatedEntitiesInline("Items", "Items", null, items));
    } else {
      links.add(OLinks.relatedEntities("Items", "Items", null));
    }

    if (isExpanded("NewestItem", queryInfo)) {
      links.add(OLinks.relatedEntityInline("NewestItem", "NewestItem", null, items.get(0)));
    } else {
      links.add(OLinks.relatedEntity("NewestItem", "NewestItem", null));
    }

    OEntity e = OEntities.create(
        edm.findEdmEntitySet("Directories"),
        OEntityKey.create("Name", name),
        props,
        links == null ? new ArrayList<OLink>() : links);
    dirs.put(name, e);
    return e;
  }

  private List<OEntity> getFileSystemItems(QueryInfo queryInfo) {
    List<OEntity> l = new ArrayList<OEntity>();
    for (OEntity dir : this.getDirectories(queryInfo)) {
      l.add(dir);
      l.addAll(getFiles(dir.getProperty("Name", String.class).getValue(), queryInfo));
    }
    return l;
  }

  private List<OEntity> getFiles(String dirName, QueryInfo queryInfo) {
    EdmEntitySet fileSet = edm.findEdmEntitySet("Files");
    List<OEntity> l = new ArrayList<OEntity>();
    for (int i = 0; i < 3; i++) {
      List<OProperty<?>> props = new ArrayList<OProperty<?>>(2);
      String name = "File-" + Integer.toString(i) + "-" + dirName;
      props.add(OProperties.string("Name", name));
      props.add(OProperties.string("FileProp1", name + "-FileProp1Value"));
      l.add(OEntities.create(
          fileSet,
          OEntityKey.create("Name", name),
          props,
          new ArrayList<OLink>()));
    }
    return l;
  }

  private List<OEntity> getFiles(QueryInfo queryInfo) {
    List<OEntity> l = new ArrayList<OEntity>();
    for (OEntity dir : this.getDirectories(queryInfo)) {
      l.addAll(getFiles(dir.getProperty("Name", String.class).getValue(), queryInfo));
    }
    return l;
  }

  private List<OEntity> getType1s() {
    List<OEntity> l = new ArrayList<OEntity>(3);
    for (int i = 0; i < 3; i++) {
      l.add(getType1(0));
    }
    return l;
  }

  private OEntity getType1(int i) {
    List<OProperty<?>> props = new ArrayList<OProperty<?>>(3);
    String id = Integer.toString(i);
    props.add(OProperties.string("Id", id));

    Builder<OObject> builder = OCollections.newBuilder(EdmSimpleType.STRING);
    props.add(OProperties.collection("EmptyStrings", new EdmCollectionType(CollectionKind.Bag, EdmSimpleType.STRING), builder.build()));

    builder = OCollections.newBuilder(EdmSimpleType.STRING);
    for (int j = 0; j < 3; j++) {
      builder.add(OSimpleObjects.create(EdmSimpleType.STRING, "bagstring-" + j));
    }
    props.add(OProperties.collection("BagOStrings", new EdmCollectionType(CollectionKind.Bag, EdmSimpleType.STRING), builder.build()));

    builder = OCollections.newBuilder(EdmSimpleType.STRING);
    for (int j = 0; j < 5; j++) {
      builder.add(OSimpleObjects.create(EdmSimpleType.STRING, "liststring-" + j));
    }
    props.add(OProperties.collection("ListOStrings", new EdmCollectionType(CollectionKind.List, EdmSimpleType.STRING), builder.build()));

    builder = OCollections.newBuilder(EdmSimpleType.INT32);
    for (int j = 0; j < 5; j++) {
      builder.add(OSimpleObjects.create(EdmSimpleType.INT32, j));
    }
    props.add(OProperties.collection("BagOInts", new EdmCollectionType(CollectionKind.List, EdmSimpleType.INT32), builder.build()));

    EdmComplexType ct1 = this.getMetadata().findEdmComplexType("myns.ComplexType1");
    OComplexObject.Builder cb = OComplexObjects.newBuilder(ct1);
    cb.add(OProperties.string("Prop1", "Val1")).add(OProperties.string("Prop2", "Val2"));
    // hmmh, I swear I put a form of OProperties.complex that took an OComplexObject....
    props.add(OProperties.complex("Complex1", ct1, cb.build().getProperties()));

    builder = OCollections.newBuilder(ct1);
    for (int j = 0; j < 2; j++) {
      cb = OComplexObjects.newBuilder(ct1);
      cb.add(OProperties.string("Prop1", "Val" + j)).add(OProperties.string("Prop2", "Val" + j));

      builder.add(cb.build());
    }
    props.add(OProperties.collection("ListOComplex", new EdmCollectionType(CollectionKind.List, ct1), builder.build()));

    return OEntities.create(
        edm.findEdmEntitySet("Type1s"),
        OEntityKey.create("Id", id),
        props,
        new ArrayList<OLink>());
  }

  public OEntity getFileSystemItem(OEntityKey entityKey, QueryInfo queryInfo) {
    List<OEntity> es = this.getFileSystemItems(queryInfo);
    for (OEntity e : es) {
      if (e.getEntityKey().equals(entityKey)) {
        return e;
      }
    }
    throw new NotFoundException("nope");
  }

  public OEntity getMLE(OEntityKey entityKey, QueryInfo queryInfo) {

    String id = entityKey.asSingleValue().toString();
    if (!mediaResources.containsKey(id)) {
      throw new NotFoundException("can't find MLE with id: " + id);
    }
    return getMLE(getMetadata().findEdmEntitySet("MLEs"), id, mediaResources.get(id));
  }

  @Override
  public EntityResponse getEntity(ODataContext context, String entitySetName, OEntityKey entityKey, EntityQueryInfo queryInfo) {
    if (entitySetName.equals("Type1s")) {
      return Responses.entity(getType1(Integer.parseInt((String) entityKey.asSingleValue())));
    }
    if (entitySetName.equals("FileSystemItems")) {
      return Responses.entity(getFileSystemItem(entityKey, queryInfo));
    } else if (entitySetName.equals("MLEs")) {
      return Responses.entity(getMLE(entityKey, queryInfo));
    } else {
      throw new NotFoundException("Unknown entity set: " + entitySetName);
    }
  }

  @Override
  public BaseResponse getNavProperty(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
    throw new NotImplementedException();
  }

  @Override
  public void close() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public EntityResponse createEntity(ODataContext context, String entitySetName, OEntity entity) {
    if (entitySetName.equals("MLEs")) {
      // just got done uploading a blob
      return Responses.entity(entity);
    } else {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  @Override
  public EntityResponse createEntity(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, OEntity entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void deleteEntity(ODataContext context, String entitySetName, OEntityKey entityKey) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void mergeEntity(ODataContext context, String entitySetName, OEntity entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void updateEntity(ODataContext context, String entitySetName, OEntity entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public EntityIdResponse getLinks(ODataContext context, OEntityId sourceEntity, String targetNavProp) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void createLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityId targetEntity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void updateLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityKey oldTargetEntityKey, OEntityId newTargetEntity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void deleteLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityKey targetEntityKey) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public BaseResponse callFunction(ODataContext context, EdmFunctionImport name, Map<String, OFunctionParameter> params, QueryInfo queryInfo) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public <TExtension extends OExtension<ODataProducer>> TExtension findExtension(Class<TExtension> clazz) {
    if (clazz.equals(OMediaLinkExtensions.class))
      return clazz.cast(extensionFactory);
    return null;
  }

  public OMediaLinkExtensions extensionFactory = new MediaLinkExtensionFactory();
  private final Map<String, String> mediaResources = new HashMap<String, String>();

  public void initResources() {
    mediaResources.put("foobar", "here we have some content for the mle with id: ('foobar')");
    mediaResources.put("blatfoo", "please delete this useless mle asap...");
  }

  protected OEntity getMLE(EdmEntitySet entitySet, String id, String content) {
    List<OProperty<?>> props = new ArrayList<OProperty<?>>();
    props.add(OProperties.string("MLEProp1", "content length is " + content.length()));
    return OEntities.create(entitySet, OEntityKey.create("Id", id), props, Collections.<OLink> emptyList());
  }

  private class MediaLinkExtensionFactory implements OMediaLinkExtensions {

    @Override
    public OMediaLinkExtension create(ODataContext context) {
      return new MediaLinkExtension();
    }

  }

  private class MediaLinkExtension implements OMediaLinkExtension {

    @Override
    public InputStream getInputStreamForMediaLinkEntry(ODataContext odataContext, OEntity mle, String etag, EntityQueryInfo query) {

      if (null == odataContext) {
        throw new IllegalArgumentException("no odataContext?");
      }

      String id = mle.getEntityKey().asSingleValue().toString();
      String content = mediaResources.get(id); //  "here we have some content for the mle with id: " +;
      if (content == null) {
        throw new NotFoundException();
      }
      return new ByteArrayInputStream(content.getBytes());
    }

    @Override
    public String getMediaLinkContentType(ODataContext odataContext, OEntity mle) {
      if (null == odataContext) {
        throw new IllegalArgumentException("no odataContext?");
      }

      return "text/plain";
    }

    @Override
    public OutputStream getOutputStreamForMediaLinkEntryCreate(ODataContext odataContext, OEntity mle, String etag, QueryInfo query) {
      if (null == odataContext) {
        throw new IllegalArgumentException("no odataContext?");
      }

      // create:
      String id = mle.getEntityKey().asSingleValue().toString();
      if (mediaResources.containsKey(id)) {
        throw new BadRequestException("MLE with id: " + id + " already exists");
      }
      return new MRStream(id);
    }

    @Override
    public OutputStream getOutputStreamForMediaLinkEntryUpdate(ODataContext odataContext, OEntity mle, String etag, QueryInfo query) {
      if (null == odataContext) {
        throw new IllegalArgumentException("no odataContext?");
      }

      String id = mle.getEntityKey().asSingleValue().toString();
      if (!mediaResources.containsKey(id)) {
        throw new NotFoundException("MLE with id: " + id + " not found");
      }
      return new MRStream(id);
    }

    @Override
    public OEntity getMediaLinkEntryForUpdateOrDelete(ODataContext odataContext, EdmEntitySet entitySet, OEntityKey key, HttpHeaders httpHeaders) {
      if (null == odataContext) {
        throw new IllegalArgumentException("no odataContext?");
      }

      String id = key.asSingleValue().toString();
      if (!mediaResources.containsKey(id)) {
        throw new NotFoundException("MLE with id: " + id + " not found");
      }
      return getMLE(entitySet, id, mediaResources.get(id));
    }

    @Override
    public String getMediaLinkContentDisposition(ODataContext odataContext, OEntity mle) {
      if (null == odataContext) {
        throw new IllegalArgumentException("no odataContext?");
      }

      return "inline";
    }

    private class MRStream extends ByteArrayOutputStream {
      public MRStream(String id) {
        this.id = id;
      }

      @Override
      public void close() throws IOException {
        super.close();
        mediaResources.put(id, toString());
      }

      private String id;
    }

    @Override
    public void deleteStream(ODataContext odataContext, OEntity mle, QueryInfo query) {
      if (null == odataContext) {
        throw new IllegalArgumentException("no odataContext?");
      }

      String id = mle.getEntityKey().asSingleValue().toString();
      if (mediaResources.containsKey(id)) {
        mediaResources.remove(id);
      } else {
        throw new NotFoundException("MLE with id: " + id + " not found");
      }
    }

    @Override
    public OEntity createMediaLinkEntry(ODataContext odataContext, EdmEntitySet entitySet, HttpHeaders httpHeaders) {
      if (null == odataContext) {
        throw new IllegalArgumentException("no odataContext?");
      }

      List<String> slugs = httpHeaders.getRequestHeader("Slug");
      if (slugs == null || slugs.isEmpty()) {
        throw new BadRequestException("missing Slug header");
      }

      // slug is the id
      List<OProperty<?>> props = new ArrayList<OProperty<?>>();
      props.add(OProperties.string("MLEProp1", "prop1 initial value"));
      return OEntities.create(entitySet, OEntityKey.create("Id", slugs.get(0)), props, Collections.<OLink> emptyList());
    }

    @Override
    public OEntity updateMediaLinkEntry(ODataContext odataContext, OEntity mle, OutputStream outStream) {
      if (null == odataContext) {
        throw new IllegalArgumentException("no odataContext?");
      }

      // sometimes after processing the blob we know more about the entity...
      String id = mle.getEntityKey().asSingleValue().toString();
      return getMLE(mle.getEntitySet(), id, mediaResources.get(id));
    }
  }
}
