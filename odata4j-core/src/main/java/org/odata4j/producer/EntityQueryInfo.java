package org.odata4j.producer;

import java.util.List;
import java.util.Map;

import org.odata4j.expression.BoolCommonExpression;
import org.odata4j.expression.EntitySimpleProperty;

/**
 * <code>QueryInfo</code> represents an OData single-entity query as a strongly-typed immutable data structure.
 *
 * The constructor for this subtype limits to only the query options applicable for single-entity queries.
 */
public class EntityQueryInfo extends QueryInfo {

  /**
   * Creates a new <code>EntityQueryInfo</code> instance.
   */
  public EntityQueryInfo(
      BoolCommonExpression filter,
      Map<String, String> customOptions,
      List<EntitySimpleProperty> expand,
      List<EntitySimpleProperty> select) {
    super(null, null, null, filter, null, null, customOptions, expand, select);
  }

  public static EntityQueryInfo.Builder newBuilder() {
    return new EntityQueryInfo.Builder();
  }

  /** Mutable builder for {@link EntityQueryInfo} objects. */
  public static class Builder extends QueryInfo.Builder {

    private BoolCommonExpression filter;

    private Map<String, String> customOptions;
    private List<EntitySimpleProperty> expand;
    private List<EntitySimpleProperty> select;

    public Builder setFilter(BoolCommonExpression filter) {
      this.filter = filter;
      return this;
    }

    public EntityQueryInfo build() {
      return new EntityQueryInfo(filter, customOptions, expand, select);
    }
  }

}
