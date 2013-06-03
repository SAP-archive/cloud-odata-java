package com.sap.core.odata.processor.api.jpa.access;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * A container for JPA Functions. A JPA function can be
 * <ol>
 * <li>Property Access method</li>
 * <li>Custom Operation (Annotated with EDM Annotation FunctionImport)</li>
 * </ol>
 * 
 * @author SAP AG
 * 
 */
public class JPAFunction {

  private Method function;
  private Class<?>[] parameterTypes;
  private Type returnType;
  private Object[] args;

  public JPAFunction(final Method function, final Class<?>[] parameterTypes,
      final Type returnType, final Object[] args) {
    this.function = function;
    this.parameterTypes = parameterTypes;
    this.returnType = returnType;
    this.args = args;
  }

  /**
   * The method returns the Java method.
   * 
   * @return an instance of {@link java.lang.reflect.Method}
   */
  public Method getFunction() {
    return function;
  }

  /**
   * The method returns the parameter types for the Java method.
   * 
   * @return an array of type {@link java.lang.Class<?>}
   */
  public Class<?>[] getParameterTypes() {
    return parameterTypes;
  }

  /**
   * The method returns the return type for the Java method.
   * 
   * @return an instance of {@link java.lang.reflect.Type}
   */
  public Type getReturnType() {
    return returnType;
  }

  /**
   * The method returns an array of arguments for invoking the Java method.
   * 
   * @return an array of Objects
   */
  public Object[] getArguments() {
    return args;
  }

}
