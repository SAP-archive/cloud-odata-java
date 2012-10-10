package org.odata4j.core;

import java.util.Iterator;

import org.core4j.Enumerable;
import org.odata4j.exceptions.ODataProducerException;

/**
 * A consumer-side query-request builder.  Call {@link #execute()} to issue the request.
 *
 * @param <T>  the entity representation as a java type
 */
public interface OQueryRequest<T> {

  /**
   * Sends the query-request to the OData service, returning a client-side {@link Enumerable} for subsequent in-memory operations.
   * <p>The returned enumerable transparently handles server-side paging and deferred enumeration.</p>
   * <p>Subsequent server calls are handled within the enumerable's {@link Iterator}.</p>
   *
   * @return the response as a client-side enumerable
   * @throws ODataProducerException  error from the producer
   */
  Enumerable<T> execute() throws ODataProducerException;

  /**
   * Sets the number of items to return.
   *
   * @param top  the number of items to return
   * @return the query-request builder
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#TopSystemQueryOption">[odata.org] Top System Query Option ($top)</a>
   */
  OQueryRequest<T> top(int top);

  /**
   * Sets the number of items to skip.
   *
   * @param skip  the number of items to skip
   * @return the query-request builder
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#SkipSystemQueryOption">[odata.org] Skip System Query Option ($skip)</a>
   */
  OQueryRequest<T> skip(int skip);

  /**
   * Sets the ordering expressions.
   *
   * @param orderBy  the ordering expressions
   * @return the query-request builder
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#OrderBySystemQueryOption">[odata.org] Orderby System Query Option ($orderby)</a>
   */
  OQueryRequest<T> orderBy(String orderBy);

  /**
   * Sets the filter expression.
   *
   * @param filter  the filter expression
   * @return the query-request builder
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#FilterSystemQueryOption">[odata.org] Filter System Query Option ($filter)</a>
   */
  OQueryRequest<T> filter(String filter);

  /**
   * Sets the selection clauses.
   *
   * @param select  the selection clauses
   * @return the query-request builder
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#SelectSystemQueryOption">[odata.org] Select System Query Option ($select)</a>
   */
  OQueryRequest<T> select(String select);

  /**
   * Navigates to a referenced collection using a collection-valued navigation property.
   *
   * @param keyValue  identify an entity in the current entity-set using this key value
   * @param navProperty  the collection-valued navigation property off of the entity
   * @return the query-request builder
   */
  OQueryRequest<T> nav(Object keyValue, String navProperty);

  /**
   * Navigates to a referenced collection using a collection-valued navigation property.
   *
   * @param key  identify an entity in the current entity-set using this entity-key
   * @param navProperty  the collection-valued navigation property off of the entity
   * @return the query-request builder
   */
  OQueryRequest<T> nav(OEntityKey key, String navProperty);

  /**
   * Adds a custom name-value pair.
   *
   * @param name  the name
   * @param value  the value
   * @return the query-request builder
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#CustomQueryOptions">[odata.org] Custom Query Options</a>
   */
  OQueryRequest<T> custom(String name, String value);

  /**
   * Sets the expand expressions.
   *
   * @param expand  the expand expressions
   * @return the query-request builder
   * @see <a href="http://www.odata.org/developers/protocols/uri-conventions#ExpandSystemQueryOption">[odata.org] Expand System Query Option ($expand)</a>
   */
  OQueryRequest<T> expand(String expand);

}
