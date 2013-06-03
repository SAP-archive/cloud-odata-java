package com.sap.core.odata.processor.core.jpa.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sap.core.odata.api.annotation.edm.FunctionImport.Multiplicity;
import com.sap.core.odata.api.annotation.edm.FunctionImport.ReturnType;
import com.sap.core.odata.api.annotation.edm.Parameter;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Facets;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.FunctionImportParameter;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmFunctionImportView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmMapping;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmNameBuilder;
import com.sap.core.odata.processor.core.jpa.access.model.JPATypeConvertor;

public class JPAEdmFunctionImport extends JPAEdmBaseViewImpl implements
    JPAEdmFunctionImportView {

  private List<FunctionImport> consistentFunctionImportList = new ArrayList<FunctionImport>();
  private JPAEdmBuilder builder = null;
  private JPAEdmSchemaView schemaView;

  public JPAEdmFunctionImport(final JPAEdmSchemaView view) {
    super(view);
    schemaView = view;
  }

  @Override
  public JPAEdmBuilder getBuilder() {
    if (builder == null) {
      builder = new JPAEdmFunctionImportBuilder();
    }
    return builder;
  }

  @Override
  public List<FunctionImport> getConsistentFunctionImportList() {
    return consistentFunctionImportList;
  }

  protected class JPAEdmFunctionImportBuilder implements JPAEdmBuilder {

    private JPAEdmEntityTypeView jpaEdmEntityTypeView = null;
    private JPAEdmComplexTypeView jpaEdmComplexTypeView = null;

    @Override
    public void build() throws ODataJPAModelException,
        ODataJPARuntimeException {

      HashMap<Class<?>, String[]> customOperations = schemaView
          .getRegisteredOperations();

      jpaEdmEntityTypeView = schemaView.getJPAEdmEntityContainerView()
          .getJPAEdmEntitySetView().getJPAEdmEntityTypeView();
      jpaEdmComplexTypeView = schemaView.getJPAEdmComplexTypeView();

      if (customOperations != null) {

        for (Class<?> clazz : customOperations.keySet()) {

          String[] operationNames = customOperations.get(clazz);
          Method[] methods = clazz.getMethods();
          Method method = null;

          int length = 0;
          if (operationNames != null) {
            length = operationNames.length;
          } else {
            length = methods.length;
          }

          boolean found = false;
          for (int i = 0; i < length; i++) {

            try {
              if (operationNames != null) {
                for (Method method2 : methods) {
                  if (method2.getName().equals(
                      operationNames[i])) {
                    found = true;
                    method = method2;
                    break;
                  }
                }
                if (found == true) {
                  found = false;
                } else {
                  continue;
                }
              } else {
                method = methods[i];
              }

              FunctionImport functionImport = buildFunctionImport(method);
              if (functionImport != null) {
                consistentFunctionImportList
                    .add(functionImport);
              }

            } catch (SecurityException e) {
              throw ODataJPAModelException.throwException(
                  ODataJPAModelException.GENERAL, e);
            }
          }
        }
      }
    }

    private FunctionImport buildFunctionImport(final Method method)
        throws ODataJPAModelException {

      com.sap.core.odata.api.annotation.edm.FunctionImport annotation = method
          .getAnnotation(com.sap.core.odata.api.annotation.edm.FunctionImport.class);
      if (annotation != null && annotation.returnType() != ReturnType.NONE) {
        FunctionImport functionImport = new FunctionImport();

        if (annotation.name().equals("")) {
          functionImport.setName(method.getName());
        } else {
          functionImport.setName(annotation.name());
        }

        JPAEdmMapping mapping = new JPAEdmMappingImpl();
        ((Mapping) mapping).setInternalName(method.getName());
        mapping.setJPAType(method.getDeclaringClass());
        functionImport.setMapping((Mapping) mapping);

        functionImport.setHttpMethod(annotation.httpMethod().name().toString());

        buildReturnType(functionImport, method, annotation);
        buildParameter(functionImport, method);

        return functionImport;
      }
      return null;
    }

    private void buildParameter(final FunctionImport functionImport, final Method method)
        throws ODataJPAModelException {

      Annotation[][] annotations = method.getParameterAnnotations();
      Class<?>[] parameterTypes = method.getParameterTypes();
      List<FunctionImportParameter> funcImpList = new ArrayList<FunctionImportParameter>();
      JPAEdmMapping mapping = null;
      int j = 0;
      for (Annotation[] annotationArr : annotations) {
        Class<?> parameterType = parameterTypes[j++];

        for (Annotation element : annotationArr) {
          if (element instanceof Parameter) {
            Parameter annotation = (Parameter) element;
            FunctionImportParameter functionImportParameter = new FunctionImportParameter();
            if (annotation.name().equals("")) {
              throw ODataJPAModelException.throwException(
                  ODataJPAModelException.FUNC_PARAM_NAME_EXP
                      .addContent(method
                          .getDeclaringClass()
                          .getName(), method
                          .getName()), null);
            } else {
              functionImportParameter.setName(annotation.name());
            }

            functionImportParameter.setType(JPATypeConvertor
                .convertToEdmSimpleType(parameterType, null));
            functionImportParameter.setMode(annotation.mode()
                .toString());

            Facets facets = new Facets();
            if (annotation.facets().maxLength() > 0) {
              facets.setMaxLength(annotation.facets().maxLength());
            }
            if (annotation.facets().nullable() == false) {
              facets.setNullable(false);
            } else {
              facets.setNullable(true);
            }

            if (annotation.facets().precision() > 0) {
              facets.setPrecision(annotation.facets().precision());
            }
            if (annotation.facets().scale() >= 0) {
              facets.setScale(annotation.facets().scale());
            }

            functionImportParameter.setFacets(facets);
            mapping = new JPAEdmMappingImpl();
            mapping.setJPAType(parameterType);
            functionImportParameter.setMapping((Mapping) mapping);
            funcImpList.add(functionImportParameter);
          }
        }
      }
      if (!funcImpList.isEmpty()) {
        functionImport.setParameters(funcImpList);
      }
    }

    private void buildReturnType(final FunctionImport functionImport,
        final Method method,
        final com.sap.core.odata.api.annotation.edm.FunctionImport annotation)
        throws ODataJPAModelException {
      ReturnType returnType = annotation.returnType();
      Multiplicity multiplicity = null;

      if (returnType != ReturnType.NONE) {
        com.sap.core.odata.api.edm.provider.ReturnType functionReturnType = new com.sap.core.odata.api.edm.provider.ReturnType();
        multiplicity = annotation.multiplicity();

        if (multiplicity == Multiplicity.MANY) {
          functionReturnType.setMultiplicity(EdmMultiplicity.MANY);
        } else {
          functionReturnType.setMultiplicity(EdmMultiplicity.ONE);
        }

        if (returnType == ReturnType.ENTITY_TYPE) {
          String entitySet = annotation.entitySet();
          if (entitySet.equals("")) {
            throw ODataJPAModelException
                .throwException(
                    ODataJPAModelException.FUNC_ENTITYSET_EXP,
                    null);
          }
          functionImport.setEntitySet(entitySet);
        }

        Class<?> methodReturnType = method.getReturnType();
        if (methodReturnType == null
            || methodReturnType.getName().equals("void")) {
          throw ODataJPAModelException.throwException(
              ODataJPAModelException.FUNC_RETURN_TYPE_EXP
                  .addContent(method.getDeclaringClass(),
                      method.getName()), null);
        }
        switch (returnType) {
        case ENTITY_TYPE:
          EntityType edmEntityType = null;
          if (multiplicity == Multiplicity.ONE) {
            edmEntityType = jpaEdmEntityTypeView
                .searchEdmEntityType(methodReturnType
                    .getSimpleName());
          } else if (multiplicity == Multiplicity.MANY) {
            edmEntityType = jpaEdmEntityTypeView
                .searchEdmEntityType(getReturnTypeSimpleName(method));
          }

          if (edmEntityType == null) {
            throw ODataJPAModelException
                .throwException(
                    ODataJPAModelException.FUNC_RETURN_TYPE_ENTITY_NOT_FOUND
                        .addContent(
                            method.getDeclaringClass(),
                            method.getName(),
                            methodReturnType
                                .getSimpleName()),
                    null);
          }
          functionReturnType.setTypeName(JPAEdmNameBuilder.build(
              schemaView, edmEntityType.getName()));
          break;
        case SCALAR:

          EdmSimpleTypeKind edmSimpleTypeKind = JPATypeConvertor
              .convertToEdmSimpleType(methodReturnType, null);
          functionReturnType.setTypeName(edmSimpleTypeKind
              .getFullQualifiedName());

          break;
        case COMPLEX_TYPE:
          ComplexType complexType = null;
          if (multiplicity == Multiplicity.ONE) {
            complexType = jpaEdmComplexTypeView
                .searchEdmComplexType(methodReturnType
                    .getName());
          } else if (multiplicity == Multiplicity.MANY) {
            complexType = jpaEdmComplexTypeView
                .searchEdmComplexType(getReturnTypeName(method));
          }
          if (complexType == null) {
            throw ODataJPAModelException
                .throwException(
                    ODataJPAModelException.FUNC_RETURN_TYPE_ENTITY_NOT_FOUND
                        .addContent(
                            method.getDeclaringClass(),
                            method.getName(),
                            methodReturnType
                                .getSimpleName()),
                    null);
          }
          functionReturnType.setTypeName(JPAEdmNameBuilder.build(
              schemaView, complexType.getName()));
          break;
        default:
          break;
        }
        functionImport.setReturnType(functionReturnType);
      }
    }

    private String getReturnTypeName(final Method method) {
      try {
        ParameterizedType pt = (ParameterizedType) method
            .getGenericReturnType();
        Type t = pt.getActualTypeArguments()[0];
        return ((Class<?>) t).getName();
      } catch (ClassCastException e) {
        return method.getReturnType().getName();
      }
    }

    private String getReturnTypeSimpleName(final Method method) {
      try {
        ParameterizedType pt = (ParameterizedType) method
            .getGenericReturnType();
        Type t = pt.getActualTypeArguments()[0];
        return ((Class<?>) t).getSimpleName();
      } catch (ClassCastException e) {
        return method.getReturnType().getSimpleName();
      }
    }
  }
}
