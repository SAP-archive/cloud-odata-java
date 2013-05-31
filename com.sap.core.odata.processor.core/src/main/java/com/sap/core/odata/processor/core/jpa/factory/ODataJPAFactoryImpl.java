/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.processor.core.jpa.factory;

import java.util.Locale;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmMappingModelAccess;
import com.sap.core.odata.processor.api.jpa.access.JPAMethodContext.JPAMethodContextBuilder;
import com.sap.core.odata.processor.api.jpa.access.JPAProcessor;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAMessageService;
import com.sap.core.odata.processor.api.jpa.factory.JPAAccessFactory;
import com.sap.core.odata.processor.api.jpa.factory.JPQLBuilderFactory;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAAccessFactory;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextView;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement.JPQLStatementBuilder;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmModelView;
import com.sap.core.odata.processor.core.jpa.ODataJPAContextImpl;
import com.sap.core.odata.processor.core.jpa.ODataJPAProcessorDefault;
import com.sap.core.odata.processor.core.jpa.access.data.JPAFunctionContext;
import com.sap.core.odata.processor.core.jpa.access.data.JPAProcessorImpl;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmMappingModelService;
import com.sap.core.odata.processor.core.jpa.edm.ODataJPAEdmProvider;
import com.sap.core.odata.processor.core.jpa.exception.ODataJPAMessageServiceDefault;
import com.sap.core.odata.processor.core.jpa.jpql.JPQLJoinSelectContext;
import com.sap.core.odata.processor.core.jpa.jpql.JPQLJoinSelectSingleContext;
import com.sap.core.odata.processor.core.jpa.jpql.JPQLJoinSelectSingleStatementBuilder;
import com.sap.core.odata.processor.core.jpa.jpql.JPQLJoinStatementBuilder;
import com.sap.core.odata.processor.core.jpa.jpql.JPQLSelectContext;
import com.sap.core.odata.processor.core.jpa.jpql.JPQLSelectSingleContext;
import com.sap.core.odata.processor.core.jpa.jpql.JPQLSelectSingleStatementBuilder;
import com.sap.core.odata.processor.core.jpa.jpql.JPQLSelectStatementBuilder;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmModel;

public class ODataJPAFactoryImpl extends ODataJPAFactory {

  @Override
  public JPQLBuilderFactory getJPQLBuilderFactory() {
    return JPQLBuilderFactoryImpl.create();
  };

  @Override
  public JPAAccessFactory getJPAAccessFactory() {
    return JPAAccessFactoryImpl.create();
  };

  @Override
  public ODataJPAAccessFactory getODataJPAAccessFactory() {
    return ODataJPAAccessFactoryImpl.create();
  };

  private static class JPQLBuilderFactoryImpl implements JPQLBuilderFactory {

    private static JPQLBuilderFactoryImpl factory = null;

    private JPQLBuilderFactoryImpl() {}

    @Override
    public JPQLStatementBuilder getStatementBuilder(final JPQLContextView context) {
      JPQLStatementBuilder builder = null;
      switch (context.getType()) {
      case SELECT:
      case SELECT_COUNT: // for $count, Same as select
        builder = new JPQLSelectStatementBuilder(context);
        break;
      case SELECT_SINGLE:
        builder = new JPQLSelectSingleStatementBuilder(context);
        break;
      case JOIN:
      case JOIN_COUNT: // for $count, Same as join
        builder = new JPQLJoinStatementBuilder(context);
        break;
      case JOIN_SINGLE:
        builder = new JPQLJoinSelectSingleStatementBuilder(context);
      default:
        break;
      }

      return builder;
    }

    @Override
    public JPQLContextBuilder getContextBuilder(final JPQLContextType contextType) {
      JPQLContextBuilder contextBuilder = null;

      switch (contextType) {
      case SELECT:
        JPQLSelectContext selectContext = new JPQLSelectContext(false);
        contextBuilder = selectContext.new JPQLSelectContextBuilder();
        break;
      case SELECT_SINGLE:
        JPQLSelectSingleContext singleSelectContext = new JPQLSelectSingleContext();
        contextBuilder = singleSelectContext.new JPQLSelectSingleContextBuilder();
        break;
      case JOIN:
        JPQLJoinSelectContext joinContext = new JPQLJoinSelectContext(
            false);
        contextBuilder = joinContext.new JPQLJoinContextBuilder();
        break;
      case JOIN_SINGLE:
        JPQLJoinSelectSingleContext joinSingleContext = new JPQLJoinSelectSingleContext();
        contextBuilder = joinSingleContext.new JPQLJoinSelectSingleContextBuilder();
        break;
      case SELECT_COUNT:
        JPQLSelectContext selectCountContext = new JPQLSelectContext(
            true);
        contextBuilder = selectCountContext.new JPQLSelectContextBuilder();
        break;
      case JOIN_COUNT:
        JPQLJoinSelectContext joinCountContext = new JPQLJoinSelectContext(
            true);
        contextBuilder = joinCountContext.new JPQLJoinContextBuilder();
      default:
        break;
      }

      return contextBuilder;
    }

    private static JPQLBuilderFactory create() {
      if (factory == null) {
        return new JPQLBuilderFactoryImpl();
      } else {
        return factory;
      }
    }

    @Override
    public JPAMethodContextBuilder getJPAMethodContextBuilder(
        final JPQLContextType contextType) {

      JPAMethodContextBuilder contextBuilder = null;
      switch (contextType) {
      case FUNCTION:
        JPAFunctionContext methodConext = new JPAFunctionContext();
        contextBuilder = methodConext.new JPAFunctionContextBuilder();

        break;
      default:
        break;
      }
      return contextBuilder;
    }

  }

  private static class ODataJPAAccessFactoryImpl implements
      ODataJPAAccessFactory {

    private static ODataJPAAccessFactoryImpl factory = null;

    private ODataJPAAccessFactoryImpl() {}

    @Override
    public ODataSingleProcessor createODataProcessor(
        final ODataJPAContext oDataJPAContext) {
      return new ODataJPAProcessorDefault(oDataJPAContext);
    }

    @Override
    public EdmProvider createJPAEdmProvider(final ODataJPAContext oDataJPAContext) {
      return new ODataJPAEdmProvider(oDataJPAContext);
    }

    @Override
    public ODataJPAContext createODataJPAContext() {
      return new ODataJPAContextImpl();
    }

    private static ODataJPAAccessFactoryImpl create() {
      if (factory == null) {
        return new ODataJPAAccessFactoryImpl();
      } else {
        return factory;
      }
    }

    @Override
    public ODataJPAMessageService getODataJPAMessageService(final Locale locale) {
      return ODataJPAMessageServiceDefault.getInstance(locale);
    }

  }

  private static class JPAAccessFactoryImpl implements JPAAccessFactory {

    private static JPAAccessFactoryImpl factory = null;

    private JPAAccessFactoryImpl() {}

    @Override
    public JPAEdmModelView getJPAEdmModelView(
        final ODataJPAContext oDataJPAContext) {
      JPAEdmModelView view = null;

      view = new JPAEdmModel(oDataJPAContext);
      return view;
    }

    @Override
    public JPAProcessor getJPAProcessor(final ODataJPAContext oDataJPAContext) {
      JPAProcessor jpaProcessor = new JPAProcessorImpl(oDataJPAContext);

      return jpaProcessor;
    }

    private static JPAAccessFactoryImpl create() {
      if (factory == null) {
        return new JPAAccessFactoryImpl();
      } else {
        return factory;
      }
    }

    @Override
    public JPAEdmMappingModelAccess getJPAEdmMappingModelAccess(
        final ODataJPAContext oDataJPAContext) {
      JPAEdmMappingModelAccess mappingModelAccess = new JPAEdmMappingModelService(
          oDataJPAContext);

      return mappingModelAccess;
    }

  }
}
