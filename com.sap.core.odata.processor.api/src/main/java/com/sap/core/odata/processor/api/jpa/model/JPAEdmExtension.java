package com.sap.core.odata.processor.api.jpa.model;

/**
 * The interface provides methods to extend JPA EDM containers.
 * 
 * @author SAP AG
 * 
 */
public interface JPAEdmExtension {
  /**
   * The method is used to extend the JPA EDM schema view. Use this method to
   * register custom operations.
   * 
   * @param view
   *            is the schema view
   * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView#registerOperations(Class,
   *      String[])
   * @deprecated Use {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmExtension#extendWithOperations(JPAEdmSchemaView view)}
   * 
   */
  @Deprecated
  public void extend(JPAEdmSchemaView view);

  /**
   * The method is used to extend the JPA EDM schema view with custom operations. Use this method to
   * register custom operations.
   * 
   * @param view
   *            is the schema view
   * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView#registerOperations(Class,
   *      String[])
   * 
   */
  public void extendWithOperation(JPAEdmSchemaView view);

  /**
   * The method is used to extend the JPA EDM schema view with Entities, Entity Sets, Navigation Property and Association. 
   * 
   * @param view
   *            is the schema view
   * 
   */
  public void extendJPAEdmSchema(JPAEdmSchemaView view);

}
