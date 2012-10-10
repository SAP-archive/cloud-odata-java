package org.odata4j.core;

import java.util.List;

/**
 * A static factory to create immutable {@link OLink} instances.
 */
public class OLinks {

  private OLinks() {}

  /**
   * Creates a new {@link OLink} of sub-type {@link ORelatedEntitiesLink}.
   */
  public static ORelatedEntitiesLink relatedEntities(final String relation, final String title, final String href) {
    return new ORelatedEntitiesLinkImpl(relation, title, href);
  }

  /**
   * Creates a new {@link OLink} of sub-type {@link ORelatedEntitiesLinkInline}.
   */
  public static ORelatedEntitiesLinkInline relatedEntitiesInline(final String relation, final String title, final String href, final List<OEntity> relatedEntities) {
    return new ORelatedEntitiesLinkInlineImpl(relation, title, href, relatedEntities);
  }

  /**
   * Creates a new {@link OLink} of sub-type {@link ORelatedEntityLink}.
   */
  public static ORelatedEntityLink relatedEntity(final String relation, final String title, final String href) {
    return new ORelatedEntityLinkImpl(relation, title, href);
  }

  /**
   * Creates a new {@link OLink} of sub-type {@link ORelatedEntityLinkInline}.
   */
  public static ORelatedEntityLinkInline relatedEntityInline(final String relation, final String title, final String href, final OEntity relatedEntity) {
    return new ORelatedEntityLinkInlineImpl(relation, title, href, relatedEntity);
  }

  private static abstract class OLinkImpl implements OLink {
    private final Class<?> interfaceType;

    private final String title;
    private final String relation;
    private final String href;

    public OLinkImpl(Class<?> interfaceType, String relation, String title, String href) {
      this.interfaceType = interfaceType;
      this.title = title;
      this.relation = relation;
      this.href = href;
    }

    @Override
    public String getTitle() {
      return title;
    }

    @Override
    public String getRelation() {
      return relation;
    }

    @Override
    public String getHref() {
      return href;
    }

    @Override
    public String toString() {
      return String.format("%s[rel=%s,title=%s,href=%s]", interfaceType.getSimpleName(), relation, title, href);
    }

  }

  private static class ORelatedEntitiesLinkImpl extends OLinkImpl implements ORelatedEntitiesLink {
    public ORelatedEntitiesLinkImpl(String relation, String title, String href) {
      super(ORelatedEntitiesLink.class, relation, title, href);
    }

    @Override
    public boolean isInline() {
      return false;
    }

    @Override
    public boolean isCollection() {
      return true;
    }

    @Override
    public OEntity getRelatedEntity() {
      return null;
    }

    @Override
    public List<OEntity> getRelatedEntities() {
      return null;
    }
  }

  private static class ORelatedEntitiesLinkInlineImpl extends OLinkImpl implements ORelatedEntitiesLinkInline {
    private final List<OEntity> relatedEntities;

    public ORelatedEntitiesLinkInlineImpl(String relation, String title, String href, List<OEntity> relatedEntities) {
      super(ORelatedEntitiesLinkInline.class, relation, title, href);
      this.relatedEntities = relatedEntities;
    }

    @Override
    public List<OEntity> getRelatedEntities() {
      return relatedEntities;
    }

    @Override
    public boolean isInline() {
      return true;
    }

    @Override
    public boolean isCollection() {
      return true;
    }

    @Override
    public OEntity getRelatedEntity() {
      return null;
    }
  }

  private static class ORelatedEntityLinkImpl extends OLinkImpl implements ORelatedEntityLink {
    public ORelatedEntityLinkImpl(String relation, String title, String href) {
      super(ORelatedEntityLink.class, relation, title, href);
    }

    @Override
    public boolean isInline() {
      return false;
    }

    @Override
    public boolean isCollection() {
      return false;
    }

    @Override
    public OEntity getRelatedEntity() {
      return null;
    }

    @Override
    public List<OEntity> getRelatedEntities() {
      return null;
    }
  }

  private static class ORelatedEntityLinkInlineImpl extends OLinkImpl implements ORelatedEntityLinkInline {
    private final OEntity relatedEntity;

    public ORelatedEntityLinkInlineImpl(String relation, String title, String href, OEntity relatedEntity) {
      super(ORelatedEntityLinkInline.class, relation, title, href);
      this.relatedEntity = relatedEntity;
    }

    @Override
    public OEntity getRelatedEntity() {
      return relatedEntity;
    }

    @Override
    public boolean isInline() {
      return true;
    }

    @Override
    public boolean isCollection() {
      return false;
    }

    @Override
    public List<OEntity> getRelatedEntities() {
      return null;
    }

  }

}
