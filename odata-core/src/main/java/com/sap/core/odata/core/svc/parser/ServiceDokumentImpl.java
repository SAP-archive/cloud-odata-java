package com.sap.core.odata.core.svc.parser;

import java.util.List;

public class ServiceDokumentImpl {
  private List<WorkspaceImpl> workspaces;
  private CommonAttributesImpl attributes;
  private List<ExtensionElementImpl> extensionElements;

  public CommonAttributesImpl getCommonAttributes() {
    return attributes;
  }

  public List<WorkspaceImpl> getWorkspaces() {
    return workspaces;
  }

  public List<ExtensionElementImpl> getExtesionElements() {
    return extensionElements;
  }

  public ServiceDokumentImpl setWorkspaces(final List<WorkspaceImpl> workspaces) {
    this.workspaces = workspaces;
    return this;
  }

  public ServiceDokumentImpl setCommonAttributes(final CommonAttributesImpl attributes) {
    this.attributes = attributes;
    return this;
  }

  public ServiceDokumentImpl setExtesionElements(final List<ExtensionElementImpl> elements) {
    extensionElements = elements;
    return this;
  }

}
