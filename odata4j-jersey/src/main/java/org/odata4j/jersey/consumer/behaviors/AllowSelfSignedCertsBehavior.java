package org.odata4j.jersey.consumer.behaviors;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.odata4j.consumer.ODataClientRequest;
import org.odata4j.consumer.behaviors.OClientBehavior;
import org.odata4j.core.Throwables;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.filter.Filterable;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public enum AllowSelfSignedCertsBehavior implements JerseyClientBehavior {

  INSTANCE;

  @Override
  public ODataClientRequest transform(ODataClientRequest request) {
    return request;
  }

  @Override
  public void modify(ClientConfig clientConfig) {

    HostnameVerifier hv = new HostnameVerifier() {
      public boolean verify(String urlHostName, SSLSession session) {
        // Write a warning, as there is certainly a potential security implication here.
        System.out.println(String.format("Warning:  URL Host: '%s' does not equal '%s'", urlHostName, session.getPeerHost()));
        return true;
      }
    };

    TrustManager[] trustAll = new TrustManager[] {
        new X509TrustManager() {
          public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
          }

          public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

          public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        }
    };

    try {
      SSLContext context = SSLContext.getInstance("TLS");
      context.init(null, trustAll, new SecureRandom());
      HTTPSProperties props = new HTTPSProperties(hv, context);
      clientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, props);
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public void modifyClientFilters(Filterable client) {}

  @Override
  public void modifyWebResourceFilters(Filterable webResource) {}

  /**
   * Creates a behavior that allows for https services with self-signed certificates.
   *
   * @return a behavior that allows for https services with self-signed certificates
   */
  public static OClientBehavior allowSelfSignedCerts() {
    return AllowSelfSignedCertsBehavior.INSTANCE;
  }

}
