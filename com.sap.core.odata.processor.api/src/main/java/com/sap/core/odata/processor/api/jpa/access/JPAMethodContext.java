package com.sap.core.odata.processor.api.jpa.access;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;

/**
 * The abstract class is a compilation of objects required for building specific
 * instances of JPA Method Context. Extend this class to implement specific
 * implementations of JPQL context types (Create,Update,Function). A JPA method
 * Context is constructed from an OData request. Depending on OData
 * CUD/FunctionImport operation performed on an Entity, a corresponding JPA
 * method context object is built. The object thus built can be used for
 * executing operations on JPA Entity/Custom processor objects. <br>
 * A default implementation is provided by the library.
 * 
 * @author SAP AG
 * @see com.sap.core.odata.processor.api.jpa.access.JPAMethodContextView
 * @see com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType
 * 
 */

public abstract class JPAMethodContext implements JPAMethodContextView {

  protected Object enclosingObject;
  protected ArrayList<JPAFunction> jpaFunction;

  @Override
  /**
   * The method returns list of JPA functions that can be executed on the
   * enclosing object.
   * 
   * @return an instance of list of JPA Function
   */
  public Object getEnclosingObject() {
    return enclosingObject;
  }

  @Override
  /**
   * The method returns list of JPA functions that can be executed on the
   * enclosing object.
   * 
   * @return an instance of list of JPA Function
   */
  public List<JPAFunction> getJPAFunctionList() {
    return jpaFunction;
  }

  protected void setEnclosingObject(final Object enclosingObject) {
    this.enclosingObject = enclosingObject;
  }

  protected void setJpaFunction(final List<JPAFunction> jpaFunctionList) {
    jpaFunction = (ArrayList<JPAFunction>) jpaFunctionList;
  }

  /**
   * the method instantiates an instance of type JPAMethodContextBuilder.
   * 
   * @param contextType
   *            indicates the type of JPQLContextBuilder to instantiate.
   * @param resultsView
   *            is the OData request view
   * @return {@link com.sap.core.odata.processor.api.jpa.access.JPAMethodContext.JPAMethodContextBuilder}
   * 
   * @throws ODataJPARuntimeException
   */
  public final static JPAMethodContextBuilder createBuilder(
      final JPQLContextType contextType, final Object resultsView)
      throws ODataJPARuntimeException {
    return JPAMethodContextBuilder.create(contextType, resultsView);
  }

  /**
   * The abstract class is extended by specific JPA Method Context Builder to
   * build JPA Method Context types.
   * 
   * @author SAP AG
   * 
   */
  public static abstract class JPAMethodContextBuilder {

    /**
     * Implement this method to build JPAMethodContext
     * 
     * @return an instance of type JPAMethodContext
     * @throws ODataJPAModelException
     * @throws ODataJPARuntimeException
     */
    public abstract JPAMethodContext build() throws ODataJPAModelException,
        ODataJPARuntimeException;

    protected JPAMethodContextBuilder() {}

    private static JPAMethodContextBuilder create(
        final JPQLContextType contextType, final Object resultsView)
        throws ODataJPARuntimeException {
      JPAMethodContextBuilder contextBuilder = ODataJPAFactory
          .createFactory().getJPQLBuilderFactory()
          .getJPAMethodContextBuilder(contextType);

      if (contextBuilder == null) {
        throw ODataJPARuntimeException
            .throwException(
                ODataJPARuntimeException.ERROR_JPQLCTXBLDR_CREATE,
                null);
      }
      contextBuilder.setResultsView(resultsView);
      return contextBuilder;
    }

    protected abstract void setResultsView(Object resultsView);
  }
}
