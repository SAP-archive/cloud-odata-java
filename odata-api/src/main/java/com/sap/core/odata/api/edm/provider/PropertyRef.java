package com.sap.core.odata.api.edm.provider;

/**
 * @author SAP AG
 *
 */
public class PropertyRef {

  private String name;
  private Annotations annotations;

  /**
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * @return
   */
  public Annotations getAnnotations() {
    return annotations;
  }

  /**
   * @param name
   * @return
   */
  public PropertyRef setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @param annotations
   * @return
   */
  public PropertyRef setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
}