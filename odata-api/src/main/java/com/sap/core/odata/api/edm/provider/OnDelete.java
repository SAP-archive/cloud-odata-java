package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmAction;

/**
 * @author SAP AG
 *
 */
public class OnDelete {

  //TODO: revisit javadoc for mandatory methods
  private EdmAction action;
  private Documentation documentation;
  private Annotations annotation;

  /**
   * @return {@link EdmAction} action
   */
  public EdmAction getAction() {
    return action;
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
  public Annotations getAnnotation() {
    return annotation;
  }

  /**
   * Sets the {@link EdmAction} for this {@link OnDelete}
   * @param action
   * @return {@link OnDelete} for method chaining
   */
  public OnDelete setAction(EdmAction action) {
    this.action = action;
    return this;
  }

  /**
   * Sets the {@link Documentation} for this {@link OnDelete}
   * @param documentation
   * @return {@link OnDelete} for method chaining
   */
  public OnDelete setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the {@link Annotations} for this {@link OnDelete}
   * @param annotation
   * @return {@link OnDelete} for method chaining
   */
  public OnDelete setAnnotation(Annotations annotation) {
    this.annotation = annotation;
    return this;
  }
}