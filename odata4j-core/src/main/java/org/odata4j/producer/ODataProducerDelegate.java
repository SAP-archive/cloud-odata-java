package org.odata4j.producer;

import java.util.Map;

import org.odata4j.core.Delegate;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OExtension;
import org.odata4j.core.OFunctionParameter;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.producer.edm.MetadataProducer;

/** Abstract base {@link Delegate} for {@link ODataProducer}. */
public abstract class ODataProducerDelegate implements Delegate<ODataProducer>, ODataProducer {

  @Override
  public EdmDataServices getMetadata() {
    return getDelegate().getMetadata();
  }

  @Override
  public MetadataProducer getMetadataProducer() {
    return getDelegate().getMetadataProducer();
  }

  @Override
  public EntitiesResponse getEntities(ODataContext context, String entitySetName, QueryInfo queryInfo) {
    return getDelegate().getEntities(context, entitySetName, queryInfo);
  }

  @Override
  public CountResponse getEntitiesCount(ODataContext context, String entitySetName, QueryInfo queryInfo) {
    return getDelegate().getEntitiesCount(context, entitySetName, queryInfo);
  }

  @Override
  public EntityResponse getEntity(ODataContext context, String entitySetName, OEntityKey entityKey, EntityQueryInfo queryInfo) {
    return getDelegate().getEntity(context, entitySetName, entityKey, queryInfo);
  }

  @Override
  public BaseResponse getNavProperty(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
    return getDelegate().getNavProperty(context, entitySetName, entityKey, navProp, queryInfo);
  }

  @Override
  public CountResponse getNavPropertyCount(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
    return getDelegate().getNavPropertyCount(context, entitySetName, entityKey, navProp, queryInfo);
  }

  @Override
  public void close() {
    getDelegate().close();
  }

  @Override
  public EntityResponse createEntity(ODataContext context, String entitySetName, OEntity entity) {
    return getDelegate().createEntity(context, entitySetName, entity);
  }

  @Override
  public EntityResponse createEntity(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, OEntity entity) {
    return getDelegate().createEntity(context, entitySetName, entityKey, navProp, entity);
  }

  @Override
  public void deleteEntity(ODataContext context, String entitySetName, OEntityKey entityKey) {
    getDelegate().deleteEntity(context, entitySetName, entityKey);
  }

  @Override
  public void mergeEntity(ODataContext context, String entitySetName, OEntity entity) {
    getDelegate().mergeEntity(context, entitySetName, entity);
  }

  @Override
  public void updateEntity(ODataContext context, String entitySetName, OEntity entity) {
    getDelegate().updateEntity(context, entitySetName, entity);
  }

  @Override
  public EntityIdResponse getLinks(ODataContext context, OEntityId sourceEntity, String targetNavProp) {
    return getDelegate().getLinks(context, sourceEntity, targetNavProp);
  }

  @Override
  public void createLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityId targetEntity) {
    getDelegate().createLink(context, sourceEntity, targetNavProp, targetEntity);
  }

  @Override
  public void updateLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityKey oldTargetEntityKey, OEntityId newTargetEntity) {
    getDelegate().updateLink(context, sourceEntity, targetNavProp, oldTargetEntityKey, newTargetEntity);
  }

  @Override
  public void deleteLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityKey targetEntityKey) {
    getDelegate().deleteLink(context, sourceEntity, targetNavProp, targetEntityKey);
  }

  @Override
  public BaseResponse callFunction(ODataContext context, EdmFunctionImport name, Map<String, OFunctionParameter> params, QueryInfo queryInfo) {
    return getDelegate().callFunction(context, name, params, queryInfo);
  }

  @Override
  public <TExtension extends OExtension<ODataProducer>> TExtension findExtension(Class<TExtension> clazz) {
    return getDelegate().findExtension(clazz);
  }
}
