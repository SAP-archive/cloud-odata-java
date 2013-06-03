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

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Parameter {
  enum Mode {
    IN {
      @Override
      public String toString() {
        return new String("In");
      }
    },
    OUT {
      @Override
      public String toString() {
        return new String("Out");
      }
    },
    INOUT {
      @Override
      public String toString() {
        return new String("InOut");
      }
    }
  };

  String name();

  Mode mode() default Mode.IN;

  Facets facets() default @Facets;

  Documentation documentation() default @Documentation;
}
