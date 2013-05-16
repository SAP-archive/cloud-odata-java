package com.sap.core.odata.core.doc;

import java.util.List;

import com.sap.core.odata.api.doc.CommonAttributes;
import com.sap.core.odata.api.doc.ExtensionElement;
import com.sap.core.odata.api.doc.ServiceDocument;
import com.sap.core.odata.api.doc.Workspace;

/**
 * ServiceDokumentImpl
 * <p>The implementiation of the interface ServiceDocument
 * @author SAP AG
 */
public class ServiceDokumentImpl implements ServiceDocument {
  private List<WorkspaceImpl> workspaces;
  private CommonAttributes attributes;
  private List<ExtensionElementImpl> extensionElements;

  @Override
  public CommonAttributes getCommonAttributes() {
    return attributes;
  }

  @Override
  public List<Workspace> getWorkspaces() {
    return (List<Workspace>) (List<? extends Workspace>) workspaces;
  }

  @Override
  public List<ExtensionElement> getExtesionElements() {
    return (List<ExtensionElement>) (List<? extends ExtensionElement>) extensionElements;
  }

  public ServiceDokumentImpl setWorkspaces(final List<WorkspaceImpl> workspaces) {
    this.workspaces = workspaces;
    return this;
  }

  public ServiceDokumentImpl setCommonAttributes(final CommonAttributes attributes) {
    this.attributes = attributes;
    return this;
  }

  public ServiceDokumentImpl setExtesionElements(final List<ExtensionElementImpl> elements) {
    extensionElements = elements;
    return this;
  }

}
