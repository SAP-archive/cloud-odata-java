package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.FilterExpression;

public class FilterExpressionImpl implements FilterExpression {
  private String filterString;
  private EdmType edmType;
  private CommonExpression commonExpression;
  
  public FilterExpressionImpl(String filterExpression)
  {
    this.filterString = filterExpression;
  }

  public FilterExpressionImpl(String filterExpression, CommonExpression childExpression)
  {
    this.filterString = filterExpression;
    this.commonExpression = childExpression;
  }

  @Override
  public String getExpressionString() {
    return filterString;
  }

  @Override
  public CommonExpression getExpression() {
    return commonExpression;
  }

  @Override
  public Object accept(ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException
  {
    Object retCommonExpression = commonExpression.accept(visitor);
    Object ret = visitor.visitFilterExpression(this, filterString, retCommonExpression);
    return ret;
  }

  @Override
  public CommonExpression setEdmType(EdmType edmType) {
    this.edmType = edmType;
    return this;

  }

  @Override
  public EdmType getEdmType() {
    return edmType;
  }

  @Override
  public ExpressionKind getKind() {
    return ExpressionKind.FILTER;
  }

  @Override
  public String getUriLiteral() {
    return "";
  }

}
