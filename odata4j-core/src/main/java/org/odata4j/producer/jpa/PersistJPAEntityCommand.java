package org.odata4j.producer.jpa;

public class PersistJPAEntityCommand implements Command {

  @Override
  public boolean execute(JPAContext context) {
    context.getEntityManager().persist(
        context.getEntity().getJpaEntity());

    return false;
  }
}