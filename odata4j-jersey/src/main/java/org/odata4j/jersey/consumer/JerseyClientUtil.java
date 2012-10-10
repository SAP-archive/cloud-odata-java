package org.odata4j.jersey.consumer;

import java.lang.reflect.Field;
import java.util.Set;

import javax.ws.rs.ext.RuntimeDelegate;

import org.odata4j.consumer.behaviors.OClientBehavior;
import org.odata4j.core.Throwables;
import org.odata4j.internal.PlatformUtil;
import org.odata4j.jersey.consumer.behaviors.JerseyClientBehavior;
import org.odata4j.jersey.internal.StringProvider2;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.impl.provider.header.MediaTypeProvider;
import com.sun.jersey.core.spi.factory.AbstractRuntimeDelegate;
import com.sun.jersey.spi.HeaderDelegateProvider;

class JerseyClientUtil {

  static {
    if (PlatformUtil.runningOnAndroid())
      androidJerseyClientHack();
  }

  @SuppressWarnings("unchecked")
  private static void androidJerseyClientHack() {
    try {
      RuntimeDelegate rd = RuntimeDelegate.getInstance();
      Field f = AbstractRuntimeDelegate.class.getDeclaredField("hps");
      f.setAccessible(true);
      Set<HeaderDelegateProvider<?>> hps = (Set<HeaderDelegateProvider<?>>) f.get(rd);
      hps.clear();
      hps.add(new MediaTypeProvider());
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  public static Client newClient(JerseyClientFactory clientFactory, OClientBehavior[] behaviors) {
    DefaultClientConfig cc = new DefaultClientConfig();
    cc.getSingletons().add(new StringProvider2());
    if (behaviors != null) {
      for (OClientBehavior behavior : behaviors)
      {
        if (behavior instanceof JerseyClientBehavior) {
          ((JerseyClientBehavior) behavior).modify(cc);
        }
      }
    }
    Client client = clientFactory.createClient(cc);
    if (behaviors != null)
    {
      for (OClientBehavior behavior : behaviors)
      {
        if (behavior instanceof JerseyClientBehavior) {
          ((JerseyClientBehavior) behavior).modifyClientFilters(client);
        }
      }
    }
    return client;
  }

  public static WebResource resource(Client client, String url, OClientBehavior[] behaviors) {
    WebResource resource = client.resource(url);
    if (behaviors != null)
    {
      for (OClientBehavior behavior : behaviors)
      {
        if (behavior instanceof JerseyClientBehavior) {
          ((JerseyClientBehavior) behavior).modifyWebResourceFilters(resource);
        }
      }
    }
    return resource;
  }

}
