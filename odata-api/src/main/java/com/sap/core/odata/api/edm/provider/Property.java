package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * @author SAP AG
 *<p>
 * Objects of this class represent a property of an entity type in the EDM
 */
public class Property {

  private String name;
  private FullQualifiedName type;
  private EdmFacets facets;
  private CustomizableFeedMappings customizableFeedMappings;
  private String mimeType;
  private Mapping mapping;
  private Documentation documentation;
  private Annotations annotations;

  /**
   * @return <b>String</b> name of this property
   */
  public String getName() {
    return name;
  }

  /**
   * @return {@link FullQualifiedName} of the {@link EdmType} of this property
   */
  public FullQualifiedName getType() {
    return type;
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
   * @return {@link Annotations} of this property
   */
  public Annotations getAnnotations() {
    return annotations;
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
   * MANDATORY
   * <p>Sets the {@link FullQualifiedName} for the {@link EdmTypeKind} of this {@link Property}
   * @param type
   * @return {@link Property} for method chaining
   */
  public Property setType(FullQualifiedName type) {
    this.type = type;
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
   * Sets the {@link Annotations} for this {@link Property}
   * @param annotations
   * @return {@link Property} for method chaining
   */
  public Property setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
}