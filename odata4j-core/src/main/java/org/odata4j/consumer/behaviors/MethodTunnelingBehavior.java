package org.odata4j.consumer.behaviors;

import org.odata4j.consumer.ODataClientRequest;
import org.odata4j.core.ODataConstants;

public class MethodTunnelingBehavior implements OClientBehavior {

  private final String[] methodsToTunnel;

  public MethodTunnelingBehavior(String... methodsToTunnel) {
    this.methodsToTunnel = methodsToTunnel;
  }

  @Override
  public ODataClientRequest transform(ODataClientRequest request) {
    String method = request.getMethod();
    for (String methodToTunnel : methodsToTunnel) {
      if (method.equals(methodToTunnel)) {
        return request.header(ODataConstants.Headers.X_HTTP_METHOD, method).method("POST");
      }
    }
    return request;
  }

}
