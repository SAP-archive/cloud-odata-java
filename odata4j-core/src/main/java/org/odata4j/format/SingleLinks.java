package org.odata4j.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityIds;

public class SingleLinks implements Iterable<SingleLink> {

  private final List<SingleLink> links;

  private SingleLinks(Collection<SingleLink> links) {
    this.links = new ArrayList<SingleLink>(links);
  }

  @Override
  public Iterator<SingleLink> iterator() {
    return links.iterator();
  }

  public static SingleLink create(String uri) {
    return new SingleLinkImpl(uri);
  }

  public static SingleLinks create(String serviceRootUri, Iterable<OEntityId> entities) {
    List<SingleLink> rt = new ArrayList<SingleLink>();
    for (OEntityId e : entities)
      rt.add(create(serviceRootUri, e));
    return new SingleLinks(rt);
  }

  public static SingleLink create(String serviceRootUri, OEntityId entity) {
    String uri = serviceRootUri;
    if (!uri.endsWith("/"))
      uri += "/";
    uri += OEntityIds.toKeyString(entity);
    return create(uri);
  }

  private static class SingleLinkImpl implements SingleLink {

    private final String uri;

    public SingleLinkImpl(String uri) {
      this.uri = uri;
    }

    @Override
    public String getUri() {
      return uri;
    }

    @Override
    public String toString() {
      return String.format("SingleLink[%s]", uri);
    }
  }

}
