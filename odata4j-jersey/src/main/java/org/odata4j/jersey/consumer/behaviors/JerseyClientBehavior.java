package org.odata4j.jersey.consumer.behaviors;

import org.odata4j.consumer.behaviors.OClientBehavior;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.filter.Filterable;

public interface JerseyClientBehavior extends OClientBehavior {

  public void modify(ClientConfig clientConfig);

  public void modifyClientFilters(Filterable client);

  public void modifyWebResourceFilters(Filterable webResource);

}
