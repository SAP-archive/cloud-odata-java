package com.sap.core.odata.api.edm.provider;

import java.util.List;

import com.sap.core.odata.api.edm.EdmAction;

/**
 * Objects of this class represent an OnDelete Action
 * @author SAP AG
 */
public class OnDelete {

  private EdmAction action;
  private Documentation documentation;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

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
   * @return List of {@link AnnotationAttribute} annotation attributes
   */
  public List<AnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  /**
   * @return List of {@link AnnotationElement} annotation elements
   */
  public List<AnnotationElement> getAnnotationElements() {
    return annotationElements;
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
   * Sets the List of {@link AnnotationAttribute} for this {@link OnDelete}
   * @param annotationAttributes
   * @return {@link OnDelete} for method chaining
   */
  public OnDelete setAnnotationAttributes(List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationElement} for this {@link OnDelete}
   * @param annotationElements
   * @return {@link OnDelete} for method chaining
   */
  public OnDelete setAnnotationElements(List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}