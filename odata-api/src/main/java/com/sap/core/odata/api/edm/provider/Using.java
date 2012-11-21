package com.sap.core.odata.api.edm.provider;

/**
 * @author SAP AG
 * 
 */
public class Using {

  private String namespace;
  private String alias;
  private Documentation documentation;
  private Annotations annotations;

  /**
   * @param namespace
   */
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  /**
   * @param alias
   */
  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   * @param documentation
   */
  public void setDocumentation(Documentation documentation) {
    this.documentation = documentation;
  }

  /**
   * @param annotations
   */
  public void setAnnotations(Annotations annotations) {
    this.annotations = annotations;
  }

  /**
   * @return
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * @return
   */
  public String getAlias() {
    return alias;
  }

  /**
   * @return
   */
  public Documentation getDocumentation() {
    return documentation;
  }

  /**
   * @return
   */
  public Annotations getAnnotations() {
    return annotations;
  }
}
