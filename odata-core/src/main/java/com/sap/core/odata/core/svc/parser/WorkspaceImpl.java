package com.sap.core.odata.core.svc.parser;

import java.util.List;

public class WorkspaceImpl {
  private TitleImpl title;
  private List<CollectionImpl> collections;
  private CommonAttributesImpl attributes;
  private List<ExtensionElementImpl> extensionElements;

  public TitleImpl getTitle() {
    return title;
  }

  public List<CollectionImpl> getCollections() {
    return collections;
  }

  public CommonAttributesImpl getAttributes() {
    return attributes;
  }

  public List<ExtensionElementImpl> getExtesionElements() {
    return extensionElements;
  }

  public WorkspaceImpl setTitle(final TitleImpl title) {
    this.title = title;
    return this;
  }

  public WorkspaceImpl setCollections(final List<CollectionImpl> collections) {
    this.collections = collections;
    return this;
  }

  public WorkspaceImpl setAttributes(final CommonAttributesImpl attributes) {
    this.attributes = attributes;
    return this;
  }

  public WorkspaceImpl setExtesionElements(final List<ExtensionElementImpl> elements) {
    extensionElements = elements;
    return this;
  }
}
