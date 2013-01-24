package com.sap.core.odata.core.uri.expression;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.MethodOperator;

public class MethodExpressionImpl implements MethodExpression {

  private InfoMethod infoMethod;
  private EdmType returnType;
  private List<CommonExpression> actualParameters;

  public MethodExpressionImpl(InfoMethod infoMethod)
  {
    this.infoMethod = infoMethod;
    this.returnType = infoMethod.getReturnType();
    this.actualParameters = new ArrayList<CommonExpression>();
  }

  @Override
  public EdmType getEdmType()
  {
    return returnType;
  }

  @Override
  public CommonExpression setEdmType(EdmType edmType) 
  {
    this.returnType = edmType;
    return this;
  }

  @Override
  public MethodOperator getMethod()
  {
    return infoMethod.getMethod();
  }
  
  public InfoMethod getMethodInfo()
  {
    return infoMethod;
  }

  @Override
  public List<CommonExpression> getParameters()
  {
    return actualParameters;
  }

  @Override
  public int getParameterCount()
  {
    return actualParameters.size();
  }

  /**
   * @param expression
   * @return A self reference for method chaining" 
   */
  public MethodExpressionImpl appendParameter(CommonExpression expression) 
  {
    actualParameters.add(expression);
    return this;
  }

  @Override
  public ExpressionKind getKind() 
  {
    return ExpressionKind.METHOD;
  }

  @Override
  public String getUriLiteral()
  {
    return infoMethod.getSyntax();
  }

  @Override
  public Object accept(ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException
  {
    ArrayList<Object> retParameters = new ArrayList<Object>();
    for (CommonExpression parameter : actualParameters)
    {
      Object retParameter = parameter.accept(visitor);
      retParameters.add(retParameter);
    }

    Object ret = visitor.visitMethod(this, this.getMethod(), retParameters);
    return ret;
  }

}
