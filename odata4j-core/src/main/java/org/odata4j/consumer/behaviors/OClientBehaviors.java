package org.odata4j.consumer.behaviors;

import org.odata4j.consumer.ODataClientRequest;
import org.odata4j.core.Throwables;

/**
 * A static factory to create built-in {@link OClientBehavior} instances.
 */
public class OClientBehaviors {

  private OClientBehaviors() {}

  /**
   * Creates a behavior that does http basic authentication.
   *
   * @param user  the basic auth user
   * @param password  the basic auth password
   * @return a behavior that does http basic authentication
   */
  public static OClientBehavior basicAuth(String user, String password) {
    return new BasicAuthenticationBehavior(user, password);
  }

  /**
   * Creates a behavior that signs requests properly for the Azure Table Storage service.
   *
   * @param account  azure account key
   * @param key  azure secret key
   * @return a behavior that signs requests properly for the Azure Table Storage service
   */
  public static OClientBehavior azureTables(String account, String key) {
    return new AzureTableBehavior(account, key);
  }

  /**
   * Creates a behavior that tunnels specific http request methods through POST.
   *
   * @param methodsToTunnel  the methods to tunnel.  e.g. <code>MERGE</code>
   * @return a behavior that tunnels specific http request methods through POST
   */
  public static OClientBehavior methodTunneling(String... methodsToTunnel) {
    return new MethodTunnelingBehavior(methodsToTunnel);
  }

  /**
   * Creates a behavior that sleeps a specified amount of time before each client request.
   *
   * @param millis  the time to sleep in milliseconds
   * @return a behavior that sleeps a specified amount of time before each client request
   */
  public static OClientBehavior rateLimit(final long millis) {
    return new OClientBehavior() {
      @Override
      public ODataClientRequest transform(ODataClientRequest request) {
        try {
          Thread.sleep(millis);
        } catch (InterruptedException e) {
          throw Throwables.propagate(e);
        }
        return request;
      }
    };
  }

}
