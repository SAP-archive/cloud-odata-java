package org.odata4j.producer.jpa;

import javax.persistence.metamodel.EntityType;

import org.odata4j.exceptions.NotFoundException;
import org.odata4j.producer.jpa.JPAContext.EntityAccessor;

public class GetEntityCommand implements Command {

  private EntityAccessor accessor;

  public GetEntityCommand() {
    this(EntityAccessor.ENTITY);
  }

  public GetEntityCommand(EntityAccessor accessor) {
    this.accessor = accessor;
  }

  @Override
  public boolean execute(JPAContext context) {

    EntityType<?> jpaEntityType = accessor.getEntity(context)
        .getJPAEntityType();
    Object typeSafeEntityKey = accessor.getEntity(context)
        .getTypeSafeEntityKey();
    Object jpaEntity = context.getEntityManager().find(
        jpaEntityType.getJavaType(), typeSafeEntityKey);

    if (jpaEntity == null) {
      throw new NotFoundException(jpaEntityType
          .getJavaType()
          + " not found with key "
          + typeSafeEntityKey);
    }

    accessor.getEntity(context).setJpaEntity(jpaEntity);

    context.setResult(JPAResults.entity(accessor
        .getEntity(context).getJpaEntity()));

    return false;
  }
}