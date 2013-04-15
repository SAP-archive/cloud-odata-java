/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.uri.expression;

import java.util.List;

import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmTyped;

/**
 * Interface {@link ExpressionVisitor} is used to traverse a $filter or $orderby expression tree.
 * Any class instance implementing this interface can be passed to the method {@link Visitable#accept(ExpressionVisitor)}
 * of an expression node to start the traversing. While traversing, the appropriate methods of the visitor implementation
 * will be called.
 * @author SAP AG
 */
public interface ExpressionVisitor {
  /**
   * Visits a filter expression
   * @param filterExpression
   *   The visited filter expression node
   * @param expressionString
   *   The $filter expression string used to build the filter expression tree   
   * @param expression
   *   The expression node representing the first <i>operator</i>,<i>method</i>,<i>literal</i> or <i>property</i> of the expression tree
   * @return
   *   The overall result of evaluating the whole filter expression tree    
   */
  Object visitFilterExpression(FilterExpression filterExpression, String expressionString, Object expression);

  /**
   * Visits a binary expression
   * @param binaryExpression
   *   The visited binary expression node
   * @param operator
   *   The operator used in the binary expression
   * @param leftSide
   *   The result of visiting the left expression node
   * @param rightSide
   *   The result of visiting the right expression node
   * @return
   *   Returns the result from evaluating operator, leftSide and rightSide 
   */
  Object visitBinary(BinaryExpression binaryExpression, BinaryOperator operator, Object leftSide, Object rightSide);

  /**
   * Visits a orderby expression
   * @param orderByExpression
   *   The visited orderby expression node
   * @param expressionString
   *   The $orderby expression string used to build the orderby expression tree   
   * @param orders
   *   The result of visiting the orders of the orderby expression
   * @return
   *   The overall result of evaluating the orderby expression tree  
   */
  Object visitOrderByExpression(OrderByExpression orderByExpression, String expressionString, List<Object> orders);

  /**
   * Visits a order expression
   * @param orderExpression
   *   The visited order expression node
   * @param filterResult
   *   The result of visiting the filter expression contained in the order
   * @param sortOrder
   *   The sort order
   * @return
   *   The overall result of evaluating the order
   */
  Object visitOrder(OrderExpression orderExpression, Object filterResult, SortOrder sortOrder);

  /**
   * Visits a literal expression 
   * @param literal
   *   The visited literal expression node
   * @param edmLiteral
   *   The detected EDM literal (value and type)  
   * @return
   *   The value of the literal 
   */
  Object visitLiteral(LiteralExpression literal, EdmLiteral edmLiteral);

  /**
   * Visits a method expression
   * @param methodExpression
   *   The visited method expression node
   * @param method
   *   The method used in the method expression
   * @param parameters
   *   The result of visiting the parameters of the method 
   * @return
   *   Returns the result from evaluating the method and the method parameters 
   */
  Object visitMethod(MethodExpression methodExpression, MethodOperator method, List<Object> parameters);

  /**
   * Visits a member expression (e.g. <path property>/<member property>)
   * @param memberExpression
   *   The visited member expression node
   * @param path
   *   The result of visiting the path property expression node (the left side of the property operator)
   * @param property
   *   The result of visiting the member property expression node
   * @return
  *    Returns the <b>value</b> of the corresponding property (which may be a single EDM value or a structured EDM value) 
   */
  Object visitMember(MemberExpression memberExpression, Object path, Object property);

  /** 
  * Visits a property expression
  * @param propertyExpression
  *   The visited property expression node
  * @param uriLiteral
  *   The URI literal of the property
  * @param edmProperty
  *   The EDM property matching the property name used in the expression String 
  * @return
  *   Returns the <b>value</b> of the corresponding property ( which may be a single EDM value or a structured EDM value)
  */
  Object visitProperty(PropertyExpression propertyExpression, String uriLiteral, EdmTyped edmProperty);

  /**
   * Visits a unary expression
   * @param unaryExpression
   *   The visited unary expression node
   * @param operator
   *   The operator used in the unary expression 
   * @param operand
   *   The result of visiting the operand expression node
   * @return
   *   Returns the result from evaluating operator and operand
   */
  Object visitUnary(UnaryExpression unaryExpression, UnaryOperator operator, Object operand);
}
