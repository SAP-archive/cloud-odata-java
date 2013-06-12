package com.sap.core.odata.core.servicedocument;

import java.util.List;

import com.sap.core.odata.api.servicedocument.Collection;
import com.sap.core.odata.api.servicedocument.CommonAttributes;
import com.sap.core.odata.api.servicedocument.ExtensionElement;
import com.sap.core.odata.api.servicedocument.Title;
import com.sap.core.odata.api.servicedocument.Workspace;

/**
 * @author SAP AG
 */
public class WorkspaceImpl implements Workspace {
  private Title title;
  private List<Collection> collections;
  private CommonAttributes attributes;
  private List<ExtensionElement> extensionElements;

  @Override
  public Title getTitle() {
    return title;
  }

  @Override
  public List<Collection> getCollections() {
    return collections;
  }

  @Override
  public CommonAttributes getCommonAttributes() {
    return attributes;
  }

  @Override
  public List<ExtensionElement> getExtesionElements() {
    return extensionElements;
  }

  public WorkspaceImpl setTitle(final Title title) {
    this.title = title;
    return this;
  }

  public WorkspaceImpl setCollections(final List<Collection> collections) {
    this.collections = collections;
    return this;
  }

  public WorkspaceImpl setAttributes(final CommonAttributes attributes) {
    this.attributes = attributes;
    return this;
  }

  public WorkspaceImpl setExtesionElements(final List<ExtensionElement> elements) {
    extensionElements = elements;
    return this;
  }
}
