package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.EdmFacets;

/**
 * Objects of this class represent a property of an entity type
 * @author SAP AG
 */
public abstract class Property {

  private String name;
  private EdmFacets facets;
  private CustomizableFeedMappings customizableFeedMappings;
  private String mimeType;
  private Mapping mapping;
  private Documentation documentation;
  private Collection<AnnotationAttribute> annotationAttributes;
  private Collection<AnnotationElement> annotationElements;

  /**
   * @return <b>String</b> name of this property
   */
  public String getName() {
    return name;
  }

  /**
   * @return {@link EdmFacets} of this property
   */
  public EdmFacets getFacets() {
    return facets;
  }

  /**
   * @return {@link CustomizableFeedMappings} of this property
   */
  public CustomizableFeedMappings getCustomizableFeedMappings() {
    return customizableFeedMappings;
  }

  /**
   * @return <b>String</b> mime type of this property
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * @return {@link Mapping} of this property
   */
  public Mapping getMapping() {
    return mapping;
  }

  /**
   * @return {@link Documentation} of this property
   */
  public Documentation getDocumentation() {
    return documentation;
  }

  /**
   * @return collection of {@link AnnotationAttribute} annotation attributes
   */
  public Collection<AnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  /**
   * @return collection of {@link AnnotationElement} annotation elements
   */
  public Collection<AnnotationElement> getAnnotationElements() {
    return annotationElements;
  }

  /**
   * MANDATORY
   * <p>Sets the name for this {@link Property}
   * @param name
   * @return {@link Property} for method chaining
   */
  public Property setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the {@link Facets} for this {@link Property}
   * @param facets
   * @return {@link Property} for method chaining
   */
  public Property setFacets(EdmFacets facets) {
    this.facets = facets;
    return this;
  }

  /**
   * Sets the {@link CustomizableFeedMappings} for this {@link Property}
   * @param customizableFeedMappings
   * @return {@link Property} for method chaining
   */
  public Property setCustomizableFeedMappings(CustomizableFeedMappings customizableFeedMappings) {
    this.customizableFeedMappings = customizableFeedMappings;
    return this;
  }

  /**
   * Sets the mime type for this {@link Property}
   * @param mimeType
   * @return {@link Property} for method chaining
   */
  public Property setMimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  /**
   * Sets the {@link Mapping} for this {@link Property}
   * @param mapping
   * @return {@link Property} for method chaining
   */
  public Property setMapping(Mapping mapping) {
    this.mapping = mapping;
    return this;
  }

  /**
   * Sets the {@link Documentation} for this {@link Property}
   * @param documentation
   * @return {@link Property} for method chaining
   */
  public Property setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link Property}
   * @param annotationAttributes
   * @return {@link Property} for method chaining
   */
  public Property setAnnotationAttributes(Collection<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link Property}
   * @param annotationElements
   * @return {@link Property} for method chaining
   */
  public Property setAnnotationElements(Collection<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}