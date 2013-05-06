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
package com.sap.core.odata.api.annotation.edm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sap.core.odata.api.annotation.edmx.HttpMethod;
import com.sap.core.odata.api.annotation.edmx.HttpMethod.Name;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FunctionImport {

  enum ReturnType {
    SCALAR, ENTITY_TYPE, COMPLEX_TYPE, NONE
  }

  enum Multiplicity {
    MANY, ONE
  }

  String name() default "";

  String entitySet() default "";

  ReturnType returnType();

  Multiplicity multiplicity() default Multiplicity.ONE;

  HttpMethod httpMethod() default @HttpMethod(name = Name.GET);

  Documentation documentation() default @Documentation;
}
