package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.FilterExpression;

public class FilterExpressionImpl implements FilterExpression {

  private String filterExpression;
  private CommonExpression commonExpression;
  
  public FilterExpressionImpl(String filterExpression, CommonExpression childExpression) 
  {
    this.filterExpression = filterExpression;
    this.commonExpression = childExpression;
  }

  @Override
  public String getFilterExpression() {
    return filterExpression;
  }

  @Override
  public CommonExpression getExpression() {
    return commonExpression;
  }

 
}
