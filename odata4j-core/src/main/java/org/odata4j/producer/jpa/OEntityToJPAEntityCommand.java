package org.odata4j.producer.jpa;

public class OEntityToJPAEntityCommand implements Command {

  private boolean withLinks;
  private JPAContext.EntityAccessor accessor;

  public OEntityToJPAEntityCommand(boolean withLinks) {
    this(JPAContext.EntityAccessor.ENTITY, withLinks);
  }

  public OEntityToJPAEntityCommand(JPAContext.EntityAccessor accessor,
      boolean withLinks) {
    this.accessor = accessor;
    this.withLinks = withLinks;
  }

  @Override
  public boolean execute(JPAContext context) {

    Object jpaEntity = JPAProducer.createNewJPAEntity(
        context.getEntityManager(),
        accessor.getEntity(context).getJPAEntityType(),
        accessor.getEntity(context).getOEntity(),
        withLinks);
    accessor.getEntity(context).setJpaEntity(jpaEntity);

    return false;
  }
}