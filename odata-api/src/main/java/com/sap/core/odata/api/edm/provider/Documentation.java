package com.sap.core.odata.api.edm.provider;

public class Documentation {

  private String summary;
  private String longDescription;
  private Annotations annotations;

  public String getSummary() {
    return summary;
  }

  public String getLongDescription() {
    return longDescription;
  }

  public Annotations getAnnotations() {
    return annotations;
  }

  public Documentation setSummary(String summary) {
    this.summary = summary;
    return this;
  }

  public Documentation setLongDescription(String longDescription) {
    this.longDescription = longDescription;
    return this;
  }

  public Documentation setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}