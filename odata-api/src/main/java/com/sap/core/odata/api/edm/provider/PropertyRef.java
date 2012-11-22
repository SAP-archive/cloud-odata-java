package com.sap.core.odata.api.edm.provider;

/**
 * @author SAP AG
 *
 */
public class PropertyRef {

  private String name;
  private Annotations annotations;

  /**
   * @return <b>String</b> name of the {@link Property} this {@link PropertyRef} is referencing to
   */
  public String getName() {
    return name;
  }

  /**
   * @return {@link Annotations} annotations
   */
  public Annotations getAnnotations() {
    return annotations;
  }

  /**
   * MANDATORY
   * <p>Sets the name of the {@link Property} this {@link PropertyRef} is pointing to
   * @param name
   * @return {@link PropertyRef} for method chaining
   */
  public PropertyRef setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the {@link Annotations} for this {@link PropertyRef}
   * @param annotations
   * @return {@link PropertyRef} for method chaining
   */
  public PropertyRef setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
}