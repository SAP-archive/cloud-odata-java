package org.odata4j.producer.jpa;

import java.util.List;

public interface EntitiesResult extends JPAResult {
  public List<Object> getEntities();

  public Integer getInlineCount();

  public boolean createSkipToken();
}