package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.MemberExpression;

public class MemberExpressionImpl implements BinaryExpression, MemberExpression
{
  CommonExpression path;
  CommonExpression property;
  EdmType edmType;

  public MemberExpressionImpl(CommonExpression path, CommonExpression property)
  {
    this.path = path;
    this.property = property;
    this.edmType = property.getEdmType();
  }

  @Override
  public CommonExpression getPath() {
    return path;
  }

  @Override
  public CommonExpression getProperty() {
    return property;
  }

  @Override
  public EdmType getEdmType()
  {
    return edmType;
  }

  @Override
  public CommonExpression setEdmType(EdmType edmType) {
    this.edmType = edmType;
    return this;
  }

  @Override
  public BinaryOperator getOperator()
  {
    return BinaryOperator.PROPERTY_ACCESS;
  }

  @Override
  public ExpressionKind getKind()
  {
    return ExpressionKind.MEMBER;
  }

  @Override
  public String getUriLiteral() 
  {
    return BinaryOperator.PROPERTY_ACCESS.toUriLiteral();
  }

  @Override
  public Object accept(ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException
  {
    Object retSource = path.accept(visitor);
    Object retPath = property.accept(visitor);

    Object ret = visitor.visitMember(this, retSource, retPath);
    return ret;
  }

  @Override
  public CommonExpression getLeftOperand() {
    return path;
  }

  @Override
  public CommonExpression getRightOperand() {
    return property;
  }

}
