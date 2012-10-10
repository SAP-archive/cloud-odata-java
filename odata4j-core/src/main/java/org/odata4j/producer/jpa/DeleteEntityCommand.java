package org.odata4j.producer.jpa;

public class DeleteEntityCommand implements Command {

  @Override
  public boolean execute(JPAContext context) {
    context.getEntityManager().remove(context.getEntity().getJpaEntity());

    return false;
  }
}