package org.odata4j.producer.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.metamodel.EntityType;

import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmPropertyBase;
import org.odata4j.producer.BaseResponse;
import org.odata4j.producer.QueryInfo;

public class JPAContext implements Context {

  private EdmDataServices metadata;
  private EntityManager em;
  private EntityTransaction tx;

  private ContextEntity entity;
  private ContextEntity otherEntity;

  private String navProperty;

  private QueryInfo queryInfo;

  private String jpqlQuery;
  private EdmPropertyBase edmPropertyBase;

  private JPAResult result;

  private BaseResponse response;

  // update, merge, delete
  protected JPAContext(EdmDataServices metadata, String entitySetName,
      OEntityKey oEntityKey, OEntity oEntity) {
    this.metadata = metadata;
    this.entity = new ContextEntity(entitySetName, oEntityKey, oEntity);
  }

  // create
  public JPAContext(EdmDataServices metadata, String entitySetName,
      OEntityKey oEntityKey, String navProperty, OEntity oEntity) {
    this.metadata = metadata;
    this.entity = new ContextEntity(entitySetName, oEntityKey, null);
    this.navProperty = navProperty;
    this.otherEntity = new ContextEntity(oEntity.getEntitySetName(), oEntity.getEntityKey(), oEntity);
  }

  // query
  public JPAContext(EdmDataServices metadata, String entitySetName,
      QueryInfo queryInfo) {
    this.metadata = metadata;
    this.entity = new ContextEntity(entitySetName, null, null);
    this.queryInfo = queryInfo;
  }

  // get entity / with nav property (count?)
  public JPAContext(EdmDataServices metadata, String entitySetName,
      OEntityKey oEntityKey, String navProperty, QueryInfo queryInfo) {
    this.metadata = metadata;
    this.entity = new ContextEntity(entitySetName, oEntityKey, null);
    this.navProperty = navProperty;
    this.queryInfo = queryInfo;
  }

  public EdmDataServices getMetadata() {
    return metadata;
  }

  public EntityManager getEntityManager() {
    return em;
  }

  public void setEntityManager(EntityManager em) {
    this.em = em;
  }

  public EntityTransaction getEntityTransaction() {
    return tx;
  }

  public void setEntityTransaction(EntityTransaction tx) {
    this.tx = tx;
  }

  public ContextEntity getEntity() {
    return entity;
  }

  public ContextEntity getOtherEntity() {
    return otherEntity;
  }

  public String getNavProperty() {
    return navProperty;
  }

  public QueryInfo getQueryInfo() {
    return queryInfo;
  }

  public String getJPQLQuery() {
    return jpqlQuery;
  }

  public void setJPQLQuery(String jpqlQuery) {
    this.jpqlQuery = jpqlQuery;
  }

  public EdmPropertyBase getEdmPropertyBase() {
    return edmPropertyBase;
  }

  public void setEdmPropertyBase(EdmPropertyBase edmPropertyBase) {
    this.edmPropertyBase = edmPropertyBase;
  }

  public JPAResult getResult() {
    return result;
  }

  public void setResult(JPAResult result) {
    this.result = result;
  }

  public BaseResponse getResponse() {
    return response;
  }

  public void setResponse(BaseResponse response) {
    this.response = response;
  }

  public class ContextEntity {
    private String entitySetName;
    private OEntityKey oEntityKey;
    private OEntity oEntity;

    private EdmEntitySet ees;
    private EntityType<?> jpaEntityType;
    private String keyAttributeName;
    private Object jpaEntity;

    public ContextEntity(String entitySetName, OEntityKey oEntityKey,
        OEntity oEntity) {
      this.entitySetName = entitySetName;
      this.oEntityKey = oEntityKey;
      this.oEntity = oEntity;
    }

    public String getEntitySetName() {
      return entitySetName;
    }

    public void setEntitySetName(String entitySetName) {
      this.entitySetName = entitySetName;
      this.jpaEntityType = null;
      this.ees = null;
      this.keyAttributeName = null;
      this.oEntityKey = null;
    }

    public void setOEntityKey(OEntityKey oEntityKey) {
      this.oEntityKey = oEntityKey;
    }

    public EntityType<?> getJPAEntityType() {
      if (jpaEntityType == null) {

        jpaEntityType = JPAProducer.getJPAEntityType(em,
            getEdmEntitySet()
                .getType().getName());
      }
      return jpaEntityType;
    }

    public EdmEntitySet getEdmEntitySet() {
      if (ees == null) {
        ees = getMetadata().getEdmEntitySet(getEntitySetName());
      }
      return ees;
    }

    public String getKeyAttributeName() {
      if (keyAttributeName == null) {
        keyAttributeName = JPAEdmGenerator
            .getIdAttribute(getJPAEntityType()).getName();
      }
      return keyAttributeName;
    }

    public Object getTypeSafeEntityKey() {
      return JPAProducer.typeSafeEntityKey(
          getEntityManager(),
          getJPAEntityType(),
          oEntityKey);
    }

    public Object getJpaEntity() {
      return jpaEntity;
    }

    public void setJpaEntity(Object jpaEntity) {
      this.jpaEntity = jpaEntity;
    }

    public OEntity getOEntity() {
      return oEntity;
    }

    public void setOEntity(OEntity oEntity) {
      this.oEntity = oEntity;
      setEntitySetName(oEntity.getEntitySetName());
      this.oEntityKey = oEntity != null ? oEntity.getEntityKey() : null;
    }
  }

  public static abstract class EntityAccessor {
    public abstract ContextEntity getEntity(JPAContext context);

    public abstract void setJPAEntity(JPAContext context, Object jpaEntity);

    public static final JPAContext.EntityAccessor ENTITY = new EntityAccessor() {

      @Override
      public ContextEntity getEntity(JPAContext context) {
        return context.getEntity();
      }

      @Override
      public void setJPAEntity(JPAContext context, Object jpaEntity) {
        context.getEntity().setJpaEntity(jpaEntity);
      }
    };

    public static final JPAContext.EntityAccessor OTHER = new EntityAccessor() {

      @Override
      public ContextEntity getEntity(JPAContext context) {
        return context.getOtherEntity();
      }

      @Override
      public void setJPAEntity(JPAContext context, Object jpaEntity) {
        context.getOtherEntity().setJpaEntity(jpaEntity);
      }
    };
  }
}