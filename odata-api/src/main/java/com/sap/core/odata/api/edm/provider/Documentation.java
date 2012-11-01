package com.sap.core.odata.api.edm.provider;

public class Documentation {

  private String summary;
  private String longDescription;
  private Annotations annotations;

  public Documentation(String summary, String longDescription, Annotations annotations) {
    this.summary = summary;
    this.longDescription = longDescription;
    this.annotations = annotations;
  }

  public String getSummary() {
    return summary;
  }

  public String getLongDescription() {
    return longDescription;
  }

  public Annotations getAnnotations() {
    return annotations;
  }
}