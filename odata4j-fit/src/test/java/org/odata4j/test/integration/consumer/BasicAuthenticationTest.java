package org.odata4j.test.integration.consumer;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.eclipse.jetty.http.security.Constraint;
import org.eclipse.jetty.http.security.Password;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;
import org.odata4j.cxf.producer.server.ODataCxfServer;
import org.odata4j.examples.producer.jpa.AddressBookJpaExample;
import org.odata4j.jersey.producer.server.ODataJerseyServer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.test.integration.AbstractODataConsumerTest;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.BasicAuthenticator;

public class BasicAuthenticationTest extends AbstractODataConsumerTest {

  private static final String REALM = "SomeRealm";
  private static final String USERNAME = "SomeUsername";
  private static final String PASSWORD = "SomePassword";
  private static final String ROLE = "SomeRole";

  public BasicAuthenticationTest(RuntimeFacadeType type) {
    super(type);
  }

  @Override
  protected void startODataServer() throws Exception {
    server = rtFacade.createODataServer(BASE_URI);

    if (server instanceof ODataJerseyServer)
      ((ODataJerseyServer) server).setHttpServerAuthenticator(getAuthenticator());
    else
      ((ODataCxfServer) server).setJettySecurityHandler(getSecurityHandler());

    server.start();
  }

  private Authenticator getAuthenticator() {
    Authenticator authenticator = new BasicAuthenticator(REALM) {
      public boolean checkCredentials(String username, String password) {
        return USERNAME.equals(username) && PASSWORD.equals(password);
      }
    };
    return authenticator;
  }

  private SecurityHandler getSecurityHandler() {
    ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
    securityHandler.addConstraintMapping(mapping(constraint()));
    securityHandler.setLoginService(loginService());
    return securityHandler;
  }

  private Constraint constraint() {
    Constraint constraint = new Constraint();
    constraint.setAuthenticate(true);
    constraint.setRoles(new String[] { ROLE });
    return constraint;
  }

  private ConstraintMapping mapping(Constraint constraint) {
    ConstraintMapping constraintMapping = new ConstraintMapping();
    constraintMapping.setConstraint(constraint);
    constraintMapping.setPathSpec("/*");
    return constraintMapping;
  }

  private HashLoginService loginService() {
    HashLoginService loginService = new HashLoginService();
    loginService.update(USERNAME, new Password(PASSWORD), new String[] { ROLE });
    return loginService;
  }

  @Override
  protected void startClient() throws Exception {
    ODataConsumer.dump.all(true);
    consumer = rtFacade.createODataConsumer(BASE_URI, format, OClientBehaviors.basicAuth(USERNAME, PASSWORD));
  }

  @Override
  protected void registerODataProducer() throws Exception {
    DefaultODataProducerProvider.setInstance(AddressBookJpaExample.createProducer());
  }

  @Test
  public void unauthorizedConsumerIsRejected() throws Exception {
    ODataConsumer unauthorizedConsumer = rtFacade.createODataConsumer(BASE_URI, format);
    try {
      unauthorizedConsumer.getEntities("Persons").execute();
      fail();
    } catch (RuntimeException e) {
      assertThat(e.getMessage(), containsString("Unauthorized"));
    }
  }

  @Test
  public void getEntity() throws Exception {
    OEntity entity = consumer.getEntity("Persons", Integer.valueOf(1)).execute();
    assertThat(entity, notNullValue());
  }

  @Test
  public void createEntity() throws Exception {
    OProperty<Integer> personId = OProperties.int32("PersonId", Integer.valueOf(4));
    OProperty<String> name = OProperties.string("Name", "Stephanie Spring");
    OProperty<String> emailAddress = OProperties.string("EmailAddress", "st.spring@mail-provider.com");
    OProperty<LocalDateTime> birthDay = OProperties.datetime("BirthDay", new LocalDateTime(1979, 4, 9, 0, 0));

    assertThat(consumer.createEntity("Persons").properties(personId, name, emailAddress, birthDay).execute(), notNullValue());
  }

  @Test
  public void updateEntity() throws Exception {
    OProperty<String> newEmailAddress = OProperties.string("EmailAddress", "walter.winter@new-company.com");

    OEntity entity = consumer.getEntity("Persons", Integer.valueOf(2)).execute();
    consumer.updateEntity(entity).properties(newEmailAddress).execute();
  }

  @Test
  public void deleteEntity() throws Exception {
    consumer.deleteEntity("Persons", Integer.valueOf(3)).execute();
  }
}
