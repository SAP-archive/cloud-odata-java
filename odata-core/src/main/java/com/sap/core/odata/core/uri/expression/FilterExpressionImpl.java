package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.FilterExpression;

public class FilterExpressionImpl implements FilterExpression {

  private String filterString;
  private CommonExpression commonExpression;

  public FilterExpressionImpl(String filterExpression, CommonExpression childExpression)
  {
    this.filterString = filterExpression;
    this.commonExpression = childExpression;
  }

  @Override
  public String getFilterExpression() {
    return filterString;
  }

  @Override
  public CommonExpression getExpression() {
    return commonExpression;
  }

  @Override
  public Object accept(ExpressionVisitor visitor) throws ExceptionVisitExpression
  {
    Object retCommonExpression = commonExpression.accept(visitor);
    Object ret = visitor.visitFilterExpression(this, filterString, retCommonExpression);
    return ret;
  }

  @Override
  public void setEdmType(EdmType edmType) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public EdmType getEdmType() {
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
