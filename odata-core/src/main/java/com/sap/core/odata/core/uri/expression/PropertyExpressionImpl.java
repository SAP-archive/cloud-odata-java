package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.PropertyExpression;

public class PropertyExpressionImpl implements PropertyExpression, EdmTyped {
  String uriLiteral;

  public PropertyExpressionImpl(Object stringValue) {
    // TODO Auto-generated constructor stub
  }

  public PropertyExpressionImpl(Object stringValue, EdmTyped lo_edm_property, String uriLiteral) {
    // TODO Auto-generated constructor stub
    this.uriLiteral = uriLiteral;
  }

  @Override
  public void setEdmType(EdmType edmType) {
    // TODO Auto-generated method stub

  }

  @Override
  public String getPropertyName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmProperty getEdmProperty() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getName() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmType getType() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmMultiplicity getMultiplicity() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public EdmType getEdmType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ExpressionKind getKind() {
    return ExpressionKind.PROPERTY;
  }

  @Override
  public String toUriLiteral() {
     return uriLiteral;
  }
 


}
