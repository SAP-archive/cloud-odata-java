package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmAction;

public class OnDelete {

  private EdmAction action;
  private Documentation documentation;
  private Annotations annotation;

  public EdmAction getAction() {
    return action;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotation() {
    return annotation;
  }

  public OnDelete setAction(EdmAction action) {
    this.action = action;
    return this;
  }

  public OnDelete setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  public OnDelete setAnnotation(Annotations annotation) {
    this.annotation = annotation;
    return this;
  }
}