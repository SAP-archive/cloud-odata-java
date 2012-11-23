package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * @author SAP AG
 *
 */
public class FunctionImportParameter {

  private String name;
  private String mode;
  private FullQualifiedName qualifiedName;
  private EdmFacets facets;
  private Mapping mapping;
  private Documentation documentation;
  private Annotations annotations;

  /**
   * @return <b>String</b> name of the parameter
   */
  public String getName() {
    return name;
  }

  /**
   * @return <b>String</b> mode of this parameter
   */
  public String getMode() {
    return mode;
  }

  /**
   * @return {@link FullQualifiedName} of this parameter
   */
  public FullQualifiedName getQualifiedName() {
    return qualifiedName;
  }

  /**
   * @return {@link EdmFacets} of this parameter
   */
  public EdmFacets getFacets() {
    return facets;
  }

  /**
   * @return {@link Mapping} of this parameter
   */
  public Mapping getMapping() {
    return mapping;
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
  public Annotations getAnnotations() {
    return annotations;
  }

  /**
   * MANDATORY
   * <p>Sets the name of this {@link FunctionImportParameter}
   * @param name
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the mode of this {@link FunctionImportParameter}
   * @param mode
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setMode(String mode) {
    this.mode = mode;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link FullQualifiedName} of this {@link FunctionImportParameter}
   * @param qualifiedName
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setQualifiedName(FullQualifiedName qualifiedName) {
    this.qualifiedName = qualifiedName;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link EdmFacets} of this {@link FunctionImportParameter}
   * @param facets
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setFacets(EdmFacets facets) {
    this.facets = facets;
    return this;
  }

  /**
   * Sets the {@link Mapping} of this {@link FunctionImportParameter}
   * @param mapping
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setMapping(Mapping mapping) {
    this.mapping = mapping;
    return this;
  }

  /**
   * Sets the {@link Documentation} of this {@link FunctionImportParameter}
   * @param documentation
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the {@link Annotations} of this {@link FunctionImportParameter}
   * @param annotations
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}