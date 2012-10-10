package org.odata4j.producer.resources;

import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.odata4j.core.Throwables;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.ODataProducerFactory;

/**
 * Default OData producer provider.
 *
 * <p>OData producer instances can either be set statically (method {@code setInstance})
 * or created by a factory ({@link ODataProducerFactory}) specified by the corresponding
 * system property (constant {@code FACTORY_PROPNAME}).
 *
 * <p>To introduce an additional container-specific setting, it is required to extend
 * this class and override method {@code createInstanceFromFactoryInContainerSpecificSetting}.
 * Furthermore a subclass of {@link AbstractODataApplication} has to be created to make
 * the new provider available to the JAX-RS runtime.
 */
@Provider
public class DefaultODataProducerProvider implements ContextResolver<ODataProducer> {

  /**
   * Constant used as system property name.
   */
  public static final String FACTORY_PROPNAME = "odata4j.producerfactory";

  private static ODataProducer STATIC;

  /**
   * Sets the given OData producer as a static singleton.
   *
   * @param producer  the OData producer
   */
  public static void setInstance(ODataProducer producer) {
    STATIC = producer;
  }

  private final Logger log = Logger.getLogger(getClass().getName());

  private ODataProducer instance;

  @Override
  public final ODataProducer getContext(Class<?> type) {
    if (!type.equals(ODataProducer.class))
      throw new RuntimeException("Invalid context type");

    if (instance != null)
      return instance;

    initializeInstance();
    return instance;
  }

  private void initializeInstance() {
    instance = setInstanceToStaticSingleton();
    if (instance == null)
      instance = createInstanceFromFactoryInContainerSpecificSetting();
    if (instance == null)
      instance = createInstanceFromFactoryInSystemProperties();
    if (instance == null)
      throw new RuntimeException("Unable to find an OData producer implementation. Call ODataProducerProvider.setInstance to set the static singleton or set the producer factory property \'" + FACTORY_PROPNAME + "\' in either the system properties or a container-specifc manner to a class name that implements ODataProducerFactory.");
  }

  private ODataProducer setInstanceToStaticSingleton() {
    if (STATIC != null) {
      log("Setting producer instance to static singleton: " + STATIC);
      return STATIC;
    }
    return null;
  }

  /**
   * Creates an OData producer instance using a factory specified in a container-specific
   * setting.
   *
   * <p>The default implementation returns {@code null}. Implementers can use the helper
   * methods {@code newProducerFromFactory} and {@code log}.
   *
   * @return the OData producer or {@code null} if no container-specific setting exists
   */
  protected ODataProducer createInstanceFromFactoryInContainerSpecificSetting() {
    return null;
  }

  private ODataProducer createInstanceFromFactoryInSystemProperties() {
    if (System.getProperty(FACTORY_PROPNAME) != null) {
      String factoryTypeName = System.getProperty(FACTORY_PROPNAME);
      log("Creating producer from factory in system properties: " + factoryTypeName);
      return newProducerFromFactory(factoryTypeName, System.getProperties());
    }
    return null;
  }

  /**
   * Helper method to create an OData producer instance from a given producer factory
   * ({@link ODataProducerFactory}).
   *
   * @param factoryTypeName  the factory's type name (fully qualified)
   * @param props  the properties to use when constructing the producer
   * @return the new producer
   */
  protected final ODataProducer newProducerFromFactory(String factoryTypeName, Properties props) {
    try {
      Class<?> factoryType = Class.forName(factoryTypeName);
      Object obj = factoryType.newInstance();
      ODataProducerFactory factory = (ODataProducerFactory) obj;
      return factory.create(props);
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  /**
   * Helper method to log an INFO message.
   *
   * @param msg  the log message
   */
  protected final void log(String msg) {
    log.info(msg);
  }
}
