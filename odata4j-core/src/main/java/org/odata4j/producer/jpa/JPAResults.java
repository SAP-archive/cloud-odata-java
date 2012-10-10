package org.odata4j.producer.jpa;

import java.util.List;

import org.odata4j.edm.EdmSimpleType;

public class JPAResults {

  public static CountResult count(final long count) {
    return new CountResult() {
      @Override
      public long getCount() {
        return count;
      }
    };
  }

  public static <T> PropertyResult<T> property(final String name,
      final EdmSimpleType<T> type, final Object value) {
    return new PropertyResult<T>() {
      @Override
      public String getName() {
        return name;
      }

      @Override
      public EdmSimpleType<T> getType() {
        return type;
      }

      @Override
      public Object getValue() {
        return value;
      }
    };
  }

  public static EntityResult entity(final Object entity) {
    return new EntityResult() {
      @Override
      public Object getEntity() {
        return entity;
      }
    };
  }

  public static EntitiesResult entities(final List<Object> entities,
      final Integer inlineCount, final boolean createSkipToken) {
    return new EntitiesResult() {
      @Override
      public List<Object> getEntities() {
        return entities;
      }

      @Override
      public Integer getInlineCount() {
        return inlineCount;
      }

      @Override
      public boolean createSkipToken() {
        return createSkipToken;
      }

    };
  }
}
