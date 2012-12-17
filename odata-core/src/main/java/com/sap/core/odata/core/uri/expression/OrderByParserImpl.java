package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderByParser;
import com.sap.core.odata.api.uri.expression.OrderByParserException;

public class OrderByParserImpl implements OrderByParser
{
  /*instance attributes*/
  protected boolean promoteParameters = true;
  protected Edm edm = null;
  protected EdmEntityType resourceEntityType = null;
  
  public OrderByParserImpl(Edm edm, EdmEntityType edmType)
  {
    this.edm = edm;
    this.resourceEntityType = edmType;
  }
  
  
  @Override
  public OrderByExpression parseOrderByString(String expression) throws OrderByParserException, ODataMessageException {
    // TODO Auto-generated method stub
    return new OrderByExpressionImpl();
  }

}
