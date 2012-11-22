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
   * MANDATORY
   * <p>Sets the namespace for this {@link Using}
   * @param namespace
   * @return {@link Using} for method chaining
   */
  public Using setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the alias for this {@link Using}
   * @param alias
   * @return {@link Using} for method chaining
   */
  public Using setAlias(String alias) {
    this.alias = alias;
    return this;
  }

  /**
   * Sets the {@link Documentation} for this {@link Using}
   * @param documentation
   * @return {@link Using} for method chaining
   */
  public Using setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the {@link Annotations} for this {@link Using}
   * @param annotations
   * @return {@link Using} for method chaining
   */
  public Using setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

  /**
   * @return <b>String</b> namespace
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * @return <b>String</b> alias
   */
  public String getAlias() {
    return alias;
  }

  /**
   * @return {@link Documentation} documentation
   */
  public Documentation getDocumentation() {
    return documentation;
  }

  /**
   * @return {@link Annotations} annotations
   */
  public Annotations getAnnotations() {
    return annotations;
  }
}
