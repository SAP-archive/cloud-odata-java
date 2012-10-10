package org.odata4j.format.xml;

import org.odata4j.core.EntitySetInfo;

public class AtomCollectionInfo implements EntitySetInfo {

  private final String href;
  private final String url;
  private final String title;
  private final String accept;

  AtomCollectionInfo(String href, String url, String title, String accept) {
    this.href = href;
    this.url = url;
    this.title = title;
    this.accept = accept;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getSimpleName());
    sb.append('[');
    sb.append(title);
    if (!title.equals(href))
      sb.append(",href=" + href);
    sb.append(']');
    return sb.toString();
  }

  @Override
  public String getHref() {
    return href;
  }

  @Override
  public String getTitle() {
    return title;
  }

  public String getUrl() {
    return url;
  }

  public String getAccept() {
    return accept;
  }

}