package org.odata4j.producer;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.odata4j.expression.BoolCommonExpression;
import org.odata4j.expression.EntitySimpleProperty;
import org.odata4j.expression.OrderByExpression;

/**
 * <code>QueryInfo</code> represents an OData multiple-entity query as a strongly-typed immutable data structure.
 */
public class QueryInfo {

  /**
   * The $inlinecount value, if present.
   *
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#InlinecountSystemQueryOption">[odata.org] Inlinecount System Query Option ($inlinecount)</a>
   */
  public final InlineCount inlineCount;

  /**
   * The number of items to return, if present.
   *
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#TopSystemQueryOption">[odata.org] Top System Query Option ($top)</a>
   */
  public final Integer top;

  /**
   * The number of items to skip, if present.
   *
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#SkipSystemQueryOption">[odata.org] Skip System Query Option ($skip)</a>
   */
  public final Integer skip;

  /**
   * The filter expression to apply, if present.
   *
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#FilterSystemQueryOption">[odata.org] Filter System Query Option ($filter)</a>
   */
  public final BoolCommonExpression filter;

  /**
   * The ordering expressions to apply, if present.
   *
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#OrderBySystemQueryOption">[odata.org] Orderby System Query Option ($orderby)</a>
   */
  public final List<OrderByExpression> orderBy;

  /**
   * The continuation token to use as a starting point, if present.
   */
  public final String skipToken;

  /**
   * Custom name-value pairs, if present.
   *
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#CustomQueryOptions">[odata.org] Custom Query Options</a>
   */
  public final Map<String, String> customOptions;

  /**
   * Expand expressions, if present.
   *
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#ExpandSystemQueryOption">[odata.org] Expand System Query Option ($expand)</a>
   */
  public final List<EntitySimpleProperty> expand;

  /**
   * Selection clauses, if present.
   *
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#SelectSystemQueryOption">[odata.org] Select System Query Option ($select)</a>
   */
  public final List<EntitySimpleProperty> select;

  public QueryInfo() {
    this.inlineCount = null;
    this.top = null;
    this.skip = null;
    this.filter = null;
    this.orderBy = null;
    this.skipToken = null;
    this.select = Collections.<EntitySimpleProperty> emptyList();
    this.expand = Collections.<EntitySimpleProperty> emptyList();
    this.customOptions = Collections.<String, String> emptyMap();
  }

  /**
   * Creates a new <code>QueryInfo</code> instance.
   */
  public QueryInfo(
      InlineCount inlineCount,
      Integer top,
      Integer skip,
      BoolCommonExpression filter,
      List<OrderByExpression> orderBy,
      String skipToken,
      Map<String, String> customOptions,
      List<EntitySimpleProperty> expand,
      List<EntitySimpleProperty> select) {
    this.inlineCount = inlineCount;
    this.top = top;
    this.skip = skip;
    this.filter = filter;
    this.orderBy = orderBy;
    this.skipToken = skipToken;
    this.customOptions = customOptions == null ? Collections.<String, String> emptyMap() : Collections.unmodifiableMap(customOptions);
    this.expand = expand == null ? Collections.<EntitySimpleProperty> emptyList() : Collections.unmodifiableList(expand);
    this.select = select == null ? Collections.<EntitySimpleProperty> emptyList() : Collections.unmodifiableList(select);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  /** Mutable builder for {@link QueryInfo} objects. */
  public static class Builder {
    private InlineCount inlineCount;
    private Integer top;
    private Integer skip;
    private BoolCommonExpression filter;
    private List<OrderByExpression> orderBy;
    private String skipToken;
    private Map<String, String> customOptions;
    private List<EntitySimpleProperty> expand;
    private List<EntitySimpleProperty> select;

    public Builder setInlineCount(InlineCount inlineCount) {
      this.inlineCount = inlineCount;
      return this;
    }

    public Builder setTop(Integer top) {
      this.top = top;
      return this;
    }

    public Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    public Builder setFilter(BoolCommonExpression filter) {
      this.filter = filter;
      return this;
    }

    public Builder setOrderBy(List<OrderByExpression> orderBy) {
      this.orderBy = orderBy;
      return this;
    }

    public Builder setSkipToken(String value) {
      this.skipToken = value;
      return this;
    }

    public Builder setCustomOptions(Map<String, String> value) {
      this.customOptions = value;
      return this;
    }

    public Builder setExpand(List<EntitySimpleProperty> value) {
      this.expand = value;
      return this;
    }

    public Builder setSelect(List<EntitySimpleProperty> value) {
      this.select = value;
      return this;
    }

    public QueryInfo build() {
      return new QueryInfo(inlineCount, top, skip, filter, orderBy, skipToken, customOptions, expand, select);
    }
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    boolean first = true;
    first = appendField(sb, "$inlinecount", inlineCount, first);
    first = appendField(sb, "$top", top, first);
    first = appendField(sb, "$skip", skip, first);
    first = appendField(sb, "$filter", filter, first);
    first = appendField(sb, "$orderby", orderBy, first);
    first = appendField(sb, "$skiptoken", skipToken, first);
    first = appendField(sb, "customOptions", customOptions, first);
    first = appendField(sb, "$expand", expand, first);
    first = appendField(sb, "$select", select, first);
    sb.append("]");
    return sb.toString();
  }

  private boolean appendField(StringBuffer sb, String name, Object field, boolean first) {
    if (field == null)
      return first;
    if (field instanceof List && ((List<?>) field).isEmpty())
      return first;
    if (field instanceof Map && ((Map<?, ?>) field).isEmpty())
      return first;
    if (first)
      first = false;
    else
      sb.append(",");
    sb.append(name + "=" + field);
    return first;
  }
}
