package com.sap.core.odata.processor.core.jpa.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EmbeddableType;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmMappingModelAccess;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmMapping;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmNameBuilder;

public class JPAEdmComplexType extends JPAEdmBaseViewImpl implements
    JPAEdmComplexTypeView {

  private JPAEdmSchemaView schemaView;
  private ComplexType currentComplexType = null;
  private EmbeddableType<?> currentEmbeddableType = null;
  private HashMap<String, ComplexType> searchMap = null;
  private List<ComplexType> consistentComplextTypes = null;
  private boolean directBuild;
  private EmbeddableType<?> nestedComplexType = null;

  public JPAEdmComplexType(final JPAEdmSchemaView view) {
    super(view);
    schemaView = view;
    directBuild = true;
  }

  public JPAEdmComplexType(final JPAEdmSchemaView view, final Attribute<?, ?> complexAttribute) {
    super(view);
    schemaView = view;
    for (EmbeddableType<?> jpaEmbeddable : schemaView.getJPAMetaModel().getEmbeddables())
    {
      if (jpaEmbeddable.getJavaType().getName().equals(complexAttribute.getJavaType().getName()))
      {
        nestedComplexType = jpaEmbeddable;
        break;
      }
    }
    directBuild = false;
  }

  @Override
  public JPAEdmBuilder getBuilder() {
    if (builder == null) {
      builder = new JPAEdmComplexTypeBuilder();
    }

    return builder;
  }

  @Override
  public ComplexType getEdmComplexType() {
    return currentComplexType;
  }

  @Override
  public ComplexType searchEdmComplexType(final String embeddableTypeName) {
    return searchMap.get(embeddableTypeName);
  }

  @Override
  public EmbeddableType<?> getJPAEmbeddableType() {
    return currentEmbeddableType;
  }

  @Override
  public List<ComplexType> getConsistentEdmComplexTypes() {
    return consistentComplextTypes;
  }

  @Override
  public ComplexType searchEdmComplexType(final FullQualifiedName type) {
    String name = type.getName();
    return searchComplexTypeByName(name);

  }

  private ComplexType searchComplexTypeByName(final String name) {
    for (ComplexType complexType : consistentComplextTypes) {
      if (null != complexType && null != complexType.getName() && complexType.getName().equals(name)) {
        return complexType;
      }
    }

    return null;
  }

  @Override
  public void addJPAEdmCompleTypeView(final JPAEdmComplexTypeView view) {
    String searchKey = view.getJPAEmbeddableType().getJavaType().getName();

    if (!searchMap.containsKey(searchKey)) {
      consistentComplextTypes.add(view.getEdmComplexType());
      searchMap.put(searchKey, view.getEdmComplexType());
    }
  }

  @Override
  public void expandEdmComplexType(final ComplexType complexType, List<Property> expandedList, final String embeddablePropertyName) {

    if (expandedList == null) {
      expandedList = new ArrayList<Property>();
    }
    JPAEdmMapping complexTypeMapping = (JPAEdmMapping) complexType.getMapping();
    for (Property property : complexType.getProperties())
    {
      try {
        SimpleProperty newSimpleProperty = new SimpleProperty();
        SimpleProperty oldSimpleProperty = (SimpleProperty) property;
        newSimpleProperty.setAnnotationAttributes(oldSimpleProperty.getAnnotationAttributes());
        newSimpleProperty.setAnnotationElements(oldSimpleProperty.getAnnotationElements());
        newSimpleProperty.setCustomizableFeedMappings(oldSimpleProperty.getCustomizableFeedMappings());
        newSimpleProperty.setDocumentation(oldSimpleProperty.getDocumentation());
        newSimpleProperty.setFacets(oldSimpleProperty.getFacets());
        newSimpleProperty.setMimeType(oldSimpleProperty.getMimeType());
        newSimpleProperty.setName(oldSimpleProperty.getName());
        newSimpleProperty.setType(oldSimpleProperty.getType());
        JPAEdmMappingImpl newMapping = new JPAEdmMappingImpl();
        Mapping mapping = oldSimpleProperty.getMapping();
        JPAEdmMapping oldMapping = (JPAEdmMapping) mapping;
        newMapping.setJPAColumnName(oldMapping.getJPAColumnName());
        newMapping.setInternalName(embeddablePropertyName + "." + mapping.getInternalName());
        newMapping.setMimeType(mapping.getMimeType());
        newMapping.setObject(mapping.getObject());
        newMapping.setJPAType(oldMapping.getJPAType());
        //newMapping.addToJPATypeHierachy(complexTypeMapping.getJPAType());
        //newMapping.addToJPATypeHierachy(oldMapping.getJPAType());
        
        newSimpleProperty.setMapping(newMapping);
        expandedList.add(newSimpleProperty);
      } catch (ClassCastException e) {
        ComplexProperty complexProperty = (ComplexProperty) property;
        String name = complexProperty.getMapping().getInternalName();
        expandEdmComplexType(searchComplexTypeByName(complexProperty.getName()), expandedList, name);
      }
    }

  }

  private class JPAEdmComplexTypeBuilder implements JPAEdmBuilder {
    /*
     * 
     * Each call to build method creates a new Complex Type.
     * The Complex Type is created only if it is not created
     * earlier. A local buffer is maintained to track the list
     * of complex types created.
     *  
     * ************************************************************
     * 				Build EDM Complex Type - STEPS
     * ************************************************************
     * 1) Fetch list of embeddable types from JPA Model
     * 2) Search local buffer if there exists already a Complex 
     * type for the embeddable type. 
     * 3) If the complex type was already been built continue with
     * the next embeddable type, else create new EDM Complex Type.
     * 4) Create a Property view with Complex Type
     * 5) Get Property Builder and build the Property with Complex
     * type.
     * 6) Set EDM complex type with list of properties built by
     * the property view
     * 7) Provide name for EDM complex type.
     * 
     * ************************************************************
     * 				Build EDM Complex Type - STEPS
     * ************************************************************
     *
     */
    @Override
    public void build() throws ODataJPAModelException, ODataJPARuntimeException {
      Set<EmbeddableType<?>> embeddables = new HashSet<EmbeddableType<?>>();

      if (consistentComplextTypes == null) {
        consistentComplextTypes = new ArrayList<ComplexType>();
      }

      if (searchMap == null) {
        searchMap = new HashMap<String, ComplexType>();
      }

      if (directBuild) {
        embeddables = schemaView.getJPAMetaModel().getEmbeddables();
      } else {
        embeddables.add(nestedComplexType);
      }

      for (EmbeddableType<?> embeddableType : embeddables) {

        currentEmbeddableType = embeddableType;
        String searchKey = embeddableType.getJavaType().getName();

        if (searchMap.containsKey(searchKey)) {
          continue;
        }

        // Check for need to Exclude
        if (isExcluded(JPAEdmComplexType.this)) {
          continue;
        }

        JPAEdmPropertyView propertyView = new JPAEdmProperty(
            schemaView, JPAEdmComplexType.this);
        propertyView.getBuilder().build();

        currentComplexType = new ComplexType();
        currentComplexType
            .setProperties(propertyView.getEdmPropertyList());
        JPAEdmNameBuilder.build(JPAEdmComplexType.this);

        searchMap.put(searchKey, currentComplexType);
        consistentComplextTypes.add(currentComplexType);

      }

    }

    private boolean isExcluded(final JPAEdmComplexType jpaEdmComplexType) {

      JPAEdmMappingModelAccess mappingModelAccess = jpaEdmComplexType
          .getJPAEdmMappingModelAccess();
      if (mappingModelAccess != null
          && mappingModelAccess.isMappingModelExists()
          && mappingModelAccess.checkExclusionOfJPAEmbeddableType(jpaEdmComplexType.getJPAEmbeddableType()
              .getJavaType().getSimpleName())) {
        return true;
      }
      return false;
    }

  }
}
