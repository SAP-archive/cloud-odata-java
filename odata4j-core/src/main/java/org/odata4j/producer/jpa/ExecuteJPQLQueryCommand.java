package org.odata4j.producer.jpa;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.odata4j.edm.EdmMultiplicity;
import org.odata4j.edm.EdmNavigationProperty;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.producer.InlineCount;

public class ExecuteJPQLQueryCommand implements Command {

  private int maxResults;

  public ExecuteJPQLQueryCommand(int maxResults) {
    this.maxResults = maxResults;
  }

  @Override
  public boolean execute(JPAContext context) {
    context.setResult(getEntitiesResponse(context));

    return false;
  }

  private JPAResult getEntitiesResponse(final JPAContext context) {

    // get the jpql
    String jpql = context.getJPQLQuery();

    // jpql -> jpa query
    Query tq = context.getEntityManager().createQuery(jpql);

    Integer inlineCount = context.getQueryInfo() != null
        && context.getQueryInfo().inlineCount == InlineCount.ALLPAGES
        ? tq.getResultList().size()
        : null;

    int queryMaxResults = maxResults;
    if (context.getQueryInfo() != null
        && context.getQueryInfo().top != null) {

      // top=0: don't even hit jpa, return a response with zero
      // entities
      if (context.getQueryInfo().top.equals(0)) {
        // returning null from this function would cause the
        // FormatWriters to throw
        // a null reference exception as the entities collection is
        // expected to be empty and
        // not null. This prevents us from being able to
        // successfully
        // respond to $top=0 contexts.
        List<Object> emptyList = Collections.emptyList();
        return JPAResults.entities(emptyList, inlineCount, false);
      }

      if (context.getQueryInfo().top < maxResults)
        queryMaxResults = context.getQueryInfo().top;
    }

    // jpa query for one more than specified to determine whether or not
    // to
    // return a skip token
    tq = tq.setMaxResults(queryMaxResults + 1);

    if (context.getQueryInfo() != null
        && context.getQueryInfo().skip != null)
      tq = tq.setFirstResult(context.getQueryInfo().skip);

    // execute jpa query
    @SuppressWarnings("unchecked")
    List<Object> results = tq.getResultList();

    // property response
    if (context.getEdmPropertyBase() instanceof EdmProperty) {
      EdmProperty propInfo = (EdmProperty) context
          .getEdmPropertyBase();

      if (results.size() != 1)
        throw new RuntimeException(
            "Expected one and only one result for property, found "
                + results.size());

      Object value = results.get(0);
      return JPAResults.property(((EdmProperty) propInfo).getName(),
          (EdmSimpleType<?>) ((EdmProperty) propInfo).getType(), value);
    }

    // compute skip token if necessary
    boolean hasMoreResults = context.getQueryInfo() != null
        && context.getQueryInfo().top != null
        ? context.getQueryInfo().top > maxResults
            && results.size() > queryMaxResults
        : results.size() > queryMaxResults;

    if (context.getEdmPropertyBase() instanceof EdmNavigationProperty) {
      EdmNavigationProperty edmNavProp = (EdmNavigationProperty) context
          .getEdmPropertyBase();
      if (edmNavProp.getToRole().getMultiplicity() == EdmMultiplicity.ONE
          || edmNavProp.getToRole().getMultiplicity() == EdmMultiplicity.ZERO_TO_ONE) {
        if (results.size() != 1)
          throw new RuntimeException(
              "Expected only one entity, found "
                  + results.size());

        return JPAResults.entity(results.get(0));
      }
    }

    return JPAResults.entities(results
        .subList(0, Math.min(queryMaxResults, results.size())),
        inlineCount, hasMoreResults);
  }

}