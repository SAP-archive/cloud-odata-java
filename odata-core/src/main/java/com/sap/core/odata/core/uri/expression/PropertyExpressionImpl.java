package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.EdmLiteral;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.PropertyExpression;

public class PropertyExpressionImpl implements PropertyExpression {
  String uriLiteral;
  EdmType edmType;
  EdmProperty edmProperty;
  EdmLiteral edmLiteral;
  
  public PropertyExpressionImpl(String uriLiteral, EdmProperty edmProperty, EdmLiteral edmLiteral) {
    this.uriLiteral = uriLiteral;
    this.edmProperty = edmProperty;
    this.edmLiteral = edmLiteral;
    if (edmLiteral != null)
    {
      this.edmType= edmLiteral.getType();
    }
   }

  @Override
  public void setEdmType(EdmType edmType) {
    this.edmType = edmType;
  }

  @Override
  public String getPropertyName() throws EdmException {
    return edmProperty.getName();
  }

  @Override
  public  EdmProperty getEdmProperty() {
    return edmProperty;
  }

  @Override
  public ExpressionKind getKind() {
    return ExpressionKind.PROPERTY;
  }

  @Override
  public String toUriLiteral() {
     return uriLiteral;
  }

  @Override
  public EdmType getEdmType() 
  {
    return edmType;
  }

  @Override
  public Object accept(ExpressionVisitor visitor) {
    Object ret = visitor.visitProperty(this,uriLiteral, edmProperty );
    return ret;
  }



}
