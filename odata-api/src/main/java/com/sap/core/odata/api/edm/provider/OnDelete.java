package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmAction;

public class OnDelete {

  private EdmAction action;
  private Documentation documentation;
  private Annotations annotation;

  public OnDelete(EdmAction action, Documentation documentation, Annotations annotation) {
    this.action = action;
    this.documentation = documentation;
    this.annotation = annotation;
  }

  public EdmAction getAction() {
    return action;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotation() {
    return annotation;
  }
}