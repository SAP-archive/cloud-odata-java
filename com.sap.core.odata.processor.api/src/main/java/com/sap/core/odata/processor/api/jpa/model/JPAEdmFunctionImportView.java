package com.sap.core.odata.processor.api.jpa.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.FunctionImport;

/**
 * <p>
 * A view on EDM Function Imports. EDM function imports are derived from Java
 * class methods annotated with EDM Annotations.
 * </p>
 * <p>
 * The implementation of the view provides access to EDM Function Import created
 * from Java class methods. The implementation act as a container for list of
 * function imports that are consistent.
 * </p>
 * 
 * @author SAP AG
 *         <p>
 * @DoNotImplement
 * 
 */
public interface JPAEdmFunctionImportView extends JPAEdmBaseView {

  /**
   * The method returns a list of consistent Function Imports. A function
   * import is said to be consistent only if it adheres to the rules defined
   * in CSDL.
   * 
   * @return a list of type
   *         {@link com.sap.core.odata.api.edm.provider.FunctionImport}
   */
  List<FunctionImport> getConsistentFunctionImportList();
}
