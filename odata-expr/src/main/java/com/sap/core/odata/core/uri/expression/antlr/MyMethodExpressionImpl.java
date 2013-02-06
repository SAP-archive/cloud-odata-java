package com.sap.core.odata.core.uri.expression.antlr;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.MethodOperator;

public class MyMethodExpressionImpl implements MethodExpression {

  private final MyInfoMethod infoMethod;
  private EdmType returnType;
  private final List<CommonExpression> actualParameters;

  public MyMethodExpressionImpl(MyInfoMethod infoMethod) {
    this.infoMethod = infoMethod;
    returnType = infoMethod.getReturnType();
    actualParameters = new ArrayList<CommonExpression>();
  }

  @Override
  public EdmType getEdmType() {
    return returnType;
  }

  @Override
  public CommonExpression setEdmType(EdmType edmType) {
    returnType = edmType;
    return this;
  }

  @Override
  public MethodOperator getMethod()
  {
    return infoMethod.getMethod();
  }

  @Override
  public List<CommonExpression> getParameters() {
    return actualParameters;
  }

  @Override
  public int getParameterCount() {
    return actualParameters.size();
  }

  /**
   * @param expression
   * @return "this" self reference for method chaining" 
   */
  public MyMethodExpressionImpl appendParameter(CommonExpression expression)
  {
    actualParameters.add(expression);
    return this;
  }

  @Override
  public ExpressionKind getKind() {
    return ExpressionKind.METHOD;
  }

  @Override
  public String getUriLiteral() {
    return infoMethod.getSyntax();
  }

  @Override
  public Object accept(ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException
  {
    final Vector<Object> retParameters = new Vector<Object>();
    for (final CommonExpression parameter : actualParameters)
    {
      final Object retParameter = parameter.accept(visitor);
      retParameters.add(retParameter);
    }

    final Object ret = visitor.visitMethod(this, getMethod(), retParameters);
    return ret;
  }

}
