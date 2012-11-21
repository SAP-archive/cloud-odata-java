package com.sap.core.odata.api.edm.provider;

/**
 * @author SAP AG
 * <p>
 * Objects of this class represent documentation
 */
public class Documentation {

  private String summary;
  private String longDescription;
  private Annotations annotations;

  /**
   * @return <b>String</b> summary
   */
  public String getSummary() {
    return summary;
  }

  /**
   * @return <b>String</b> the long description
   */
  public String getLongDescription() {
    return longDescription;
  }

  /**
   * @return {@link Annotations} annotations
   */
  public Annotations getAnnotations() {
    return annotations;
  }

  /**
   * Sets the summary for this {@link Documentation}
   * @param summary
   * @return {@link Documentation} for method chaining
   */
  public Documentation setSummary(String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Sets the long description for this {@link Documentation}
   * @param longDescription
   * @return {@link Documentation} for method chaining
   */
  public Documentation setLongDescription(String longDescription) {
    this.longDescription = longDescription;
    return this;
  }

  /**
   * Sets the {@link Annotations} for this {@link Documentation}
   * @param annotations
   * @return {@link Documentation} for method chaining
   */
  public Documentation setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}