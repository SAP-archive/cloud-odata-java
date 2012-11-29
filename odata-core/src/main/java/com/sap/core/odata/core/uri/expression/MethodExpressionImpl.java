package com.sap.core.odata.core.uri.expression;

import java.util.Vector;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.MethodOperator;

public class MethodExpressionImpl implements MethodExpression {

  InfoMethod infoMethod;
  EdmType returnType;
  Vector<CommonExpression> actualParameters;

  public MethodExpressionImpl(InfoMethod infoMethod) {
    this.infoMethod = infoMethod;
    this.returnType = infoMethod.getReturnType();
    this.actualParameters = new Vector<CommonExpression>();
  }

  @Override
  public EdmType getEdmType() {
    return returnType;
  }

  @Override
  public void setEdmType(EdmType edmType) {
    this.returnType = edmType;
  }

  @Override
  public MethodOperator getMethod()
  {
    return infoMethod.getMethod();
  }

  @Override
  public Vector<CommonExpression> getParameters() {
    return actualParameters;
  }

  @Override
  public int getParameterCount() {
    return actualParameters.size();
  }

  public CommonExpression appendParameter(CommonExpression expression) {
    actualParameters.add(expression);
    return null;
  }

  @Override
  public ExpressionKind getKind() {
    return ExpressionKind.METHOD;
  }

  @Override
  public String toUriLiteral() {
    return infoMethod.getSyntax();
  }

  @Override
  public Object accept(ExpressionVisitor visitor)
  {
    Vector<Object> retParameters = new Vector<Object>();
    for (CommonExpression parameter : actualParameters)
    {
      Object retParameter = parameter.accept(visitor);
      retParameters.add(retParameter);
    }

    Object ret = visitor.visitMethod(this, this.getMethod(), retParameters);
    return ret;
  }

}
