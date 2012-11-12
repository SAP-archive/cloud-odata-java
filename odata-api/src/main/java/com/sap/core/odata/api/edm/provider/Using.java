package com.sap.core.odata.api.edm.provider;

public class Using {

  private String namespace;
  private String alias;
  private Documentation documentation;
  private Annotations annotations;

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public void setDocumentation(Documentation documentation) {
    this.documentation = documentation;
  }

  public void setAnnotations(Annotations annotations) {
    this.annotations = annotations;
  }

  public String getNamespace() {
    return namespace;
  }

  public String getAlias() {
    return alias;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotations() {
    return annotations;
  }
}
