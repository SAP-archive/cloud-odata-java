package org.odata4j.producer;

import java.util.Properties;

/**
 * An <code>ODataProducerFactory</code> creates new OData producer instances given properties.
 */
public interface ODataProducerFactory {

  /**
   * Create a new OData producer from the given properties.
   * 
   * @param properties  the properties to use when constructing the producer
   * @return the new producer
   */
  ODataProducer create(Properties properties);
}
