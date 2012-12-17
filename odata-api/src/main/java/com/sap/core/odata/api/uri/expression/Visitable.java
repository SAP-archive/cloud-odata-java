package com.sap.core.odata.api.uri.expression;
/*X*/
import com.sap.core.odata.api.exception.ODataApplicationException;


/**
 * The interface {@link Visitable} is part of the visitor pattern used to traverse 
 * the expression tree build from a $filter expression string or $orderby expression string.
 * It is implemented by each expression used as node in an expression tree.
 * @author SAP AG
 * @see ExpressionVisitor
 *
 */
public interface Visitable {
  
  /**
   * Method {@link #accept(ExpressionVisitor)} is called when traversing the expression tree. This method is invoked on each 
   * expression used as node in an expression tree. The implementations should roughly  
   * behave as follows:
   * <li>Call accept on all sub nodes and store the returned Objects
   * <li>Call the appropriate method on the {@line ExpressionVisitor} instance and provide the stored objects to that instance 
   * <li>Return the object returned by the {@line ExpressionVisitor} method.
   * <br>
   * <br>
   * @param visitor 
   *   Class whose methods are called during traversing a expression node of the expression tree.
   * @return
   *   Object returned by the {@line ExpressionVisitor} method.
   * @throws ExceptionVisitExpression
   *   Exception occurred the OData library while traversing the tree
   * @throws ODataApplicationException
   *   Exception thrown by the application who implemented the visitor  
   */
  Object accept(ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException;
}
