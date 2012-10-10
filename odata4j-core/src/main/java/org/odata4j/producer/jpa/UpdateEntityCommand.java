package org.odata4j.producer.jpa;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.odata4j.core.OEntity;

public class UpdateEntityCommand implements Command {

  @Override
  public boolean execute(JPAContext context) {
    EntityManager em = context.getEntityManager();
    EntityType<?> jpaEntityType = context.getEntity()
        .getJPAEntityType();
    Object jpaEntity = context.getEntity().getJpaEntity();
    OEntity entity = context.getEntity().getOEntity();

    em.merge(jpaEntity);
    JPAProducer.applyOLinks(em, jpaEntityType, entity.getLinks(),
        jpaEntity);

    return false;
  }
}