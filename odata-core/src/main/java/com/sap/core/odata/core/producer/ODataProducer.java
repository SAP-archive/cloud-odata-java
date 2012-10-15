package com.sap.core.odata.core.producer;

public abstract class ODataProducer {

  public Metadata getMetadata() {
    Metadata metadata;

    if (this instanceof Metadata) {
      metadata = (Metadata) this;
    } else {
      throw new RuntimeException("Metadata must be supported!");
    }

    return metadata;
  }

  public final boolean isBatch() {
    return null != this.getBatch();
  }

  public Batch getBatch() {
    Batch batch = null;

    if (this instanceof Batch) {
      batch = (Batch) this;
    }
    return batch;
  }

  public final boolean isEntity() {
    return null != this.getEntity();
  }

  public Entity getEntity() {
    Entity entity = null;

    if (this instanceof Entity) {
      entity = (Entity) this;
    }
    return entity;
  }

  public final boolean isEntityComplexProperties() {
    return null != this.getEntityComplexProperties();
  }

  public EntityComplexProperties getEntityComplexProperties() {
    EntityComplexProperties entityComplexProperties = null;

    if (this instanceof EntityComplexProperties) {
      entityComplexProperties = (EntityComplexProperties) this;
    }
    return entityComplexProperties;
  }

  public final boolean isEntityLink() {
    return null != this.getEntityLink();
  }

  public EntityLink getEntityLink() {
    EntityLink entityLink = null;

    if (this instanceof EntityLink) {
      entityLink = (EntityLink) this;
    }
    return entityLink;
  }

  public final boolean isEntityLinks() {
    return null != this.getEntityLinks();
  }

  public EntityLinks getEntityLinks() {
    EntityLinks entityLinks = null;

    if (this instanceof EntityLinks) {
      entityLinks = (EntityLinks) this;
    }
    return entityLinks;
  }

  public final boolean isEntityMedia() {
    return null != this.getEntityMedia();
  }

  public EntityMedia getEntityMedia() {
    EntityMedia entityMedia = null;

    if (this instanceof EntityMedia) {
      entityMedia = (EntityMedia) this;
    }
    return entityMedia;
  }

  public final boolean isEntitySet() {
    return null != this.getEntitySet();
  }

  public EntitySet getEntitySet() {
    EntitySet entitySet = null;

    if (this instanceof EntitySet) {
      entitySet = (EntitySet) this;
    }
    return entitySet;
  }

  public final boolean isEntitySimpleProperty() {
    return null != this.getEntitySimpleProperty();
  }

  public EntitySimpleProperty getEntitySimpleProperty() {
    EntitySimpleProperty entitySimpleProperty = null;

    if (this instanceof EntitySimpleProperty) {
      entitySimpleProperty = (EntitySimpleProperty) this;
    }
    return entitySimpleProperty;
  }

  public final boolean isEntityValueProperties() {
    return null != this.getEntityValueProperties();
  }

  public EntityValueProperties getEntityValueProperties() {
    EntityValueProperties entityValueProperties = null;

    if (this instanceof EntityValueProperties) {
      entityValueProperties = (EntityValueProperties) this;
    }
    return entityValueProperties;
  }

  public final boolean isFunctionImport() {
    return null != this.getFunctionImport();
  }

  public FunctionImport getFunctionImport() {
    FunctionImport functionImport = null;

    if (this instanceof FunctionImport) {
      functionImport = (FunctionImport) this;
    }
    return functionImport;
  }
}
