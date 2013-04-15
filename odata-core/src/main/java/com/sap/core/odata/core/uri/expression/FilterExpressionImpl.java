/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.FilterExpression;

public class FilterExpressionImpl implements FilterExpression
{
  private final String filterString;
  private EdmType edmType;
  private CommonExpression commonExpression;

  public FilterExpressionImpl(final String filterExpression)
  {
    filterString = filterExpression;
  }

  public FilterExpressionImpl(final String filterExpression, final CommonExpression childExpression)
  {
    filterString = filterExpression;
    commonExpression = childExpression;
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
  public Object accept(final ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException
  {
    Object retCommonExpression = commonExpression.accept(visitor);

    return visitor.visitFilterExpression(this, filterString, retCommonExpression);
  }

  @Override
  public CommonExpression setEdmType(final EdmType edmType)
  {
    this.edmType = edmType;
    return this;

  }

  @Override
  public EdmType getEdmType()
  {
    return edmType;
  }

  @Override
  public ExpressionKind getKind()
  {
    return ExpressionKind.FILTER;
  }

  @Override
  public String getUriLiteral()
  {
    return getExpressionString();
  }

}
