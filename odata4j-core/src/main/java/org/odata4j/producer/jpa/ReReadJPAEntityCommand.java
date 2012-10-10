package org.odata4j.producer.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ReReadJPAEntityCommand implements Command {

  @Override
  public boolean execute(JPAContext context) {

    // reread the entity in case we had links. This should insure
    // we get the implicitly set foreign keys. E.g in the Northwind model
    // creating a new Product with a link to the Category should return
    // the CategoryID.
    EntityManager em = context.getEntityManager();
    if (context.getEntity().getOEntity().getLinks() != null
        && !context.getEntity().getOEntity().getLinks().isEmpty()) {

      EntityTransaction tx = em.getTransaction();
      tx.begin();
      try {
        context.getEntityManager().refresh(
            context.getEntity().getJpaEntity());
        tx.commit();
      } finally {
        if (tx.isActive()) {
          tx.rollback();
        }
      }
    }

    context.setResult(JPAResults
        .entity(context.getEntity().getJpaEntity()));

    return false;
  }
}