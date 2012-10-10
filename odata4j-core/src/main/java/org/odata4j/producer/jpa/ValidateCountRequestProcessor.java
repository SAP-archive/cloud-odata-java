package org.odata4j.producer.jpa;

import org.odata4j.producer.InlineCount;
import org.odata4j.producer.QueryInfo;

public class ValidateCountRequestProcessor implements Command {

  @Override
  public boolean execute(JPAContext context) {
    // inlineCount is not applicable to $count queries
    QueryInfo query = context.getQueryInfo();
    if (query != null && query.inlineCount == InlineCount.ALLPAGES) {
      throw new UnsupportedOperationException(
          "$inlinecount cannot be applied to the resource segment '$count'");
    }

    // sktiptoken is not applicable to $count queries
    if (query != null && query.skipToken != null) {
      throw new UnsupportedOperationException(
          "Skip tokens can only be provided for contexts that return collections of entities.");
    }

    return false;
  }
}