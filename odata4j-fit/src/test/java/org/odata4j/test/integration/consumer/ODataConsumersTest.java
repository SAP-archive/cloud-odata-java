package org.odata4j.test.integration.consumer;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.odata4j.consumer.ODataConsumers.CONSUMERIMPL_PROPERTY;
import static org.odata4j.consumer.ODataConsumers.CXF_CONSUMERIMPL;
import static org.odata4j.consumer.ODataConsumers.CXF_CONSUMER_CLASSNAME;
import static org.odata4j.consumer.ODataConsumers.JERSEY_CONSUMERIMPL;
import static org.odata4j.consumer.ODataConsumers.JERSEY_CONSUMER_CLASSNAME;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.core4j.Enumerable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.cxf.consumer.ODataCxfConsumer;
import org.odata4j.jersey.consumer.ODataJerseyConsumer;

public class ODataConsumersTest {

  private static String oldConsumerImplProperty;

  // The system property 'odata4j.consumerimpl' actually refers to a class implementing a factory
  // method 'newBuilder' - by convention. This method returns an ODataConsumer.Builder, whose
  // build() method returns an ODataConsumer.
  //
  // ODataJerseyConsumer and ODataCxfConsumer do not only implement the nested interfaces
  // ODataConsumer and ODataConsumer.Builder, but also this 'newBuilder' factory method.
  //
  // For testing purposes the interfaces are mocked (otherConsumerBuilderMock and
  // otherConsumerMock), but the factory method 'newBuilder' is implemented in a separate static
  // inner class (OtherConsumer).

  private static ODataConsumer.Builder otherConsumerBuilderMock;
  private static ODataConsumer otherConsumerMock;

  public static class OtherConsumer {

    public static ODataConsumer.Builder newBuilder(String serviceRootUri) {
      return otherConsumerBuilderMock;
    }

    public static Class<?> getConsumerClass() {
      return otherConsumerMock.getClass();
    }
  }

  public static class OtherConsumerWithWrongInterface {}

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @BeforeClass
  public static void setupClass() throws Exception {
    oldConsumerImplProperty = System.getProperty(CONSUMERIMPL_PROPERTY);
    System.clearProperty(CONSUMERIMPL_PROPERTY);

    otherConsumerBuilderMock = mock(ODataConsumer.Builder.class);
    otherConsumerMock = mock(ODataConsumer.class);
    when(otherConsumerBuilderMock.build()).thenReturn(otherConsumerMock);

    TestClassLoader.addClass(OtherConsumer.class);
    TestClassLoader.addClass(OtherConsumerWithWrongInterface.class);
  }

  @AfterClass
  public static void teardownClass() throws Exception {
    if (oldConsumerImplProperty != null)
      System.setProperty(CONSUMERIMPL_PROPERTY, oldConsumerImplProperty);
  }

  @After
  public void teardown() throws Exception {
    // reset 'classLoader' field
    injectClassLoader(null);

    // remove 'odata4j.consumerimpl' property
    System.clearProperty(CONSUMERIMPL_PROPERTY);
  }

  @Test
  public void loadJerseyImplByDefault() throws Exception {
    ODataConsumer consumer = ODataConsumers.newBuilder("foo").build();
    assertThat(consumer, is(ODataJerseyConsumer.class));
  }

  @Test
  public void loadCxfImplByDefaultIfJerseyImplIsNotAvailable() throws Exception {
    useTestClassLoader(JERSEY_CONSUMER_CLASSNAME);
    ODataConsumer consumer = ODataConsumers.newBuilder("foo").build();
    assertThat(consumer, is(ODataCxfConsumer.class));
  }

  @Test
  public void failIfNoDefaultImplIsAvailable() throws Exception {
    useTestClassLoader(JERSEY_CONSUMER_CLASSNAME, CXF_CONSUMER_CLASSNAME);
    exception.expect(RuntimeException.class);
    exception.expectMessage(ClassNotFoundException.class.getName());
    ODataConsumers.newBuilder("foo");
  }

  @Test
  public void loadJerseyImplAsSpecifiedInProperty() throws Exception {
    System.setProperty(CONSUMERIMPL_PROPERTY, JERSEY_CONSUMERIMPL);
    ODataConsumer consumer = ODataConsumers.newBuilder("foo").build();
    assertThat(consumer, is(ODataJerseyConsumer.class));
  }

  @Test
  public void failIfJerseyImplIsSpecifiedInPropertyButCouldNotBeLoaded() throws Exception {
    System.setProperty(CONSUMERIMPL_PROPERTY, JERSEY_CONSUMERIMPL);
    useTestClassLoader(JERSEY_CONSUMER_CLASSNAME);
    exception.expect(RuntimeException.class);
    exception.expectMessage(ClassNotFoundException.class.getName());
    ODataConsumers.newBuilder("foo");
  }

  @Test
  public void loadCxfImplAsSpecifiedInProperty() throws Exception {
    System.setProperty(CONSUMERIMPL_PROPERTY, CXF_CONSUMERIMPL);
    ODataConsumer consumer = ODataConsumers.newBuilder("foo").build();
    assertThat(consumer, is(ODataCxfConsumer.class));
  }

  @Test
  public void loadOtherImplAsSpecifiedInProperty() throws Exception {
    System.setProperty(CONSUMERIMPL_PROPERTY, OtherConsumer.class.getName());
    useTestClassLoader();
    ODataConsumer consumer = ODataConsumers.newBuilder("foo").build();
    assertThat(consumer, is(OtherConsumer.getConsumerClass()));
  }

  @Test
  public void failIfOtherImplIsSpecifiedInPropertyButCouldNotBeLoaded() throws Exception {
    System.setProperty(CONSUMERIMPL_PROPERTY, OtherConsumer.class.getName());
    useTestClassLoader(OtherConsumer.class.getName());
    exception.expect(RuntimeException.class);
    exception.expectMessage(ClassNotFoundException.class.getName());
    ODataConsumers.newBuilder("foo");
  }

  @Test
  public void failIfOtherImplIsSpecifiedInPropertyButDoesNotMatchInterface() throws Exception {
    System.setProperty(CONSUMERIMPL_PROPERTY, OtherConsumerWithWrongInterface.class.getName());
    useTestClassLoader();
    exception.expect(RuntimeException.class);
    exception.expectMessage(NoSuchMethodException.class.getName());
    ODataConsumers.newBuilder("foo");
  }

  private void injectClassLoader(ClassLoader classLoader) throws Exception {
    // use backdoor injection to set different classloader
    Field classLoaderField = ODataConsumers.class.getDeclaredField("classLoader");
    classLoaderField.setAccessible(true);
    classLoaderField.set(null, classLoader);
  }

  private void useTestClassLoader(String... classesNotToFind) throws Exception {
    injectClassLoader(new TestClassLoader(classesNotToFind));
  }

  public static class TestClassLoader extends ClassLoader {

    private static final Map<String, Class<?>> otherClasses = new HashMap<String, Class<?>>();

    private final Enumerable<String> classesNotToFind;

    private TestClassLoader(String... classesNotToFind) {
      this.classesNotToFind = Enumerable.create(classesNotToFind);
    }

    private static void addClass(Class<?> clazz) {
      otherClasses.put(clazz.getName(), clazz);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
      if (classesNotToFind.contains(name))
        throw new ClassNotFoundException();
      if (otherClasses.containsKey(name))
        return otherClasses.get(name);
      return super.loadClass(name);
    }
  }
}
