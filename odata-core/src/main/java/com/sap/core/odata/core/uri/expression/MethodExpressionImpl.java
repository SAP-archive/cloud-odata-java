package com.sap.core.odata.core.uri.expression;

import java.util.List;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.MethodOperator;
import com.sap.core.odata.api.uri.expression.MethodExpression;

public class MethodExpressionImpl implements MethodExpression {

  public MethodExpressionImpl(Object stringValue) {
    // TODO Auto-generated constructor stub
  }

  @Override
  public EdmType getEdmType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setEdmType(EdmType edmType) {
    // TODO Auto-generated method stub

  }

  @Override
  public MethodOperator getMethod() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<CommonExpression> getParameters() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CommonExpression getParameterCount() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CommonExpression appendParameter(CommonExpression ls_tmp_node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ExpressionKind getKind() {
    
    return ExpressionKind.METHOD;
  }

  @Override
  public String toUriLiteral() {
    return "Error"; // to do change this
  }
 

}
