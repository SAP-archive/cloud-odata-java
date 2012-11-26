package com.sap.core.odata.core.uri.expression;

import java.util.List;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.FunctionExpression;

public class FunctionExpressionImpl implements FunctionExpression {

  public FunctionExpressionImpl(Object stringValue) {
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
  public String GetFunctionName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<CommonExpression> GetParameters() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CommonExpression GetParameterCount() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CommonExpression AppendParameter(CommonExpression ls_tmp_node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ExpressionKind getKind() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toUriLiteral() {
    // TODO Auto-generated method stub
    return null;
  }

}
