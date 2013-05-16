package com.sap.core.odata.core.doc;

import java.util.List;

import com.sap.core.odata.api.doc.Collection;
import com.sap.core.odata.api.doc.CommonAttributes;
import com.sap.core.odata.api.doc.ExtensionElement;
import com.sap.core.odata.api.doc.Title;
import com.sap.core.odata.api.doc.Workspace;

/**
 * WorkspaceImpl
 * <p>The implementiation of the interface Workspace
 * @author SAP AG
 */
public class WorkspaceImpl implements Workspace {
  private Title title;
  private List<CollectionImpl> collections;
  private CommonAttributes attributes;
  private List<ExtensionElementImpl> extensionElements;

  @Override
  public Title getTitle() {
    return title;
  }

  @Override
  public List<Collection> getCollections() {
    return (List<Collection>) (List<? extends Collection>) collections;
  }

  @Override
  public CommonAttributes getCommonAttributes() {
    return attributes;
  }

  @Override
  public List<ExtensionElement> getExtesionElements() {
    return (List<ExtensionElement>) (List<? extends ExtensionElement>) extensionElements;
  }

  public WorkspaceImpl setTitle(final Title title) {
    this.title = title;
    return this;
  }

  public WorkspaceImpl setCollections(final List<CollectionImpl> collections) {
    this.collections = collections;
    return this;
  }

  public WorkspaceImpl setAttributes(final CommonAttributes attributes) {
    this.attributes = attributes;
    return this;
  }

  public WorkspaceImpl setExtesionElements(final List<ExtensionElementImpl> elements) {
    extensionElements = elements;
    return this;
  }
}
