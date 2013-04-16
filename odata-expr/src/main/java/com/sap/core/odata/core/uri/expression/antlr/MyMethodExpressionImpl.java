/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.core.uri.expression.antlr;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.MethodOperator;

public class MyMethodExpressionImpl implements MethodExpression {

  private final MyInfoMethod infoMethod;
  private EdmType returnType;
  private final List<CommonExpression> actualParameters;

  public MyMethodExpressionImpl(final MyInfoMethod infoMethod) {
    this.infoMethod = infoMethod;
    returnType = infoMethod.getReturnType();
    actualParameters = new ArrayList<CommonExpression>();
  }

  @Override
  public EdmType getEdmType() {
    return returnType;
  }

  @Override
  public CommonExpression setEdmType(final EdmType edmType) {
    returnType = edmType;
    return this;
  }

  @Override
  public MethodOperator getMethod() {
    return infoMethod.getMethod();
  }

  @Override
  public List<CommonExpression> getParameters() {
    return actualParameters;
  }

  @Override
  public int getParameterCount() {
    return actualParameters.size();
  }

  /**
   * @param expression
   * @return "this" self reference for method chaining" 
   */
  public MyMethodExpressionImpl appendParameter(final CommonExpression expression) {
    actualParameters.add(expression);
    return this;
  }

  @Override
  public ExpressionKind getKind() {
    return ExpressionKind.METHOD;
  }

  @Override
  public String getUriLiteral() {
    return infoMethod.getSyntax();
  }

  @Override
  public Object accept(final ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException {
    final Vector<Object> retParameters = new Vector<Object>();
    for (final CommonExpression parameter : actualParameters) {
      final Object retParameter = parameter.accept(visitor);
      retParameters.add(retParameter);
    }

    final Object ret = visitor.visitMethod(this, getMethod(), retParameters);
    return ret;
  }

}
